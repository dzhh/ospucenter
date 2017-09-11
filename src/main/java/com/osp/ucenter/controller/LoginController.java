package com.osp.ucenter.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.osp.common.json.JsonUtil;
import com.osp.ucenter.common.model.ResponseObject;
import com.osp.ucenter.common.utils.LoggerUtils;
import com.osp.ucenter.jwt.JwtHelper;
import com.osp.ucenter.manager.UserManager;
import com.osp.ucenter.persistence.bo.JWTUserBean;
import com.osp.ucenter.persistence.model.UcUser;
import com.osp.ucenter.service.UcUserService;
import com.osp.ucenter.service.impl.RedisServiceImpl;

/**
 * 用户登录注册
 * 
 * @author zhangmingcheng
 */
@Controller
@Scope(value = "prototype")
@RequestMapping(value = "/user")
public class LoginController {
	@Autowired
	UcUserService ucUserService;

	@Autowired
	private RedisServiceImpl redisServiceImpl;

	@ResponseBody
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public String login(@RequestBody UcUser user) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			String username = user.getUserName();
			String password = user.getUserPwd();
			//String username = "zmcheng";
			//String password = "123456";
			Subject currentUser = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, UserManager.md5Pswd(username, password));
			currentUser.login(token);
			// 获取 菜单
			UcUser ucUser = (UcUser) currentUser.getPrincipal();
			if (ucUser != null) {
				JWTUserBean jwtUserBean = organizeJWTUserBean(ucUser);
				ro.setToken(jwtUserBean.getJwtToken());
				ro.setOspState(200);
				ro.setValue("ucUser", ucUser);
			}
			return JsonUtil.beanToJson(ro);
		} catch (DisabledAccountException e) {
			ro.setOspState(403);
			ro.setValue("msg", "帐号已经禁止登录！");
			return JsonUtil.beanToJson(ro);
		} catch (AccountException e) {
			ro.setOspState(401);
			ro.setValue("msg", "帐号或密码不正确！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			ro.setValue("msg", "服务器异常，登陆失败！");
			e.printStackTrace();
			return JsonUtil.beanToJson(ro);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/register")
	public String register(@RequestBody UcUser user) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			UcUser tempUser = ucUserService.findUser(user.getUserId());
			if (null != tempUser) {
				ro.setValue("msg", "此帐号已经存在,注册失败！");
				ro.setOspState(500);
				return JsonUtil.beanToJson(ro);
			}
			user = UserManager.md5Pswd(user);
			int count = ucUserService.insert(user);
			if (count > 0) {
				LoggerUtils.fmtDebug(getClass(), "注册插入完毕！", user.toString());
				Subject currentUser = SecurityUtils.getSubject();
				UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getUserPwd());
				currentUser.login(token);
				// currentUser.hasRole("admin");
				// 获取 菜单
				UcUser ucUser = (UcUser) currentUser.getPrincipal();
				if (ucUser != null) {
					JWTUserBean jwtUserBean = organizeJWTUserBean(ucUser);
					ro.setOspState(200);
					ro.setToken(jwtUserBean.getJwtToken());
					ro.setValue("ucUser", ucUser);
				}
			} else {
				ro.setOspState(500);
				ro.setValue("msg", "服务器异常,注册失败！");
			}
			return JsonUtil.beanToJson(ro);
		} catch (DisabledAccountException e) {
			ro.setOspState(403);
			ro.setValue("msg", "帐号已经禁止登录！");
			return JsonUtil.beanToJson(ro);
		} catch (AccountException e) {
			ro.setOspState(401);
			ro.setValue("msg", "帐号或密码不正确！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			ro.setValue("msg", "服务器异常，注册失败！");
			e.printStackTrace();
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 组织JWTUserBean，并将JWTUserBean写入到redis里面
	 * 
	 * @param ucUser
	 * @return
	 */
	public JWTUserBean organizeJWTUserBean(UcUser ucUser) throws Exception {
		// 过期时间 1小时
		long expire = 3600;
		// 拼装accessToken MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=
		String accessToken = JwtHelper.createJWT(ucUser.getUserName(), expire * 1000);
		JWTUserBean jwtUserBean = new JWTUserBean(ucUser);
		jwtUserBean.setCreateJWTTime(ucUser.getLastLoginTime());
		jwtUserBean.setJwtToken(accessToken);
		redisServiceImpl.put(accessToken, jwtUserBean, expire);
		return jwtUserBean;
	}

	@ResponseBody
	@RequestMapping(value = "/auth", method = { RequestMethod.GET, RequestMethod.POST })
	public String noAuthor() {
		ResponseObject ro = ResponseObject.getInstance();
		ro.setOspState(403);
		System.out.println("====================没有访问该资源的权限==================");
		ro.setValue("msg", "对不起，您没有权限，请联系管理员！");
		return JsonUtil.beanToJson(ro);
	}
	

	@ResponseBody
	@RequestMapping(value = "/toLogin", method = { RequestMethod.GET, RequestMethod.POST })
	public String toLogin() {
		ResponseObject ro = ResponseObject.getInstance();
		ro.setOspState(401);
		System.out.println("=========================没登录====================");
		ro.setValue("msg", "没有登录，请先登录！");
		return JsonUtil.beanToJson(ro);
	}
	
}

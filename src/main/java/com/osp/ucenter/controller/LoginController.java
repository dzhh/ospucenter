package com.osp.ucenter.controller;

import java.util.HashMap;
import java.util.Map;

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
import com.osp.ucenter.common.exception.MyRuntimeException;
import com.osp.ucenter.common.model.ResponseObject;
import com.osp.ucenter.common.utils.BaseUtils;
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
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			String username = user.getUserName();
			String password = user.getUserPwd();
			Subject currentUser = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, UserManager.md5Pswd(username, password));
			currentUser.login(token);
			// currentUser.hasRole("admin");
			// 获取 菜单
			UcUser ucUser = (UcUser) currentUser.getPrincipal();
			if (ucUser != null) {
				JWTUserBean jwtUserBean = organizeJWTUserBean(ucUser);
				ro.setToken(jwtUserBean.getJwtToken());
				ro.setOspState(200);
				data.put("ucUser", ucUser);
				ro.setData(data);
			}
			return JsonUtil.beanToJson(ro);
		} catch (DisabledAccountException e) {
			ro.setOspState(500);
			return JsonUtil.beanToJson(ro);
		} catch (AccountException e) {
			ro.setOspState(500);
			e.printStackTrace();
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			e.printStackTrace();
			return JsonUtil.beanToJson(ro);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/register")
	public String register(@RequestBody UcUser user) {
		ResponseObject ro = ResponseObject.getInstance();
		HashMap<String, Object> data = new HashMap<String, Object>();
		try {
			UcUser tempUser = ucUserService.findUser(user.getUserId());
			if (null != tempUser) {
				data.put("ucUser", "帐号已经存在！");
				ro.setOspState(500);
				ro.setData(data);
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
					data.put("ucUser", ucUser);
					ro.setData(data);
				}
			} else {
				ro.setOspState(500);
			}
			return JsonUtil.beanToJson(ro);
		} catch (DisabledAccountException e) {
			ro.setOspState(500);
			return JsonUtil.beanToJson(ro);
		} catch (AccountException e) {
			ro.setOspState(500);
			e.printStackTrace();
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
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
		jwtUserBean.setCreateTime(BaseUtils.getCurrentTime());
		jwtUserBean.setJwtToken(accessToken);
		redisServiceImpl.put(accessToken, jwtUserBean, expire);
		return jwtUserBean;
	}

	@ResponseBody
	@RequestMapping(value = "/auth", method = { RequestMethod.GET, RequestMethod.POST })
	public String noauth() {
		ResponseObject ro = ResponseObject.getInstance();
		ro.setOspState(402);
		return JsonUtil.beanToJson(ro);
	}
}

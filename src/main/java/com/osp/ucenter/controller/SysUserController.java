package com.osp.ucenter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.osp.ucenter.jwt.TokenAuth;
import com.osp.ucenter.manager.UserManager;
import com.osp.ucenter.mybatis.page.Pagination;
import com.osp.ucenter.persistence.bo.JWTUserBean;
import com.osp.ucenter.persistence.model.UcUser;
import com.osp.ucenter.service.UcUserService;
import com.osp.ucenter.service.impl.RedisServiceImpl;

/**
 * 用户管理
 * 
 * @author zhangmingcheng
 */
@Controller
@Scope(value = "prototype")
@RequestMapping("/user")
public class SysUserController {
	@Autowired
	UcUserService ucUserService;

	@Autowired
	private RedisServiceImpl redisServiceImpl;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 禁止登陆 前台需要传递参数 Pagination.ids
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/forbidUserById", method = { RequestMethod.GET, RequestMethod.POST })
	public String forbidUserById(@RequestBody Pagination<UcUser> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			Map<String, Object> data = ucUserService.updateForbidUserById(Integer.parseInt(pagination.getIds()), 0);
			int status = (Integer) data.get("status");
			ro.setOspState(status);
			if (status != 200) {
				ro.setValue("msg", "服务器错误,禁止用户登陆失败！");
			}
			this.getUserLists(pagination, ro);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 激活登陆 前台需要传递参数 Pagination.ids
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/activeUserById", method = { RequestMethod.GET, RequestMethod.POST })
	public String activeUserById(@RequestBody Pagination<UcUser> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			Map<String, Object> data = ucUserService.updateForbidUserById(Integer.parseInt(pagination.getIds()), 1);
			int status = (Integer) data.get("status");
			ro.setOspState(status);
			if (status != 200) {
				ro.setValue("msg", "服务器异常,激活用户登陆失败！");
			}
			this.getUserLists(pagination, ro);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setValue("msg", "服务器异常！");
			ro.setOspState(500);
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 用户列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userLists", method = { RequestMethod.GET, RequestMethod.POST })
	public String userLists(@RequestBody Pagination<UcUser> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			this.getUserLists(pagination, ro);
			ro.setOspState(200);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器出现异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			ro.setValue("msg", "服务器异常，获取用户列表失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 删除用户，根据ID，删除用户前必须清空用户角色
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deleteUser", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String deleteRoleById(@RequestBody Pagination<UcUser> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			Map<String, Object> data = ucUserService.deleteUserById(pagination.getIds());
			ro.setValue("msg", data.get("ucUserStatus"));
			this.getUserLists(pagination, ro);
			ro.setOspState(200);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 在线用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/onlineUsers", method = { RequestMethod.GET, RequestMethod.POST })
	public String onlineUsers(@RequestBody Pagination<JWTUserBean> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			pagination.setTotalCount((int) redisServiceImpl.count());
			int start = (pagination.getPageNo() - 1) * pagination.getPageSize();
			int end = start + pagination.getPageSize();
			int i = 0;
			List<JWTUserBean> lists = new ArrayList<>();
			for (String jwtToken : redisServiceImpl.getKeys()) {
				if (i >= start && i < end) {
					Object jwtUser = redisServiceImpl.get(jwtToken);
					JWTUserBean jwtUserBean = JsonUtil.jsonToBean(JsonUtil.beanToJson(jwtUser), JWTUserBean.class);
					// 暂存jwtToken信息，不存签名部分
					// jwtUserBean.setJwtToken(jwtToken.substring(0,
					// jwtToken.lastIndexOf('.')));
					lists.add(jwtUserBean);
					i++;
				} else {
					break;
				}
			}
			pagination.setList(lists);
			ro.setValue("ucUser", pagination.getList());
			ro.setOspState(200);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器出现异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			ro.setValue("msg", "服务器出现异常,获取在线用户列表失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateUserInfo(@RequestBody UcUser user) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			int status = ucUserService.updateByPrimaryKeySelective(user);
			if (status > 0) {
				UcUser ucUser = ucUserService.findUser(user.getUserId());
				String jwtToken = request.getHeader("token");
				if (jwtToken != null && !jwtToken.equals("")) {
					redisServiceImpl.put(jwtToken, new JWTUserBean(ucUser), 3600);// 维护用户信息
				}
				ro.setValue("ucUser", ucUser);
			}
			ro.setOspState(200);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器出现异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器出现异常，修改用户信息失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 修改用户密码:前台需要传递参数 userId username userPsw userNewPsw
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserPsw", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateUserPsw(@RequestBody UcUser user) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			UcUser dbUser = ucUserService.findUser(user.getUserId());
			user = UserManager.md5Pswd(user);
			if (user.getUserPwd().equals(dbUser.getUserPwd()) == false) {
				ro.setOspState(500);
				ro.setValue("msg", "原密码不对");
				return JsonUtil.beanToJson(ro);
			}
			user.setUserPwd(UserManager.md5Pswd(user.getUserName(), user.getNewPwd()));
			int status = ucUserService.updateByPrimaryKeySelective(user);
			if (status > 0) {
				UcUser ucUser = ucUserService.findUser(user.getUserId());
				String jwtToken = request.getHeader("token");
				if (jwtToken != null && !jwtToken.equals("")) {
					redisServiceImpl.put(jwtToken, new JWTUserBean(ucUser), 3600);// 维护用户信息
				}
			}
			ro.setOspState(200);
			UcUser ucUser = TokenAuth.getUser(user.getUserId());
			ro.setValue("ucUser", ucUser);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器出现异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器出现异常，修改用户密码失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 用户信息
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String userInfo() {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			String jwtToken = request.getHeader("token");
			if (jwtToken != null && !jwtToken.equals("")) {
				Object jwtUser = redisServiceImpl.get(request.getHeader("token"));
				JWTUserBean ucUser = JsonUtil.jsonToBean(JsonUtil.beanToJson(jwtUser), JWTUserBean.class);
				ro.setValue("ucUser", ucUser);
				ro.setOspState(200);
			} else {
				ro.setOspState(500);
				ro.setValue("msg", "获取用户信息失败！");
			}
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器出现异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			ro.setValue("msg", "服务器出现异常，获取用户信息失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 踢出在线用户 前台需传递参数Pagination.ospToken
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/shotOffOnlineUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String shotOffOnlineUser(@RequestBody Pagination<JWTUserBean> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			String osptoken = pagination.getOspToken();
			if (osptoken != null && !osptoken.equals("") && redisServiceImpl.isKeyExists(osptoken) == true) {
				ro.setOspState(200);
				redisServiceImpl.remove(osptoken);
			} else {
				ro.setOspState(500);
				ro.setValue("msg", "前台传入的jwtToken有误或jwtToken已过期，踢出失败");
			}
			pagination.setTotalCount((int) redisServiceImpl.count());
			int start = (pagination.getPageNo() - 1) * pagination.getPageSize();
			int end = start + pagination.getPageSize();
			int i = 0;
			List<JWTUserBean> lists = new ArrayList<>();
			for (String jwtToken : redisServiceImpl.getKeys()) {
				if (i >= start && i < end) {
					Object jwtUser = redisServiceImpl.get(jwtToken);
					JWTUserBean jwtUserBean = JsonUtil.jsonToBean(JsonUtil.beanToJson(jwtUser), JWTUserBean.class);
					lists.add(jwtUserBean);
					i++;
				} else {
					break;
				}
			}
			pagination.setList(lists);
			ro.setValue("ucUser", pagination.getList());
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器出现异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 取得当前页用户
	 * 
	 * @param pagination
	 * @return
	 */
	public void getUserLists(Pagination<UcUser> pagination, ResponseObject ro) {
		Map<String, Object> findContent = new HashMap<String, Object>();
		try {
			findContent.put("findContent", pagination.getFindContent());
			Pagination<UcUser> ucUsers = ucUserService.findPage(findContent, pagination.getPageNo(),
					pagination.getPageSize());
			for (UcUser tempUcUser : ucUsers.getList()) {
				tempUcUser.setUserPwd("");
			}
			ro.setValue("ucUser", ucUsers.getList());
		} catch (Exception e) {
			throw e;
		}
	}
}

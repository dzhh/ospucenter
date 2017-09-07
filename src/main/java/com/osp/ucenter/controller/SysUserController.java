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
	 * 踢出在线用户 前台需传递参数Pagination.ids 
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/shotOffOnlineUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String shotOffOnlineUser(@RequestBody Pagination<JWTUserBean> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			UcUser ucUser = ucUserService.selectByPrimaryKey(Integer.parseInt(pagination.getIds()));
			ucUserService.shotOffOnlineUser(ucUser);
			pagination.setTotalCount((int)redisServiceImpl.count());
			int start = (pagination.getPageNo() - 1) * pagination.getPageSize();
			int end = start + pagination.getPageSize();
			int i = 0;
			List<JWTUserBean> lists = new ArrayList<>();
			for (String jwtToken : redisServiceImpl.getKeys()) {
				if (i >= start && i < end) {
					Object jwtUser= redisServiceImpl.get(jwtToken);
					JWTUserBean jwtUserBean = JsonUtil.jsonToBean(JsonUtil.beanToJson(jwtUser), JWTUserBean.class);
					// 暂存jwtToken信息，不存签名部分
					jwtUserBean.setJwtToken(jwtToken.substring(0, jwtToken.lastIndexOf('.')));
					lists.add(jwtUserBean);
				    i++;
				}else{
					break;
				}
			}
			pagination.setList(lists);
			data.put("ucUser", pagination.getList());
			ro.setOspState(200);
			ro.setData(data);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			e.printStackTrace();
			return JsonUtil.beanToJson(ro);
		}
	}
	
	
	/**
	 * 禁止登陆 前台需要传递参数  Pagination.ids 
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/forbidUserById", method = { RequestMethod.GET, RequestMethod.POST })
	public String forbidUserById(@RequestBody Pagination<UcUser> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			Map<String, Object> data = ucUserService.updateForbidUserById(Integer.parseInt(pagination.getIds()),0);
			ro.setOspState((Integer) data.get("status"));
			Pagination<UcUser> ucUsers = ucUserService.findPage(new HashMap<String, Object>(), pagination.getPageNo(),
					pagination.getPageSize());
			for (UcUser tempUcUser : ucUsers.getList()) {
				tempUcUser.setUserPwd("");
			}
			data.put("ucUser", ucUsers.getList());
			ro.setData(data);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			return JsonUtil.beanToJson(ro);
		}
	}
	
	/**
	 * 激活登陆 前台需要传递参数  Pagination.ids 
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/forbidUserById", method = { RequestMethod.GET, RequestMethod.POST })
	public String activeUserById(@RequestBody Pagination<UcUser> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			Map<String, Object> data = ucUserService.updateForbidUserById(Integer.parseInt(pagination.getIds()),1);
			ro.setOspState((Integer) data.get("status"));
			Pagination<UcUser> ucUsers = ucUserService.findPage(new HashMap<String, Object>(), pagination.getPageNo(),
					pagination.getPageSize());
			for (UcUser tempUcUser : ucUsers.getList()) {
				tempUcUser.setUserPwd("");
			}
			data.put("ucUser", ucUsers.getList());
			ro.setData(data);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
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
		Map<String, Object> findContent = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			findContent.put("findContent", pagination.getFindContent());
			Pagination<UcUser> ucUsers = ucUserService.findPage(findContent, pagination.getPageNo(),
					pagination.getPageSize());
			for (UcUser tempUcUser : ucUsers.getList()) {
				tempUcUser.setUserPwd("");
			}
			data.put("ucUser", ucUsers.getList());
			ro.setOspState(200);
			ro.setData(data);
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
			Pagination<UcUser> ucUsers = ucUserService.findPage(new HashMap<String, Object>(), pagination.getPageNo(),
					pagination.getPageSize());
			for (UcUser tempUcUser : ucUsers.getList()) {
				tempUcUser.setUserPwd("");
			}
			data.put("ucUser", ucUsers.getList());
			ro.setOspState(200);
			ro.setData(data);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
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
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			pagination.setTotalCount((int)redisServiceImpl.count());
			int start = (pagination.getPageNo() - 1) * pagination.getPageSize();
			int end = start + pagination.getPageSize();
			int i = 0;
			List<JWTUserBean> lists = new ArrayList<>();
			for (String jwtToken : redisServiceImpl.getKeys()) {
				if (i >= start && i < end) {
					Object jwtUser= redisServiceImpl.get(jwtToken);
					JWTUserBean jwtUserBean = JsonUtil.jsonToBean(JsonUtil.beanToJson(jwtUser), JWTUserBean.class);
					// 暂存jwtToken信息，不存签名部分
					jwtUserBean.setJwtToken(jwtToken.substring(0, jwtToken.lastIndexOf('.')));
					lists.add(jwtUserBean);
				    i++;
				}else{
					break;
				}
			}
			pagination.setList(lists);
			data.put("ucUser", pagination.getList());
			ro.setOspState(200);
			ro.setData(data);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			e.printStackTrace();
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
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			int status = ucUserService.updateByPrimaryKeySelective(user);
			if (status > 0) {
				UcUser ucUser = ucUserService.findUser(user.getUserId());
				redisServiceImpl.put(request.getHeader("token"), new JWTUserBean(ucUser), 3600);//维护用户信息
				data.put("ucUser", ucUser);
			}
			ro.setOspState(200);
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
	 * 修改用户密码:前台需要传递参数 userId username userPsw userNewPsw
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserPsw", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateUserPsw(@RequestBody UcUser user) {
		ResponseObject ro = ResponseObject.getInstance();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			UcUser dbUser = ucUserService.findUser(user.getUserId());
			user = UserManager.md5Pswd(user);
			if (user.getUserPwd().equals(dbUser.getUserPwd()) == false) {
				ro.setOspState(500);
				data.put("ucUser", "原密码不对！！！");
				return JsonUtil.beanToJson(ro);
			}
			user.setUserPwd(UserManager.md5Pswd(user.getUserName(), user.getNewPwd()));
			int status = ucUserService.updateByPrimaryKeySelective(user);
			if (status > 0) {
				UcUser ucUser = ucUserService.findUser(user.getUserId());
				redisServiceImpl.put(request.getHeader("token"), new JWTUserBean(ucUser), 3600);//维护用户信息
			}
			ro.setOspState(200);
			UcUser ucUser = TokenAuth.getUser(user.getUserId());
			data.put("ucUser", ucUser);
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
	 * 用户信息
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String userInfo() {
		ResponseObject ro = ResponseObject.getInstance();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			Object jwtUser= redisServiceImpl.get(request.getHeader("token"));
			JWTUserBean ucUser = JsonUtil.jsonToBean(JsonUtil.beanToJson(jwtUser), JWTUserBean.class);
			data.put("ucUser", ucUser);
			ro.setOspState(200);
			ro.setData(data);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(500);
			e.printStackTrace();
			return JsonUtil.beanToJson(ro);
		}
	}
}

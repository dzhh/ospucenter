package com.osp.ucenter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.osp.ucenter.persistence.model.UcUser;
import com.osp.ucenter.service.UcUserService;

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

	/**
	 * 禁止登陆
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/forbidUserById", method = { RequestMethod.GET, RequestMethod.POST })
	public String forbidUserById(String userId) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			Map<String, Object> data = ucUserService.updateForbidUserById(Integer.valueOf(userId));
			ro.setOspState((Integer)data.get("status"));
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
	public String deleteRoleById(String ucUserIds,@RequestBody Pagination<UcUser> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			Map<String, Object> data = ucUserService.deleteUserById(ucUserIds);
			Pagination<UcUser> ucUsers = ucUserService.findPage(new HashMap<String, Object>(), pagination.getPageNo(),
					pagination.getPageSize());
			for (UcUser tempUcUser : ucUsers.getList()) {
				tempUcUser.setUserPwd("");
			}
			data.put("ucUser", ucUsers.getList());
			ro.setOspState(200);
			ro.setData(data);
			return JsonUtil.beanToJson(ro);
		}  catch (Exception e) {
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
	public String onlineUsers(@RequestBody Pagination<UcUser> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			pagination.setTotalCount(TokenAuth.jwtTokens.size());
			int start = (pagination.getPageNo() - 1) * pagination.getPageSize();
			int end = start + pagination.getPageSize();
			int i = 0;
			List<UcUser> lists = new ArrayList<>();
			for (String jwtToken : TokenAuth.jwtTokens.keySet()) {
				if (i >= start && i < end) {
					lists.add(TokenAuth.jwtTokens.get(jwtToken));
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
			ro.setOspState(402);
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
				TokenAuth.updateUser(user.getUserId(), ucUser);// 用户信息修改，维护全局map
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
	 * 修改用户密码
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
				TokenAuth.updateUser(user.getUserId(), ucUser);// 用户信息修改，维护全局map
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
	public String userInfo(@RequestBody UcUser user) {
		ResponseObject ro = ResponseObject.getInstance();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			int userId = user.getUserId();
			UcUser ucUser = TokenAuth.getUser(userId);
			data.put("ucUser", ucUser);
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
}

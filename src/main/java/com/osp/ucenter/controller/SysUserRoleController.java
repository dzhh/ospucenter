package com.osp.ucenter.controller;

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
import com.osp.ucenter.mybatis.page.Pagination;
import com.osp.ucenter.persistence.bo.UcRoleBo;
import com.osp.ucenter.persistence.bo.UserRoleAllocationBo;
import com.osp.ucenter.persistence.model.UcRole;
import com.osp.ucenter.persistence.model.UcUser;
import com.osp.ucenter.service.UcPermissionService;
import com.osp.ucenter.service.UcRoleService;
import com.osp.ucenter.service.UcUserService;

@Controller
@Scope(value = "prototype")
@RequestMapping("/userRole")
public class SysUserRoleController {
	@Autowired
	UcUserService ucUserService;

	@Autowired
	UcRoleService ucRoleService;

	@Autowired
	UcPermissionService ucPermissionService;

	/**
	 * 用户角色关系列表
	 * 
	 * @param modelMap
	 * @param pageNo
	 * @param findContent
	 * @return
	 */
	@RequestMapping(value = "/allocationLists", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String allocationLists(@RequestBody Pagination<UserRoleAllocationBo> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			this.getUserRoleLists(pagination, ro);
			ro.setOspState(200);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，展示用户角色关系列表失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 操作用户的角色
	 * 
	 * @param userId
	 *            用户ID
	 * @param ids
	 *            角色ID，以‘,’间隔
	 * @return
	 */
	@RequestMapping(value = "/addRole2User", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String addRole2User(@RequestBody Pagination<UserRoleAllocationBo> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			Map<String, Object> data = ucUserService.addRole2User(pagination.getId(), pagination.getIds());
			ro.setOspState(200);
			ro.setValue("msg", data.get("ucUserRole"));
			this.getUserRoleLists(pagination, ro);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，操作用户角色失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 根据用户ID查询角色 前台需要传递参数pagination.id pagination.pageNo pagination.pageSize
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/selectRoleByUserId", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String selectRoleByUserId(@RequestBody Pagination<UcRole> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			List<UcRoleBo> ucRoleBos = ucUserService.selectRoleByUserId(pagination.getId());
			String result = "";
			for (UcRoleBo ucRoleBo : ucRoleBos) {
				if (ucRoleBo.isCheck() == true) {
					result += ucRoleBo.getRoleId() + ",";
				}
			}
			if (result.length() > 1) {
				result = result.substring(0, result.length() - 1);
			}
			ro.setOspState(200);
			ro.setValue("defaultValue", result);
			this.getRoles(pagination, ro);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常,查询角色失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 根据用户id清空其所属角色。 用户ID 以','分隔
	 * 
	 * @param userIds
	 * 
	 * @return
	 */
	@RequestMapping(value = "clearRoleByUserIds", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String clearRoleByUserIds(@RequestBody Pagination<UserRoleAllocationBo> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			ro.setOspState(200);
			ucUserService.deleteRoleByUserIds(pagination.getIds());
			this.getUserRoleLists(pagination, ro);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，清空角色失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 根据用户ID取得用户授权的菜单 前台需要传递参数 UcUser.userId
	 * 
	 * @param ucUser
	 * @return
	 */
	@RequestMapping(value = "getMenuTrees", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getMenuTrees(@RequestBody UcUser ucUser) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			ro.setOspState(200);
			ro.setValue("menuTrees", ucRoleService.getMenuTrees(ucUser.getUserId()));
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，取得用户菜单失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 取得当前页用户角色关联关系
	 * 
	 * @param pagination
	 * @return
	 */
	public void getUserRoleLists(Pagination<UserRoleAllocationBo> pagination, ResponseObject ro) {
		Map<String, Object> findContent = new HashMap<String, Object>();
		try {
			findContent.put("findContent", pagination.getFindContent());
			Pagination<UserRoleAllocationBo> boPage = ucUserService.findUserAndRole(findContent, pagination.getPageNo(),
					pagination.getPageSize());
			ro.setValue("ucUserRole", boPage.getList());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 取得角色别表
	 * 
	 * @param pagination
	 * @param ro
	 */
	public void getRoles(Pagination<UcRole> pagination, ResponseObject ro) {
		Map<String, Object> findContent = new HashMap<String, Object>();
		try {
			findContent.put("findContent", pagination.getFindContent());
			Pagination<UcRole> role = ucRoleService.findPage(findContent, pagination.getPageNo(),
					pagination.getPageSize());
			List<UcRole> roles = role.getList();
			ro.setValue("ucRole", roles);
			String result = "";
			for (UcRole ucRole : roles) {
				result += ucRole.getRoleId() + ",";
			}
			if (result.length() > 1) {
				result = result.substring(0, result.length() - 1);
			}
			ro.setValue("allRoleIds", result);
		} catch (Exception e) {
			throw e;
		}
	}
}

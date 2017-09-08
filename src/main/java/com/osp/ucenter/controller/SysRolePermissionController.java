package com.osp.ucenter.controller;

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
import com.osp.ucenter.persistence.bo.CommonRequestBody;
import com.osp.ucenter.persistence.bo.UcPermissionBo;
import com.osp.ucenter.persistence.bo.UcRolePermissionAllocationBo;
import com.osp.ucenter.service.UcPermissionService;
import com.osp.ucenter.service.UcRoleService;

/**
 * 角色权限分配
 * 
 * @author zhangmingcheng
 */
@Controller
@Scope(value = "prototype")
@RequestMapping("/rolePermission")
public class SysRolePermissionController {

	@Autowired
	UcPermissionService ucPermissionService;
	@Autowired
	UcRoleService ucRoleService;

	/**
	 * 权限分配
	 * 
	 * @param modelMap
	 * @param pageNo
	 * @param findContent
	 * @return
	 */
	@RequestMapping(value = "rolePermissionAllocation", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String rolePermissionAllocation() {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			List<UcRolePermissionAllocationBo> ucRolePermissionAllocationBos = ucRoleService
					.selectPermissionByRoleIds();
			ro.setValue("rolePermission", ucRolePermissionAllocationBos);
			ro.setOspState(200);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，权限分配失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 根据角色ID查询权限
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "selectPermissionByRoleId", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String selectPermissionByRoleId(@RequestBody CommonRequestBody commonRequestBody) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			List<UcPermissionBo> permissionBos = ucPermissionService.selectPermissionByRoleId(commonRequestBody.getId());
			ro.setValue("rolePermission", permissionBos);
			ro.setOspState(200);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，查询权限失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 操作角色的权限
	 * 
	 * @param roleId
	 *            角色ID
	 * @param ids
	 *            权限ID，以‘,’间隔
	 * @return
	 */
	@RequestMapping(value = "addPermission2Role", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String addPermission2Role(@RequestBody CommonRequestBody commonRequestBody) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			Map<String, Object> data = ucPermissionService.addPermission2Role(commonRequestBody.getId(),
					commonRequestBody.getIds());
			if (data.get("ucRolePermission").equals("操作成功")) {
				ro.setOspState(200);
			} else {
				ro.setValue("msg", "操作角色的权限失败!");
				ro.setOspState(500);
			}
			this.getUcRolePermissionAllocation(ro);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，操作角色的权限失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 根据角色id清空权限。
	 * 
	 * @param roleIds
	 *            角色ID ，以‘,’间隔
	 * @return
	 */
	@RequestMapping(value = "clearPermissionByRoleIds", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String clearPermissionByRoleIds(String roleIds) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			if (ucPermissionService.deleteByRids(roleIds).get("status").equals("200")) {
				ro.setOspState(200);
			} else {
				ro.setOspState(500);
				ro.setValue("msg", "清空角色权限失败！");
			}
			this.getUcRolePermissionAllocation(ro);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，清空角色权限失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	
	public void getUcRolePermissionAllocation(ResponseObject ro) {
		try {
			List<UcRolePermissionAllocationBo> ucRolePermissionAllocationBos = ucRoleService
					.selectPermissionByRoleIds();
			ro.setValue("rolePermission", ucRolePermissionAllocationBos);
		} catch (Exception e) {
			throw e;
		}
	}
}

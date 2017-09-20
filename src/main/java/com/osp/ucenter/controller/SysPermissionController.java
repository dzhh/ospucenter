package com.osp.ucenter.controller;

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
import com.osp.ucenter.mybatis.page.Pagination;
import com.osp.ucenter.persistence.bo.CommonRequestBody;
import com.osp.ucenter.persistence.bo.JWTUserBean;
import com.osp.ucenter.persistence.bo.UcPermissionMenuActionBo;
import com.osp.ucenter.persistence.model.UcMenu;
import com.osp.ucenter.persistence.model.UcRole;
import com.osp.ucenter.service.UcPermissionService;
import com.osp.ucenter.service.UcRoleService;
import com.osp.ucenter.service.impl.MyPermissionRedisServiceImpl;
import com.osp.ucenter.service.impl.RedisServiceImpl;

/**
 * 权限控制类
 * 
 * @author zhangmingcheng
 */
@Controller
@Scope(value = "prototype")
@RequestMapping("/permission")
public class SysPermissionController {

	@Autowired
	UcPermissionService ucPermissionService;
	
	@Autowired
	private UcRoleService ucRoleService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private RedisServiceImpl redisServiceImpl;
	
	@Autowired
	private MyPermissionRedisServiceImpl myPermissionRedisServiceImpl;

	/**
	 * 菜单列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/menuLists", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String menuLists(@RequestBody Pagination<UcMenu> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			this.getMenus(ro);
			ro.setOspState(200);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，展示菜单列表失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 权限列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/permissionLists", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String permissionLists() {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			this.getUcPermissionMenuAction(ro);
			ro.setOspState(200);
			this.SetMenuAction(ro);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，展示权限列表失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 添加菜单权限
	 * 
	 * @param ucMenu
	 * @return
	 */
	@RequestMapping(value = "/addMenu", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String addMenu(@RequestBody UcMenu ucMenu) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			if (ucPermissionService.insertSelectiveMenu(ucMenu) > 0) {
				ro.setOspState(200);
			} else {
				ro.setOspState(500);
				ro.setValue("msg", "添加菜单失败!");
			}
			this.getMenus(ro);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，添加菜单权限失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 删除菜单权限:(menuIds:id以','分割)，规则同删除权限
	 * 
	 * @param menuIds
	 * @return
	 */
	@RequestMapping(value = "/deleteMenu", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String deleteMenu(@RequestBody CommonRequestBody commonRequestBody) {
		ResponseObject ro = ResponseObject.getInstance();
		Map<String, Object> data = null;
		try {
			data = ucPermissionService.deleteByMenuIds(commonRequestBody.getIds());
			if (data.get("ucRolePermission").equals("操作成功")) {
				ro.setOspState(200);
				ro.setData(data);
			} else {
				ro.setOspState(500);
				ro.setValue("msg", "删除菜单失败！");
			}
			this.getMenus(ro);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，删除菜单权限失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 删除权限，根据ID，但是删除权限的时候，需要查询是否有赋予给角色，如果有角色在使用，那么就不能删除。注意同时维护操作表，菜单表。
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deletePermission", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String deletePermissionById(@RequestBody CommonRequestBody commonRequestBody) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			ro.setData(ucPermissionService.deletePermissionById(commonRequestBody.getIds()));
			if (ro.getData().get("ucRolePermission").equals("操作成功")) {
				ro.setOspState(200);
			} else {
				ro.setOspState(500);
				ro.setValue("msg", ro.getData().get("ucRolePermission"));
			}
			this.updateRedis();
			this.getUcPermissionMenuAction(ro);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			ro.setValue("msg", "服务器异常！");
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			ro.setValue("msg", "服务器异常，删除权限失败！");
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 权限列表
	 * @param ro
	 */
	public void getUcPermissionMenuAction(ResponseObject ro) {
		try {
			List<UcPermissionMenuActionBo> ucPermissionMenuActionBos = ucPermissionService.selectPermissions();
			for(UcPermissionMenuActionBo ucPermissionMenuActionBo:ucPermissionMenuActionBos){
				ucPermissionMenuActionBo.setKey(ucPermissionMenuActionBo.getPermissionId());
				if(ucPermissionMenuActionBo.getMenuId()!=null){
					ucPermissionMenuActionBo.setLabel(ucPermissionMenuActionBo.getMenuName());
				}else if(ucPermissionMenuActionBo.getActionId()!=null){
					ucPermissionMenuActionBo.setLabel(ucPermissionMenuActionBo.getActionName());
				}
			}
			ro.setValue("permissionLists", ucPermissionMenuActionBos);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * @param ro
	 */
	public void getMenus(ResponseObject ro) {
		try {
			List<UcMenu> ucMenus = ucPermissionService.selectMenus();
			ro.setValue("menuLists", ucMenus);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 维护redis我的权限
	 */
	public void  updateRedis(){
		String osptoken = request.getHeader("token");
		Object jwtUser = redisServiceImpl.get(osptoken);
		JWTUserBean jwtUserBean = JsonUtil.jsonToBean(JsonUtil.beanToJson(jwtUser), JWTUserBean.class);
		List<UcRole> ucRoles = ucRoleService.findAllPermissionByUser(jwtUserBean.getUserId());
		myPermissionRedisServiceImpl.put(jwtUserBean.getUserId().toString(), ucRoles, -1);
	}
	
	/**
	 * 取得此用户当前菜单的操作权限树
	 * @param ro
	 */
	public void SetMenuAction(ResponseObject ro){
		ro.setValue("menuActions", request.getAttribute("menuActions"));
	}

}

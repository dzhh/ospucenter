package com.osp.ucenter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.osp.ucenter.common.exception.MyRuntimeException;
import com.osp.ucenter.common.model.ResponseObject;
import com.osp.ucenter.common.utils.JsonUtil;
import com.osp.ucenter.common.utils.LoggerUtils;
import com.osp.ucenter.mybatis.page.Pagination;
import com.osp.ucenter.persistence.model.UcRole;
import com.osp.ucenter.service.UcRoleService;

/**
 * 用户角色管理
 * 
 * @author zhangmingcheng
 */
@Controller
@Scope(value = "prototype")
@RequestMapping(value = "/role")
public class SysRoleController {

	@Autowired
	UcRoleService ucRoleService;

	/**
	 * 角色列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/roleLists")
	@ResponseBody
	public String roleLists(@RequestBody Pagination<UcRole> pagination) {
		ResponseObject ro = ResponseObject.getInstance();
		Map<String, Object> findContent = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			findContent.put("findContent", pagination.getFindContent());
			Pagination<UcRole> role = ucRoleService.findPage(findContent, pagination.getPageNo(), pagination.getPageSize());
			data.put("ucRole", role.getList());
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
	 * 取得所有角色
	 * 
	 * @return
	 */
	public ArrayList<UcRole> getAllRoles() {
		ArrayList<UcRole> ucRoles = null;
		try {
			ucRoles = ucRoleService.selectAllRoles();
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "获取角色列表错误。source[%s]", ucRoles.toString());
		}
		return ucRoles;
	}

	/**
	 * 角色添加 @RequestMapping(value = "addRole", method = RequestMethod.POST)
	 * 规则：角色名和系统编号不能都相同
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/addRole")
	@ResponseBody
	public String addRole(@RequestBody UcRole ucRole) {
		ResponseObject ro = ResponseObject.getInstance();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			ArrayList<UcRole> ucRoles = this.getAllRoles();
			for (UcRole tempUcRole : ucRoles) {
				if (tempUcRole.getRoleName().trim().equals(ucRole.getRoleName().trim())) {
					ro.setOspState(500);
					data.put("UcRole", "此角色名称已存在！");
					return JsonUtil.beanToJson(ro);
				}
			}
			ucRoleService.insertSelective(ucRole);
			ro.setOspState(200);
			data.put("UcRole", this.getAllRoles());
			ro.setData(data);
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			LoggerUtils.fmtError(getClass(), e, "添加角色报错。source[%s]", ucRole.toString());
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			LoggerUtils.fmtError(getClass(), e, "添加角色报错。source[%s]", ucRole.toString());
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 删除角色，根据ID，系统管理员不能删除。
	 * 
	 * @RequestMapping(value="/deleteRole",method=RequestMethod.POST) @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteRole")
	@ResponseBody
	public String deleteRoleById(@RequestBody String ucRoleIds) {
		ResponseObject ro = ResponseObject.getInstance();
		try {
			ro.setOspState(200);
			ro.setData(ucRoleService.deleteRoleById(ucRoleIds));
			return JsonUtil.beanToJson(ro);
		} catch (MyRuntimeException e) {
			ro.setOspState(400);
			return JsonUtil.beanToJson(ro);
		} catch (Exception e) {
			ro.setOspState(402);
			return JsonUtil.beanToJson(ro);
		}
	}

	/**
	 * 我的权限
	 * 
	 * @return
	 */
	// @RequestMapping(value="getPermissionTree",method=RequestMethod.POST)
	// @ResponseBody
	// public List<Map<String, Object>> getPermissionTree(){
	// 查询我所有的角色 ---> 权限
	// List<UcRole> roles = ucRoleService.findNowAllPermission();
	// 把查询出来的roles 转换成bootstarp 的 tree数据
	// List<Map<String, Object>> data = UserManager.toTreeData(roles);
	// return data;
	// }
}

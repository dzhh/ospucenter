package com.osp.ucenter.persistence.bo;

import java.io.Serializable;

/**
 * 权限分配 查询列表BO
 * 
 * @author zhangmingcheng
 *
 */
public class UcRolePermissionAllocationBo implements Serializable {
	private static final long serialVersionUID = 1L;
	// 角色ID
	private Integer roleId;
	// 角色Name
	private String roleName;
	// 菜单权限Name列转行，以,分割
	private String menuNames;
	// 操作权限Name列转行，以,分割
	private String actionNames;
	// 权限Id列转行，以‘,’分割
	private String permissionIds;
	
	private Integer key;//前台需要

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getMenuNames() {
		return menuNames;
	}

	public void setMenuNames(String menuNames) {
		this.menuNames = menuNames;
	}

	public String getActionNames() {
		return actionNames;
	}

	public void setActionNames(String actionNames) {
		this.actionNames = actionNames;
	}

	public String getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(String permissionIds) {
		this.permissionIds = permissionIds;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}
}

package com.osp.ucenter.persistence.model;

import java.io.Serializable;

public class UcPermission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer permissionId;

	private String permissionType;

	private Integer menuId;

	private Integer actionId;
	
	public UcPermission() {
		super();
	}

	public UcPermission(String permissionType, Integer menuId, Integer actionId) {
		this.permissionType = permissionType;
		this.menuId = menuId;
		this.actionId = actionId;
	}

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType == null ? null : permissionType.trim();
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}
}
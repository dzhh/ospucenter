package com.osp.ucenter.persistence.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.osp.ucenter.persistence.bo.UcPermissionMenuActionBo;

public class UcRole implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer roleId;
	private String roleName;
	private String systemcode;
	//role --> permission 一对多处理
	private List<UcPermissionMenuActionBo> permissions = new LinkedList<UcPermissionMenuActionBo>();

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
		this.roleName = roleName == null ? null : roleName.trim();
	}

	public String getSystemcode() {
		return systemcode;
	}

	public void setSystemcode(String systemcode) {
		this.systemcode = systemcode == null ? null : systemcode.trim();
	}

	public List<UcPermissionMenuActionBo> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<UcPermissionMenuActionBo> permissions) {
		this.permissions = permissions;
	}
}
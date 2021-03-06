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
	private String value;// 前台需要
	private String label;// 前台需要
	private Integer key;//前台需要
	private List<UcPermissionMenuActionBo> permissions = new LinkedList<UcPermissionMenuActionBo>();// role --> permission 一对多处理

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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}
}
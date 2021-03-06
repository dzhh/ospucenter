package com.osp.ucenter.persistence.bo;

import java.io.Serializable;

import com.osp.ucenter.common.utils.StringUtils;

/**
 * 角色权限分配
 * @author zhangmingcheng
 */
public class UcPermissionBo extends UcPermissionMenuActionBo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 是否勾选
	 */
	private String marker;
	/**
	 * role Id
	 */
	private String roleId;

	public boolean isCheck(){
		return StringUtils.equals(roleId,marker);
	}
	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
}

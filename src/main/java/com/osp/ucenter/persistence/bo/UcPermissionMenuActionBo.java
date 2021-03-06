package com.osp.ucenter.persistence.bo;

import java.io.Serializable;

/**
 * 权限表数据展示
 * 
 * @author zhangmingcheng
 */
public class UcPermissionMenuActionBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer PermissionId;
	private String permissionType;
	private Integer menuId;
	private Integer actionId;
	private String menuName;
	private String menuUrl;
	private String menuIcon;
	private Integer menuParent;
	private String actionName;
	private String actionCode;
	private Integer actionParent;
	private String actionPreventUrl;
	private String value;// 前台需要
	private String label;// 前台需要
	private Integer key;

	public Integer getPermissionId() {
		return PermissionId;
	}

	public void setPermissionId(Integer permissionId) {
		PermissionId = permissionId;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
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

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public Integer getMenuParent() {
		return menuParent;
	}

	public void setMenuParent(Integer menuParent) {
		this.menuParent = menuParent;
	}

	public Integer getActionParent() {
		return actionParent;
	}

	public void setActionParent(Integer actionParent) {
		this.actionParent = actionParent;
	}

	public String getActionPreventUrl() {
		return actionPreventUrl;
	}

	public void setActionPreventUrl(String actionPreventUrl) {
		this.actionPreventUrl = actionPreventUrl;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

}

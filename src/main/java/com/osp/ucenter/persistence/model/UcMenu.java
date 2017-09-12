package com.osp.ucenter.persistence.model;

import java.io.Serializable;

public class UcMenu implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer menuId;
	private String menuName;
	private String menuUrl;
	private Integer menuParent;
	private String systemcode;
	private String menuIcon;

	public UcMenu() {
		super();
	}

	public UcMenu(Integer menuId, String menuName, String menuUrl, Integer menuParent, String menuIcon) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.menuUrl = menuUrl;
		this.menuParent = menuParent;
		this.menuIcon = menuIcon;
	}

	public UcMenu(Integer menuId, String menuName, String menuUrl, Integer menuParent, String menuIcon,
			String systemcode) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.menuUrl = menuUrl;
		this.menuParent = menuParent;
		this.menuIcon = menuIcon;
		this.systemcode = systemcode;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName == null ? null : menuName.trim();
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl == null ? null : menuUrl.trim();
	}

	public Integer getMenuParent() {
		return menuParent;
	}

	public void setMenuParent(Integer menuParent) {
		this.menuParent = menuParent;
	}

	public String getSystemcode() {
		return systemcode;
	}

	public void setSystemcode(String systemcode) {
		this.systemcode = systemcode == null ? null : systemcode.trim();
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon == null ? null : menuIcon.trim();
	}
}
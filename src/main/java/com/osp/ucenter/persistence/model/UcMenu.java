package com.osp.ucenter.persistence.model;

public class UcMenu {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column uc_menu.menu_id
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	private Integer menuId;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column uc_menu.menu_name
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	private String menuName;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column uc_menu.menu_url
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	private String menuUrl;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column uc_menu.menu_parent
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	private Integer menuParent;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column uc_menu.systemcode
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	private String systemcode;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column uc_menu.menu_icon
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	private String menuIcon;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column uc_menu.menu_id
	 * @return  the value of uc_menu.menu_id
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public Integer getMenuId() {
		return menuId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column uc_menu.menu_id
	 * @param menuId  the value for uc_menu.menu_id
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column uc_menu.menu_name
	 * @return  the value of uc_menu.menu_name
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column uc_menu.menu_name
	 * @param menuName  the value for uc_menu.menu_name
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName == null ? null : menuName.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column uc_menu.menu_url
	 * @return  the value of uc_menu.menu_url
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public String getMenuUrl() {
		return menuUrl;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column uc_menu.menu_url
	 * @param menuUrl  the value for uc_menu.menu_url
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl == null ? null : menuUrl.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column uc_menu.menu_parent
	 * @return  the value of uc_menu.menu_parent
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public Integer getMenuParent() {
		return menuParent;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column uc_menu.menu_parent
	 * @param menuParent  the value for uc_menu.menu_parent
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public void setMenuParent(Integer menuParent) {
		this.menuParent = menuParent;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column uc_menu.systemcode
	 * @return  the value of uc_menu.systemcode
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public String getSystemcode() {
		return systemcode;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column uc_menu.systemcode
	 * @param systemcode  the value for uc_menu.systemcode
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public void setSystemcode(String systemcode) {
		this.systemcode = systemcode == null ? null : systemcode.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column uc_menu.menu_icon
	 * @return  the value of uc_menu.menu_icon
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public String getMenuIcon() {
		return menuIcon;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column uc_menu.menu_icon
	 * @param menuIcon  the value for uc_menu.menu_icon
	 * @mbg.generated  Thu Aug 17 11:14:31 CST 2017
	 */
	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon == null ? null : menuIcon.trim();
	}
}
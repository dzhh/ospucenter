package com.osp.ucenter.persistence.model;

public class UcAction {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column uc_action.Action_Id
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    private Integer actionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column uc_action.action_Name
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    private String actionName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column uc_action.action_Code
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    private String actionCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column uc_action.action_parent
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    private Integer actionParent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column uc_action.action_preventUrl
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    private String actionPreventurl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column uc_action.systemcode
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    private String systemcode;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column uc_action.Action_Id
     *
     * @return the value of uc_action.Action_Id
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public Integer getActionId() {
        return actionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column uc_action.Action_Id
     *
     * @param actionId the value for uc_action.Action_Id
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column uc_action.action_Name
     *
     * @return the value of uc_action.action_Name
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column uc_action.action_Name
     *
     * @param actionName the value for uc_action.action_Name
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public void setActionName(String actionName) {
        this.actionName = actionName == null ? null : actionName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column uc_action.action_Code
     *
     * @return the value of uc_action.action_Code
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public String getActionCode() {
        return actionCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column uc_action.action_Code
     *
     * @param actionCode the value for uc_action.action_Code
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode == null ? null : actionCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column uc_action.action_parent
     *
     * @return the value of uc_action.action_parent
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public Integer getActionParent() {
        return actionParent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column uc_action.action_parent
     *
     * @param actionParent the value for uc_action.action_parent
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public void setActionParent(Integer actionParent) {
        this.actionParent = actionParent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column uc_action.action_preventUrl
     *
     * @return the value of uc_action.action_preventUrl
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public String getActionPreventurl() {
        return actionPreventurl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column uc_action.action_preventUrl
     *
     * @param actionPreventurl the value for uc_action.action_preventUrl
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public void setActionPreventurl(String actionPreventurl) {
        this.actionPreventurl = actionPreventurl == null ? null : actionPreventurl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column uc_action.systemcode
     *
     * @return the value of uc_action.systemcode
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public String getSystemcode() {
        return systemcode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column uc_action.systemcode
     *
     * @param systemcode the value for uc_action.systemcode
     *
     * @mbg.generated Wed Aug 09 18:40:44 CST 2017
     */
    public void setSystemcode(String systemcode) {
        this.systemcode = systemcode == null ? null : systemcode.trim();
    }
}
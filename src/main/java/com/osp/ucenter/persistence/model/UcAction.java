package com.osp.ucenter.persistence.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class UcAction implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer actionId;
	private String actionName;
	private String actionCode;
	private Integer actionParent;
	private String actionPreventurl;
	private String systemcode;
	private List<UcAction> children = null;

	public int hashCode() {
		int result = actionId;
		result = result + ((actionName == null) ? 0 : actionName.hashCode());
		return result;
	}

	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		UcAction other = (UcAction) obj;
		if (actionId == other.getActionId()) {
			return true;
		}
		return false;
	}

	public UcAction() {
		super();
	}

	public UcAction(Integer actionId, String actionName, String actionCode, Integer actionParent,
			String actionPreventurl) {
		this.actionId = actionId;
		this.actionName = actionName;
		this.actionCode = actionCode;
		this.actionParent = actionParent;
		this.actionPreventurl = actionPreventurl;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName == null ? null : actionName.trim();
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode == null ? null : actionCode.trim();
	}

	public Integer getActionParent() {
		return actionParent;
	}

	public void setActionParent(Integer actionParent) {
		this.actionParent = actionParent;
	}

	public String getActionPreventurl() {
		return actionPreventurl;
	}

	public void setActionPreventurl(String actionPreventurl) {
		this.actionPreventurl = actionPreventurl == null ? null : actionPreventurl.trim();
	}

	public String getSystemcode() {
		return systemcode;
	}

	public void setSystemcode(String systemcode) {
		this.systemcode = systemcode == null ? null : systemcode.trim();
	}

	public List<UcAction> getChildren() {
		if (this.children == null) {
			this.children = new LinkedList<UcAction>();
		}
		return children;
	}

	public void setChildren(List<UcAction> children) {
		this.children = children;
	}
}
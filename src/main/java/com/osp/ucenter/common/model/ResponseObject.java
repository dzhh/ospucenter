package com.osp.ucenter.common.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回的数据
 * @author fly
 *
 */
public class ResponseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;;
	
	protected ResponseObject() {
		super();
	}
	
	public static ResponseObject getInstance() {
		return new ResponseObject();
	}
	
	/**
	 * 返回状态
	 */
	private int ospState;

	public int getOspState() {
		return ospState;
	}

	public void setOspState(int ospState) {
		this.ospState = ospState;
	}

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	private Map data = new HashMap();

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}

	public void setValue(String key, Object value) {
		data.put(key, value);
	}
	
	public Object getValue(String key, Object value) {
		return data.get(key);
	}
	
	

}

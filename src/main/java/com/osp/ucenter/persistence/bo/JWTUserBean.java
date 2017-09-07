package com.osp.ucenter.persistence.bo;

import com.osp.ucenter.persistence.model.UcUser;

/**
 * 
 * @author zhangmingcheng
 */
public class JWTUserBean extends UcUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jwtToken;
	private String createJWTTime;
	private String lastActionTime;

	public JWTUserBean() {
		super();
	}

	public JWTUserBean(UcUser ucUser) {
		this.setUserId(ucUser.getUserId());
		this.setUserName(ucUser.getUserName());
		this.setUserEmail(ucUser.getUserEmail());
		this.setStatus(ucUser.getStatus());
		this.setLastLoginTime(ucUser.getLastLoginTime());
		this.setSystemcode(ucUser.getSystemcode());
		this.setCreateTime(ucUser.getCreateTime());
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getLastActionTime() {
		return lastActionTime;
	}

	public void setLastActionTime(String lastActionTime) {
		this.lastActionTime = lastActionTime;
	}

	public String getCreateJWTTime() {
		return createJWTTime;
	}

	public void setCreateJWTTime(String createJWTTime) {
		this.createJWTTime = createJWTTime;
	}
}

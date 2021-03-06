package com.osp.ucenter.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.osp.ucenter.common.utils.BaseUtils;
import com.osp.ucenter.persistence.model.UcUser;
import com.osp.ucenter.service.UcUserService;

/**
 * 基础Realm 认证+授权
 * 
 * @author zhangmingcheng
 */
public class OspAuthorizingRealm extends AuthorizingRealm {

	@Autowired
	private UcUserService ucUserService;

	public OspAuthorizingRealm() {
		super();
	}

	/**
	 * 用户登录认证 (登录时暂时还没有使用系统编号这个字段)
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
		String username = (String) usernamePasswordToken.getPrincipal();
		String password = new String((char[]) usernamePasswordToken.getCredentials());
		UcUser user = ucUserService.login(username, password);
		if (null == user) {
			throw new AccountException("帐号或密码不正确！");
		} else if (0 == user.getStatus()) {
			throw new DisabledAccountException("帐号已经禁止登录！");
		} else {
			// 登陆成功 更新最后一次登录时间
			user.setLastLoginTime(BaseUtils.getCurrentTime());
			ucUserService.updateByPrimaryKeySelective(user);		
		}
		return new SimpleAuthenticationInfo(user, user.getUserPwd(), getName());
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(); 
		System.out.println("=====================有没有权限呢");
		authorizationInfo.addRole("admin");  
		return authorizationInfo;
		/*
		 * Integer userId = TokenManager.getUserId(); SimpleAuthorizationInfo
		 * info = new SimpleAuthorizationInfo();
		 * //根据用户ID查询角色（role），放入到Authorization里。 Set<String> roles =
		 * roleService.findRoleByUserId(userId); info.setRoles(roles);
		 * //根据用户ID查询权限（permission），放入到Authorization里。 Set<String> permissions =
		 * permissionService.findPermissionByUserId(userId);
		 * info.setStringPermissions(permissions); return info;
		 */
		
	}

	/**
	 * 清空当前用户权限信息
	 */
	public void clearCachedAuthorizationInfo() {
		PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
		super.clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 指定principalCollection 清除
	 */
	public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
		super.clearCachedAuthorizationInfo(principals);
	}
}

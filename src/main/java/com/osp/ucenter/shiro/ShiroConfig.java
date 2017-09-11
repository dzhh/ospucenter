package com.osp.ucenter.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 等价于beans
public class ShiroConfig {

	private static final Logger log = LoggerFactory.getLogger(ShiroFilterFactoryBean.class);

	/**
	 * 缓存 cacheManager realm
	 * @param ospAuthorizingRealm
	 * @return
	 */
	@Bean(name = "securityManager")
	public SecurityManager securityManager(@Qualifier("ospAuthorizingRealm") OspAuthorizingRealm ospAuthorizingRealm) {
		log.info("securityManager()");
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(ospAuthorizingRealm);
		return securityManager;
	}

	/**
	 * realm
	 * 
	 * @return
	 */
	@Bean(name = "ospAuthorizingRealm")
	public OspAuthorizingRealm ospAuthorizingRealm() {
		OspAuthorizingRealm ospAuthorizingRealm = new OspAuthorizingRealm();
		return ospAuthorizingRealm;
	}

	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件问题。
	 * 注意：单独一个ShiroFilterFactoryBean配置是会报错的，在初始化ShiroFilterFactoryBean的时候需要注入：
	 * SecurityManager。 Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔
	 * 2、当设置多个过滤器时，全部验证通过，才视为通过 3、部分过滤器可指定参数，如perms，roles
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
		log.info("shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		// 必须设置 SecurityManager,说明过滤器使用的安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 拦截器
		Map<String, String> map = new LinkedHashMap<String, String>();
	    

	    //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
	    //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		//从头开始使用第一个匹配的url模式对应的拦截器链
		// map.put("/user/login", "anon");
		// map.put("/**", "authc");
		//map.put("/**", "anon");
		//map.put("/**", "roles[admin]");
         System.out.println("===========================shiro过滤器===========================");
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面  没认证自动跳转到/user/toLogin
		shiroFilterFactoryBean.setLoginUrl("/user/toLogin");
		//shiroFilterFactoryBean.setSuccessUrl("/index"); // 登录成功后要跳转的链接
		shiroFilterFactoryBean.setUnauthorizedUrl("/user/toLogin");// 未认证界面;

		shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

		return shiroFilterFactoryBean;
	}
}
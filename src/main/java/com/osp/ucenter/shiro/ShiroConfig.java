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

	@Bean(name = "securityManager")
	public SecurityManager securityManager(@Qualifier("myAuthorizingRealm") MyAuthorizingRealm myAuthorizingRealm) {
		log.info("securityManager()");
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(myAuthorizingRealm);
		return securityManager;
	}

	/**
	 * realm
	 * 
	 * @return
	 */
	@Bean(name = "myAuthorizingRealm")
	public MyAuthorizingRealm myAuthorizingRealm() {
		MyAuthorizingRealm myAuthorizingRealm = new MyAuthorizingRealm();
		return myAuthorizingRealm;
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

		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 拦截器
		Map<String, String> map = new LinkedHashMap<String, String>();
	    

	    //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
	    //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		//map.put("/**", "authc");
		//map.put("/**", "anon");

		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		shiroFilterFactoryBean.setSuccessUrl("/index"); // 登录成功后要跳转的链接
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");// 未授权界面;

		shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

		return shiroFilterFactoryBean;
	}
}
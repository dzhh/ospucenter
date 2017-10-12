package com.osp.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.osp.ucenter.filter.CrossDomainFilter;
import com.osp.ucenter.filter.Initfilter;
import com.osp.ucenter.filter.SecurityFilter;

/**
 * 2017/08/02
 * 
 * @author fly
 *
 */
@EnableAutoConfiguration
@SpringBootApplication
//@EnableDiscoveryClient
public class UcenterMain {

	
	
	public static void main(String[] args) {
		SpringApplication.run(UcenterMain.class, args);
	}
	
	/**
	 * 跨域处理Filter(最先执行此过滤器，避免跨域请求调用两次而执行后续的过滤器链)
	 * 
	 * @return
	 */
	@Bean
    @Order(Integer.MAX_VALUE)
	public FilterRegistrationBean crossDomainFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new CrossDomainFilter());
		registration.addUrlPatterns("/*");
		registration.setName("CrossDomainFilter");
		return registration;
	}

	/**
	 * 初始化
	 * 
	 * @return
	 */
	@Bean
	@Order(Integer.MAX_VALUE-1)
	public FilterRegistrationBean InitfilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new Initfilter());
		registration.addUrlPatterns("/no");
		registration.setName("Initfilter");
		return registration;
	}

	/**
	 * 安全权限 DelegatingFilterProxy servlet容器与IOC容器关联
	 * 
	 * @return
	 */
	@Bean
	@Order(Integer.MAX_VALUE-2)
	public FilterRegistrationBean securityFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new DelegatingFilterProxy("SecurityFilter"));
		registration.addInitParameter("targetFilterLifecycle", "true");
		registration.addUrlPatterns("/*");
		return registration;
	}
	
	@Bean(name = "SecurityFilter")
	public SecurityFilter getSecurityFilter(){
		return new SecurityFilter();
	}
	 
}

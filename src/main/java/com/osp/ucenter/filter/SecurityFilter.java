package com.osp.ucenter.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.osp.common.json.JsonUtil;
import com.osp.ucenter.common.utils.BaseUtils;
import com.osp.ucenter.persistence.bo.JWTUserBean;
import com.osp.ucenter.service.UcRoleService;
import com.osp.ucenter.service.impl.RedisServiceImpl;

/**
 * 权限控制
 * 
 * @author fly
 *
 */
public class SecurityFilter implements Filter {
	
	@Autowired
	private RedisServiceImpl redisServiceImpl;
	
	@Autowired 
	UcRoleService ucRoleService;

	Logger logger = Logger.getLogger(SecurityFilter.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	public static Map<String, Integer> restApp = new HashMap<String, Integer>();
	
	static {
		restApp.put("/user/login", 1);
		restApp.put("/user/register", 1);
		restApp.put("/user/auth", 1);
		restApp.put("/user/toLogin", 1);
	}
	
	public Integer getRestApiValue(String rest) {
		if(restApp.containsKey(rest)) {
			return restApp.get(rest);
		} else if(rest.endsWith("ico")) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String uri = request.getRequestURI();
		System.out.println("================="+uri+"==================");
		/**
		 * 不需要验证的
		 */
		if(this.getRestApiValue(uri) == 1){
			chain.doFilter(request, response);
			return ;
		}
		// 1. 检查用户是否已登录 Tocken JWT
		String osptoken = request.getHeader("token");
		// 2. 没登录，登录去
		if(osptoken==null||osptoken.equals("")||redisServiceImpl.isKeyExists(osptoken)==false) {
			request.getRequestDispatcher("/user/toLogin").forward(request, response);	
			return ;
		}
		Object jwtUser= redisServiceImpl.get(osptoken);
		JWTUserBean jwtUserBean = JsonUtil.jsonToBean(JsonUtil.beanToJson(jwtUser), JWTUserBean.class);
		jwtUserBean.setLastActionTime(BaseUtils.getCurrentTime());//更新会话最后活动时间
		redisServiceImpl.put(osptoken, jwtUserBean, 3600);

		// 4. 判断用户是否有访问此资源的权限
		Boolean flag = ucRoleService.hasPermission(jwtUserBean.getUserId(), uri);
	    if(flag==false){
	    	request.getRequestDispatcher("/user/auth").forward(request, response);	
			return ;
	    }
		logger.info("=============SecurityFilter dofilter=============");
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("=============SecurityFilter init=============");
	}

}

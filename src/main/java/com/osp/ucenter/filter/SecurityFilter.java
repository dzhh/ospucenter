package com.osp.ucenter.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.osp.common.json.JsonUtil;
import com.osp.ucenter.common.utils.BaseUtils;
import com.osp.ucenter.jwt.JwtUtil;
import com.osp.ucenter.persistence.bo.JWTUserBean;
import com.osp.ucenter.service.UcRoleService;
import com.osp.ucenter.service.impl.RedisServiceImpl;

/**
 * 权限控制
 * 
 * @author fly
 *
 */
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private RedisServiceImpl redisServiceImpl;

	@Autowired
	UcRoleService ucRoleService;

	Logger logger = Logger.getLogger(SecurityFilter.class);

	public static Map<String, Integer> restApp = new HashMap<String, Integer>();

	static {
		restApp.put("/user/login", 1);
		restApp.put("/user/register", 1);
		restApp.put("/user/auth", 1);
		restApp.put("/user/toLogin", 1);
	}

	public Integer getRestApiValue(String rest) {
		if (restApp.containsKey(rest)) {
			return restApp.get(rest);
		} else if (rest.endsWith("ico")) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		String osptoken = request.getHeader("token");
		/**
		 * 不需要验证的
		 */
		if (this.getRestApiValue(uri) == 1) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			// 1. 检查用户是否已登录 Tocken JWT
			JwtUtil.validateToken(osptoken);
		} catch (Exception e) {
			// 2. 没登录，登录去
			request.getRequestDispatcher("/user/toLogin").forward(request, response);
			return;
		}
		// 3.维护在线用户会话信息
		Object jwtUser = redisServiceImpl.get(osptoken);
		JWTUserBean jwtUserBean = JsonUtil.jsonToBean(JsonUtil.beanToJson(jwtUser), JWTUserBean.class);
		jwtUserBean.setLastActionTime(BaseUtils.getCurrentTime());// 更新会话最后活动时间
		redisServiceImpl.put(osptoken, jwtUserBean, 3600);
		// 4. 判断用户是否有访问此资源的权限，并判断此资源属于菜单权限还是操作权限,如果此资源属于菜单权限需要取得此菜单的所有操作权限
		int flag = ucRoleService.hasPermission(jwtUserBean.getUserId(), uri);
		if (flag == 0) {
			request.getRequestDispatcher("/user/auth").forward(request, response);
			return;
		} else if (flag == 1) {
			// 5.如果此url是调用菜单接口 ，取得此菜单的所有操作权限
			request.setAttribute("menuActions", ucRoleService.getActionTrees(jwtUserBean.getUserId(), uri));
		}
		logger.info("=============SecurityFilter dofilter=============");
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}

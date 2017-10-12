package com.osp.ucenter.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public static List<String> restList = new ArrayList<String>();
	static {
		restApp.put("/user/login", 1);
		restApp.put("/user/register", 1);
		restApp.put("/user/auth", 1);
		restApp.put("/user/toLogin", 1);
		restApp.put("/role/deleteRole", 1);
		restApp.put("/role/addRole", 1);
		restApp.put("/role/roleLists", 1);
		restApp.put("/user/userLists", 1);
		restApp.put("/user/onlineUsers", 1);
		restApp.put("/userRole/allocationLists", 1);
		restApp.put("/userRole/addRole2User", 1);
		restApp.put("/userRole/selectRoleByUserId", 1);
		restApp.put("/userRole/clearRoleByUserIds", 1);
		restApp.put("/permission/addMenu", 1);
		restApp.put("/permission/deleteMenu", 1);
		restApp.put("/permission/deletePermission", 1);
		restApp.put("/rolePermission/clearPermissionByRoleIds", 1);
		restApp.put("/index.html", 1);
		restApp.put("/hello", 1);
		restApp.put("/boot/druid/", 1);
		restApp.put("/druid/*", 1);
	}
	
	static {
		restList.add("/druid/*");
	}

	public Integer getRestApiValue(String rest) {
		if (restApp.containsKey(rest)) {
			return restApp.get(rest);
		} else if(rest.endsWith("ico")|| rest.endsWith("js")
				|| rest.endsWith("jpg")) {
			return 1;
		}
		
		for(String regEx : restList) {
			Pattern pattern = Pattern.compile(regEx);
		    Matcher matcher = pattern.matcher(rest);
		    boolean rs = matcher.find();
		    if(rs) {
		    	return 1;
		    }
		}
		
		return 0;
	}
	
	public static void main(String[] args) {
	    // 要验证的字符串
	    String str = "baike.xsoftlab.net";
	    // 正则表达式规则
	    String regEx = "baike.*";
	    // 编译正则表达式
	    Pattern pattern = Pattern.compile(regEx);
	    // 忽略大小写的写法
	    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(str);
	    // 查找字符串中是否有匹配正则表达式的字符/字符串
	    boolean rs = matcher.find();
	    System.out.println(rs);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		String osptoken = request.getHeader("token");
		logger.info(uri);
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

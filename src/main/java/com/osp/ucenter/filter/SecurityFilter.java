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
		if(redisServiceImpl.isKeyExists(osptoken)==false) {
			request.getRequestDispatcher("/user/auth").forward(request, response);
			response.setStatus(402);
			System.out.println("没有权限======================");
			return;
		}
		Object jwtUser= redisServiceImpl.get(request.getHeader("token"));
		JWTUserBean jwtUserBean = JsonUtil.jsonToBean(JsonUtil.beanToJson(jwtUser), JWTUserBean.class);
		jwtUserBean.setLastActionTime(BaseUtils.getCurrentTime());//更新会话最后活动时间
		redisServiceImpl.put(request.getHeader("token"), jwtUserBean, 3600);

		// 3. 得到用户想访问的资源

		// 4. 得到访问该资源需要的权限
		// SecurityService service = new SecurityService();
		// Resource r = service.findResource(uri);
		/*
		 * 你要访问的资源，我在权限管理系统里面没有说访问这个资源需要权限， 也即这个资源不需要被权限系统控制，只有被权限系统控制的资源，在数据库里面
		 * 才有，如果为null，这个资源不受权限系统控制。
		 */
		// if (r == null) {
		// chain.doFilter(request, response);
		// return;
		// }
		// Privilege required_Privilege = r.getPrivilege(); // 得到访问资源需要的权限

		// 5. 判断用户是否有相应权限
		// List<Privilege> list = service.getUserAllPrivilege(user.getId()); //
		// 得到用户所有权限
		// if (!list.contains(required_Privilege)) {
		// // 6. 没有权限，则提示用户权限不足，联系管理员
		// request.setAttribute("message", "对不起，您没有权限，请联系管理员！！！");
		// request.getRequestDispatcher("/message.jsp").forward(request,
		// response);
		// return;
		// }

		// 7. 如果有，则则放行

        //String token = request.getHeader("token");
//        User user = (User) request.getSession().getAttribute("user");

        // 2. 没登录，登录去
//        if (user == null) {
//            request.setAttribute("message", "请先登录！！！");
//            request.getRequestDispatcher("/message.jsp").forward(request, response);
//            return;
//        }

        // 3. 得到用户想访问的资源

        // 4. 得到访问该资源需要的权限
//        SecurityService service = new SecurityService();
//        Resource r = service.findResource(uri);
        /*
         * 你要访问的资源，我在权限管理系统里面没有说访问这个资源需要权限，
         * 也即这个资源不需要被权限系统控制，只有被权限系统控制的资源，在数据库里面
         * 才有，如果为null，这个资源不受权限系统控制。
         */
//        if (r == null) {
//            chain.doFilter(request, response);
//            return;
//        }
//        Privilege required_Privilege = r.getPrivilege(); // 得到访问资源需要的权限

        // 5. 判断用户是否有相应权限
//        List<Privilege> list = service.getUserAllPrivilege(user.getId()); // 得到用户所有权限
//        if (!list.contains(required_Privilege)) {
//            // 6. 没有权限，则提示用户权限不足，联系管理员
//            request.setAttribute("message", "对不起，您没有权限，请联系管理员！！！");
//            request.getRequestDispatcher("/message.jsp").forward(request, response);
//            return;
//        }

        // 7. 如果有，则则放行
       
		logger.info("=============SecurityFilter dofilter=============");

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("=============SecurityFilter init=============");
	}

}

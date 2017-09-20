package com.osp.ucenter.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 处理跨域的filter
 * 
 * @author fly
 *
 */
public class CrossDomainFilter extends OncePerRequestFilter {

	Logger logger = Logger.getLogger(CrossDomainFilter.class);
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			// 跨域
			String origin = request.getHeader("Origin");
			if (origin == null) {
				response.addHeader("Access-Control-Allow-Origin", "*");
			} else {
				response.addHeader("Access-Control-Allow-Origin", origin);
			}
			response.addHeader("Access-Control-Allow-Headers",
					"Origin, x-requested-with, Content-Type, Accept,X-Cookie, token");
			response.addHeader("Access-Control-Allow-Credentials", "true");
			response.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,OPTIONS,DELETE");
			if (request.getMethod().equals("OPTIONS")) {
				response.setStatus(HttpServletResponse.SC_OK);
				return;
			}
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			logger.error("Exception in crossDomainFilter.doFilter", e);
			throw e;
		}
	}

	@Override
	public void destroy() {
	}

}

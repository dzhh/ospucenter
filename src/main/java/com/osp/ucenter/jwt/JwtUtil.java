package com.osp.ucenter.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * jwt工具类
 * 
 * @author zhangmingcheng
 */
@Configuration
public class JwtUtil {

	private static long EXPIRATION_TIME; // 1 hour

	private static String SECRET;// 秘钥
	
	@Value("${jwt.expire_time}")
	public void setEXPIRATION_TIME(long eXPIRATION_TIME) {
		EXPIRATION_TIME = eXPIRATION_TIME;
	}

	@Value("${jwt.secret}")
	public void setSECRET(String sECRET) {
		SECRET = sECRET;
	}

	/**
	 * 生成jwtToken
	 * 
	 * @param username
	 * @return
	 */
	public static String generateToken(String username) {
		HashMap<String, Object> map = new HashMap<>();
		// you can put any data in the map
		map.put("username", username);
		String jwt = Jwts.builder().setClaims(map).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		return jwt;
	}

	/**
	 * 校验jwtToken
	 * 
	 * @param token
	 * @return
	 */
	public static String validateToken(String token) {
		if (token != null) {
			Map<String, Object> body = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
			String username = (String) (body.get("username"));
			if (username == null || username.isEmpty()) {
				throw new TokenValidationException("Wrong token without username");
			} else {
				return username;
			}
		} else {
			throw new TokenValidationException("Missing token");
		}
	}
	
	public static long getEXPIRATION_TIME(){
		return JwtUtil.EXPIRATION_TIME;
	}


	static class TokenValidationException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7946690694369283250L;

		public TokenValidationException(String msg) {
			super(msg);
		}
	}
}

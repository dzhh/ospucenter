package com.osp.ucenter.test.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.osp.ucenter.UcenterMain;
import com.osp.ucenter.manager.UserManager;
import com.osp.ucenter.persistence.model.UcUser;
import com.osp.ucenter.service.UcUserService;
import com.osp.ucenter.service.impl.UcUserServiceImpl;

@Controller
@EnableAutoConfiguration
@SpringBootApplication
public class TestUser {

	public static void main(String[] args) {
		SpringApplication.run(TestUser.class, args);
		
	}
	
	@Autowired
	UcUserService ucUserService;
	
	@ResponseBody
	@RequestMapping(value = "/create")
	public String createUser() {
		UcUser ucUser = new UcUser();
		ucUser.setUserName("asdqwe");
		ucUser.setUserPwd("123456");
		ucUser.setUserEmail("124973@qq.com");
		ucUser = UserManager.md5Pswd(ucUser);
		int re = ucUserService.insert(ucUser);
		System.out.println("============" + re + "==================");
		return re + "";
	}
}

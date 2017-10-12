package com.osp.ucenter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class TestController {

//	@Value("${app}")
	private String app;

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	@RequestMapping("/app-name")
	public String getAppName() {
		return app;
	}

	
    @RequestMapping("/hello")
    public String index(@RequestParam String name) {
        return "hello " + name + "，this is first messge";
    }
}

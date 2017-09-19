package com.osp.ucenter.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.osp.ucenter.persistence.model.UcRole;
import com.osp.ucenter.redis.IRedisService;

@Service
public class MyPermissionRedisServiceImpl extends IRedisService<List<UcRole>>{
	   private static final String REDIS_KEY = "OSPMyPermission";

	    @SuppressWarnings("static-access")
		@Override
	    protected String getRedisKey() {
	        return this.REDIS_KEY;
	    }
}

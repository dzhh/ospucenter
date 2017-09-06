package com.osp.ucenter.service.impl;

import org.springframework.stereotype.Service;

import com.osp.ucenter.persistence.bo.JWTUserBean;
import com.osp.ucenter.redis.IRedisService;

/**
 * 
 * @author zhangmingcheng
 */
@Service
public class RedisServiceImpl extends IRedisService<JWTUserBean>{
    private static final String REDIS_KEY = "OSPJWT";

    @Override
    protected String getRedisKey() {
        return this.REDIS_KEY;
    }
}
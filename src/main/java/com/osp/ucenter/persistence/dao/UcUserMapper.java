package com.osp.ucenter.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.osp.ucenter.persistence.bo.UcRoleBo;
import com.osp.ucenter.persistence.model.UcUser;

public interface UcUserMapper {
	/**
	 * 用户登陆
	 * @param ucUser
	 * @return
	 */
	UcUser login(@Param("username") String username, @Param("password") String password);
	/**
	 * 判断用户是否存在
	 * @param username
	 * @param password
	 * @return
	 */
	UcUser findUser(@Param("userId") Integer userId);
	UcUser findUserByUserName(@Param("userName") String userName);
   
    int deleteByPrimaryKey(Integer userId);

    int insert(UcUser record);
    
    int insertSelective(UcUser record);

    UcUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(UcUser record);

    int updateByPrimaryKey(UcUser record);
    
	List<UcRoleBo> selectRoleByUserId(@Param("id") Integer id);
}
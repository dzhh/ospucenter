package com.osp.ucenter.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.osp.ucenter.persistence.model.UcUserRole;

public interface UcUserRoleMapper {

	int insert(UcUserRole record);

	int insertSelective(UcUserRole record);

	int deleteByUserId(@Param("id") Integer id);
	
	int deleteByRoleId(@Param("id") Integer id);

	int deleteRoleByUserIds(@Param("userIds") String[] userIds);

	List<UcUserRole> findUserByUserId(Integer id);
}
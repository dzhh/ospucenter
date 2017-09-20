package com.osp.ucenter.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.osp.ucenter.persistence.model.UcRolePermission;

public interface UcRolePermissionMapper {
    int insert(UcRolePermission record);

    int insertSelective(UcRolePermission record);
    
    List<UcRolePermission> findRolePermissionByPid(Integer permissionId);
	
	List<UcRolePermission> findRolePermissionByRid(Integer id);
	
	List<UcRolePermission> find(UcRolePermission entity);
	
	int deleteByPid(Integer id);
	
	int deleteByRid(@Param("roleid") Integer roleid);
	
	int delete(UcRolePermission entity);

	int deleteByRids(@Param("roleIds") String[] roleIds);
}
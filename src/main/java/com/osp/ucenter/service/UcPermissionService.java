package com.osp.ucenter.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.osp.ucenter.persistence.bo.UcPermissionBo;
import com.osp.ucenter.persistence.bo.UcPermissionMenuActionBo;
import com.osp.ucenter.persistence.model.UcMenu;
import com.osp.ucenter.persistence.model.UcPermission;

public interface UcPermissionService {
	int deleteByPrimaryKey(Integer id);

	UcPermission insert(UcPermission record);

	Integer insertSelectiveMenu(UcMenu record);
	
	Map<String, Object> deleteByMenuIds(String menuIds);

    int updateByPrimaryKeySelective(UcPermission record);

    int updateByPrimaryKey(UcPermission record);

	Map<String, Object> deletePermissionById(String ids);

	Map<String, Object> addPermission2Role(Integer roleId,String ids);

	Map<String, Object> deleteByRids(String roleIds);
	//根据用户ID查询权限（permission），放入到Authorization里。
	Set<String> findPermissionByUserId(Integer userId);
	//增
	
	//删
	
	//改
	
	//查
	UcPermission selectByPrimaryKey(Integer id);
	
    List<UcPermissionMenuActionBo> selectPermissions();
	
	List<UcPermissionBo> selectPermissionByRoleId(Integer id);
	
	List<UcPermission> selectPermissionById(Integer id);
	
	List<UcMenu> selectMenus();
	
	
	
}

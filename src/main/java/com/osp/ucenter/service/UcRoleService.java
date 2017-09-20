package com.osp.ucenter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.osp.ucenter.mybatis.page.Pagination;
import com.osp.ucenter.persistence.bo.UcRolePermissionAllocationBo;
import com.osp.ucenter.persistence.model.UcAction;
import com.osp.ucenter.persistence.model.UcMenu;
import com.osp.ucenter.persistence.model.UcRole;

public interface UcRoleService {
	
	int deleteByPrimaryKey(Integer id);

	int insert(UcRole ucRole);
	
	int findCount();
	
	int insertSelective(UcRole ucRole);
    
	ArrayList<UcRole> selectAllRoles();
	
	UcRole selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(UcRole ucRole);

	int updateByPrimaryKey(UcRole ucRole);

	Pagination<UcRole> findPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize);

	Map<String, Object> deleteRoleById(String ids);

	List<UcRolePermissionAllocationBo> selectPermissionByRoleIds();

	Set<String> findRoleByUserId(Long userId);
	
	List<UcRole> findAllPermissionByUser(Integer userId);
	
	int hasPermission(Integer userId,String uri);
	
	public Set<UcMenu> getMenuTrees(Integer userId);
	
	public Set<UcAction> getActionTrees(Integer userId,String  menuUrl);
}

package com.osp.ucenter.service;

import java.util.List;
import java.util.Map;

import com.osp.ucenter.mybatis.page.Pagination;
import com.osp.ucenter.persistence.bo.UcRoleBo;
import com.osp.ucenter.persistence.bo.UserRoleAllocationBo;
import com.osp.ucenter.persistence.model.UcUser;

/**
 * 用户业务接口
 * 
 * @author zhangmingcheng
 */
public interface UcUserService {

	Map<String, Object> updateForbidUserById(Integer id,Integer status);

	Map<String, Object> shotOffOnlineUser(UcUser record);

	Pagination<UserRoleAllocationBo> findUserAndRole(Map<String, Object> modelMap, Integer pageNo, Integer pageSize);

	Pagination<UcUser> findPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize);

	UcUser login(String username, String password);

	int deleteByPrimaryKey(Integer id);

	int insert(UcUser record);

	int insertSelective(UcUser record);

	UcUser selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(UcUser record);

	int updateByPrimaryKey(UcUser record);

	UcUser findUser(Integer userId);
	
	UcUser findUserByUserName(String userName);

	List<UcRoleBo> selectRoleByUserId(Integer id);

	Map<String, Object> addRole2User(Integer userId, String ids);

	Map<String, Object> deleteRoleByUserIds(String userIds);

	Map<String, Object> deleteUserById(String ids);

}

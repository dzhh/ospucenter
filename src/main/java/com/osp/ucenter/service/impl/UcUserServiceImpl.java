package com.osp.ucenter.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osp.ucenter.mybatis.BaseMybatisDao;
import com.osp.ucenter.mybatis.page.Pagination;
import com.osp.ucenter.persistence.bo.UserRoleAllocationBo;
import com.osp.ucenter.persistence.dao.UcUserMapper;
import com.osp.ucenter.persistence.model.UcUser;
import com.osp.ucenter.service.UcUserService;

/**
 * 用户业务实现类
 * 
 * @author zhangmingcheng
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class UcUserServiceImpl extends BaseMybatisDao<UcUserMapper> implements UcUserService {

	@Autowired
	private UcUserMapper ucUserMapper;


	@SuppressWarnings("rawtypes")
	@Override
	public Pagination<UserRoleAllocationBo> findUserAndRole(Map modelMap,
			Integer pageNo, Integer pageSize) {
		return super.findPage("findUserAndRole", "findCount", modelMap, pageNo, pageSize);
	}
	
	@Override
	public Pagination<UcUser> findPage(Map<String, Object> resultMap,Integer pageNo, Integer pageSize) {
		return super.findPage(resultMap, pageNo, pageSize);
	}

	@Override
	public UcUser login(String username, String password) {
		return ucUserMapper.login(username, password);
	}

	@Override
	public int deleteByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(UcUser record) {
		return ucUserMapper.insert(record);
	}

	@Override
	public int insertSelective(UcUser record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UcUser selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(UcUser ucUser) {
		return ucUserMapper.updateByPrimaryKeySelective(ucUser);
	}

	@Override
	public int updateByPrimaryKey(UcUser record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UcUser findUser(int userId) {
		return ucUserMapper.findUser(userId);
	}

	@Override
	public Map<String, Object> deleteUserById(String ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateForbidUserById(Long id, Long status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> addRole2User(Long userId, String ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteRoleByUserIds(String userIds) {
		// TODO Auto-generated method stub
		return null;
	}

	public UcUserMapper getUcUserMapper() {
		return ucUserMapper;
	}

	public void setUcUserMapper(UcUserMapper ucUserMapper) {
		this.ucUserMapper = ucUserMapper;
	}

}

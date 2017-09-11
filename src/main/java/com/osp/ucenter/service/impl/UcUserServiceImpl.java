package com.osp.ucenter.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osp.common.json.JsonUtil;
import com.osp.ucenter.common.utils.LoggerUtils;
import com.osp.ucenter.common.utils.StringUtils;
import com.osp.ucenter.mybatis.BaseMybatisDao;
import com.osp.ucenter.mybatis.page.Pagination;
import com.osp.ucenter.persistence.bo.JWTUserBean;
import com.osp.ucenter.persistence.bo.UcRoleBo;
import com.osp.ucenter.persistence.bo.UserRoleAllocationBo;
import com.osp.ucenter.persistence.dao.UcUserMapper;
import com.osp.ucenter.persistence.dao.UcUserRoleMapper;
import com.osp.ucenter.persistence.model.UcUser;
import com.osp.ucenter.persistence.model.UcUserRole;
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

	@Autowired
	private UcUserRoleMapper ucUserRoleMapper;

	@Autowired
	private RedisServiceImpl redisServiceImpl;

	@Override
	public Pagination<UserRoleAllocationBo> findUserAndRole(Map<String, Object> modelMap, Integer pageNo,
			Integer pageSize) {
		return super.findPage("findUserAndRole", "findCount", modelMap, pageNo, pageSize);
	}

	@Override
	public Pagination<UcUser> findPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize) {
		return super.findPage(resultMap, pageNo, pageSize);
	}

	@Override
	public UcUser login(String username, String password) {
		return ucUserMapper.login(username, password);
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
	public UcUser selectByPrimaryKey(Integer id) {
		return ucUserMapper.selectByPrimaryKey(id);
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
	public UcUser findUser(Integer userId) {
		return ucUserMapper.findUser(userId);
	}

	/**
	 * 删除用户 用户必须没有关联角色才能被删除,如果当前用户在线，踢出用户
	 */
	@Override
	public Map<String, Object> deleteUserById(String ids) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			int successCount = 0, errorCount = 0;
			String resultMsg = "删除%s条，失败%s条";
			String[] idArray = new String[] {};
			if (StringUtils.contains(ids, ",")) {
				idArray = ids.split(",");
			} else {
				idArray = new String[] { ids };
			}
			for (String idx : idArray) {
				Integer userId = new Integer(idx);
				List<UcUserRole> ucUserRoles = ucUserRoleMapper.findUserByUserId(userId);
				if (null != ucUserRoles && ucUserRoles.size() > 0) {
					errorCount += ucUserRoles.size();
				} else {
					shotOffOnlineUser(this.selectByPrimaryKey(userId));
					successCount += this.deleteByPrimaryKey(new Integer(userId));
				}
			}
			// 如果有成功的，也有失败的，提示清楚。
			if (errorCount > 0) {
				data.put("ucUserStatus", String.format(resultMsg, successCount, errorCount));
			} else {
				data.put("ucUserStatus", "操作成功");
			}
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "根据IDS删除用户出现错误，ids[%s]", ids);
		}
		return data;
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return ucUserMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Map<String, Object> addRole2User(Integer userId, String ids) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			// 先删除原有的。
			ucUserRoleMapper.deleteByUserId(userId);
			// 如果ids(role的id)有值，那么就添加。没值象征着：把这个用户（userId）所有角色取消。
			if (StringUtils.isNotBlank(ids)) {
				String[] idArray = null;

				if (StringUtils.contains(ids, ",")) {
					idArray = ids.split(",");
				} else {
					idArray = new String[] { ids };
				}
				// 添加新的。
				for (String rid : idArray) {
					if (StringUtils.isNotBlank(rid)) {
						UcUserRole ucUserRole = new UcUserRole(userId, new Integer(rid));
						ucUserRoleMapper.insertSelective(ucUserRole);
					}
				}
			}
			data.put("ucUserRole", "维护用户角色关系成功");
			return data;
		} catch (Exception e) {
			data.put("ucUserRole", "操作失败，请重试！");
			e.printStackTrace();
		}
		// 清空用户的权限，迫使再次获取权限的时候，得重新加载
		// TokenManager.clearUserAuthByUserId(userId);
		return data;
	}

	/**
	 * 根据用户ID删除其绑定的角色信息
	 */
	@Override
	public Map<String, Object> deleteRoleByUserIds(String userIds) {

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			String[] idArray = new String[] {};
			if (StringUtils.contains(userIds, ",")) {
				idArray = userIds.split(",");
			} else {
				idArray = new String[] { userIds };
			}
			for (String id : idArray) {
				this.deleteByPrimaryKey(new Integer(id));
			}
			data.put("status", 200);
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "根据IDS删除用户出现错误，ids[%s]", userIds);
			data.put("status", 500);
		}
		return data;
	}

	@Override
	public List<UcRoleBo> selectRoleByUserId(Integer id) {
		return ucUserMapper.selectRoleByUserId(id);
	}

	/**
	 * 禁止用户登录:如果当前用户在线，踢出
	 */
	@Override
	public Map<String, Object> updateForbidUserById(Integer id,Integer status) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			UcUser ucuser = this.selectByPrimaryKey(id);
			ucuser.setStatus(status);
			updateByPrimaryKeySelective(ucuser);
			shotOffOnlineUser(ucuser);
			data.put("status", 200);
		} catch (Exception e) {
			data.put("status", 500);
			LoggerUtils.fmtError(getClass(), "禁止或者激活用户登录失败，id[%s],status[%s]", id, 0);
		}
		return data;
	}

	/**
	 * 踢出用户
	 */
	@Override
	public Map<String, Object> shotOffOnlineUser(UcUser ucUser) {
		for (String jwtToken : redisServiceImpl.getKeys()) {
			Object jwtUser= redisServiceImpl.get(jwtToken);
			JWTUserBean tempUser = JsonUtil.jsonToBean(JsonUtil.beanToJson(jwtUser), JWTUserBean.class);
			if (tempUser.getUserId()==ucUser.getUserId()){
				redisServiceImpl.remove(jwtToken);
			}
		}
		return null;
	}

	@Override
	public UcUser findUserByUserName(String userName) {
		return ucUserMapper.findUserByUserName(userName);
	}

}

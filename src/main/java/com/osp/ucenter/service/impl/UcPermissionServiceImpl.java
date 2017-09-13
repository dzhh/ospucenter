package com.osp.ucenter.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osp.ucenter.common.utils.LoggerUtils;
import com.osp.ucenter.common.utils.StringUtils;
import com.osp.ucenter.mybatis.BaseMybatisDao;
import com.osp.ucenter.persistence.bo.UcPermissionBo;
import com.osp.ucenter.persistence.bo.UcPermissionMenuActionBo;
import com.osp.ucenter.persistence.dao.UcActionMapper;
import com.osp.ucenter.persistence.dao.UcMenuMapper;
import com.osp.ucenter.persistence.dao.UcPermissionActionMapper;
import com.osp.ucenter.persistence.dao.UcPermissionMapper;
import com.osp.ucenter.persistence.dao.UcRolePermissionMapper;
import com.osp.ucenter.persistence.dao.UcUserMapper;
import com.osp.ucenter.persistence.dao.UcUserRoleMapper;
import com.osp.ucenter.persistence.model.UcMenu;
import com.osp.ucenter.persistence.model.UcPermission;
import com.osp.ucenter.persistence.model.UcRolePermission;
import com.osp.ucenter.service.UcPermissionService;

/**
 * 权限管理
 * 
 * @author zhangmingcheng
 */
@Service
@Transactional
public class UcPermissionServiceImpl extends BaseMybatisDao<UcPermissionMapper> implements UcPermissionService {

	@Autowired
	UcPermissionMapper ucPermissionMapper;

	@Autowired
	UcMenuMapper ucMenuMapper;

	@Autowired
	UcActionMapper ucActionMapper;

	@Autowired
	UcPermissionActionMapper ucPermissionActionMapper;

	@Autowired
	UcUserMapper ucUserMapper;

	@Autowired
	UcRolePermissionMapper ucRolePermissionMapper;

	@Autowired
	UcUserRoleMapper ucUserRoleMapper;

	/**
	 * 删除权限：因为菜单与权限与操作是1v1关系，所以要删除菜单
	 */
	@Override
	public int deleteByPrimaryKey(Integer id) {
		int status = 0;
		UcPermission ucPermission = ucPermissionMapper.selectByPrimaryKey(id);
		if (ucPermission != null) {
			if (ucPermission.getMenuId() != null) {
				ucMenuMapper.deleteByPrimaryKey(ucPermission.getMenuId());
			}else if(ucPermission.getActionId()!=null){
				ucActionMapper.deleteByPrimaryKey(ucPermission.getActionId());
			}
			status = ucPermissionMapper.deleteByPrimaryKey(id);
		}
		return status;
	}

	@Override
	public UcPermission insert(UcPermission record) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 插入菜单，同时维护权限表
	 */
	@Override
	public Integer insertSelectiveMenu(UcMenu ucMenu) {
		int status = ucMenuMapper.insertSelective(ucMenu);
		if (status > 0) {
			UcPermission ucPermission = new UcPermission("MENU", ucMenu.getMenuId(),null);
			status = ucPermissionMapper.insertSelective(ucPermission);
			if (status > 0) {
				// 每添加一个权限，都往系统管理员里添加一次。保证系统管理员有最大的权限
				executePermission(1, String.valueOf(ucPermission.getPermissionId()));
			}
		}
		return status;
	}

	/**
	 * 删除菜单，同时维护权限表
	 */
	@Override
	public Map<String, Object> deleteByMenuIds(String ids) {
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
				Integer menuId = new Integer(idx);
				int permissionId = ucPermissionMapper.selectByMenuId(menuId);
				List<UcRolePermission> rolePermissions = ucRolePermissionMapper.findRolePermissionByPid(permissionId);
				if (null != rolePermissions && rolePermissions.size() > 0) {
					errorCount += rolePermissions.size();
				} else {
					successCount += ucMenuMapper.deleteByPrimaryKey(menuId);// permission表menuId外键删除时属性设为CASCADE
				}
			}
			// 如果有成功的，也有失败的，提示清楚。
			if (errorCount > 0) {
				data.put("ucRolePermission", String.format(resultMsg, successCount, errorCount));
			} else {
				data.put("ucRolePermission", "操作成功");
			}
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "根据IDS删除用户出现错误，ids[%s]", ids);
			data.put("ucRolePermission", "删除出现错误，请刷新后再试！");
		}
		return data;
	}

	/**
	 * 处理角色权限
	 * 
	 * @param roleId
	 * @param ids
	 * @return
	 */
	private Map<String, Object> executePermission(Integer roleId, String ids) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			// 如果ids,permission 的id 有值，那么就添加。
			if (StringUtils.isNotBlank(ids)) {
				String[] idArray = null;

				if (StringUtils.contains(ids, ",")) {
					idArray = ids.split(",");
				} else {
					idArray = new String[] { ids };
				}
				// 添加新的。
				for (String pid : idArray) {
					if (StringUtils.isNotBlank(pid)) {
						UcRolePermission entity = new UcRolePermission(roleId, new Integer(pid));
						ucRolePermissionMapper.insertSelective(entity);
					}
				}
			}

		} catch (Exception e) {
			data.put("ucRolePermission", "操作失败，请重试！");
		}
		data.put("ucRolePermission", "操作成功");
		return data;

	}

	@Override
	public UcPermission selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(UcPermission record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(UcPermission record) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 删除权限：需要查询是否有赋予给角色，如果有角色在使用，那么就不能删除,同时维护操作表、菜单表
	 */
	@Override
	public Map<String, Object> deletePermissionById(String ids) {
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
				Integer id = new Integer(idx);
				List<UcRolePermission> rolePermissions = ucRolePermissionMapper.findRolePermissionByPid(id);
				if (null != rolePermissions && rolePermissions.size() > 0) {
					errorCount += rolePermissions.size();
				} else {
					successCount += this.deleteByPrimaryKey(id);
				}
			}
			// 如果有成功的，也有失败的，提示清楚。
			if (errorCount > 0) {
				data.put("ucRolePermission", String.format(resultMsg, successCount, errorCount));
			} else {
				data.put("ucRolePermission", "操作成功");
			}
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "根据IDS删除用户出现错误，ids[%s]", ids);
			data.put("ucRolePermission", "删除出现错误，请刷新后再试！");
		}
		return data;
	}

	@Override
	public List<UcPermission> selectPermissionById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 操作角色的权限
	 */
	@Override
	public Map<String, Object> addPermission2Role(Integer roleId, String ids) {
		//先删除原有的。
		ucRolePermissionMapper.deleteByRid(roleId);
		return executePermission(roleId, ids);
	}

	/**
	 * 清空角色权限
	 */
	@Override
	public Map<String, Object> deleteByRids(String roleIds) {
		Map<String,Object> data = new HashMap<String, Object>();
		try {
			String[] idArray = new String[] {};
			if (StringUtils.contains(roleIds, ",")) {
				idArray = roleIds.split(",");
			} else {
				idArray = new String[] { roleIds };
			}
			ucRolePermissionMapper.deleteByRids(idArray);
			data.put("status", "200");
		} catch (Exception e) {
			data.put("status", "500");
		}
		return data;
	}
	
	@Override
	public Set<String> findPermissionByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UcPermissionBo> selectPermissionByRoleId(Integer id) {
		return ucPermissionMapper.selectPermissionByRoleId(id);
	}

	@Override
	public List<UcPermissionMenuActionBo> selectPermissions() {
		return ucPermissionMapper.selectPermissions();
	}

	@Override
	public List<UcMenu> selectMenus() {
	   return ucMenuMapper.selectMenus();
	}
	
	

}

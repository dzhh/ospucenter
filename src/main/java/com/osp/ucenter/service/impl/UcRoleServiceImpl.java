package com.osp.ucenter.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osp.common.json.JsonUtil;
import com.osp.ucenter.common.utils.LoggerUtils;
import com.osp.ucenter.mybatis.BaseMybatisDao;
import com.osp.ucenter.mybatis.page.Pagination;
import com.osp.ucenter.persistence.bo.UcPermissionMenuActionBo;
import com.osp.ucenter.persistence.bo.UcRolePermissionAllocationBo;
import com.osp.ucenter.persistence.dao.UcRoleMapper;
import com.osp.ucenter.persistence.dao.UcUserRoleMapper;
import com.osp.ucenter.persistence.model.UcAction;
import com.osp.ucenter.persistence.model.UcMenu;
import com.osp.ucenter.persistence.model.UcRole;
import com.osp.ucenter.service.UcRoleService;

/**
 * 角色业务类
 * 
 * @author zhangmingcheng
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class UcRoleServiceImpl extends BaseMybatisDao<UcRoleMapper> implements UcRoleService {

	@Autowired
	private UcRoleMapper ucRoleMapper;

	@Autowired
	private UcUserRoleMapper ucUserRoleMapper;
	
	@Autowired
	private MyPermissionRedisServiceImpl myPermissionRedisServiceImpl;

	@Override
	public int findCount() {
		return ucRoleMapper.findCount();
	}

	@Override
	public Pagination<UcRole> findPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize) {
		return super.findPage(resultMap, pageNo, pageSize);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return ucRoleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(UcRole ucRole) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(UcRole ucRole) {
		return ucRoleMapper.insertSelective(ucRole);
	}

	@Override
	public UcRole selectByPrimaryKey(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(UcRole ucRole) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(UcRole ucRole) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 删除角色，前台可以多选，角色id使用“,”分割,同时需要维护用户角色表
	 */
	@Override
	public Map<String, Object> deleteRoleById(String ids) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int count = 0;
			String resultMsg = "删除成功。";
			String[] idArray = new String[] {};
			if (StringUtils.contains(ids, ",")) {
				idArray = ids.split(",");
			} else {
				idArray = new String[] { ids };
			}

			c: for (String idx : idArray) {
				Integer id = new Integer(idx);
				if (new Integer(1).equals(id)) {
					resultMsg = "操作成功，但是'系统管理员不能删除。";
					continue c;
				} else {
					count += this.deleteByPrimaryKey(id);
					ucUserRoleMapper.deleteByRoleId(id);
					// 更新redis里面存的我的权限
					List<UcRole> ucRoles = this.findAllPermissionByUser(id);
					myPermissionRedisServiceImpl.put(idx, ucRoles, -1);
				}
			}
			resultMap.put("count", count);
			resultMap.put("msg", resultMsg);
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "根据IDS删除用户出现错误，ids[%s]", ids);
		}
		return resultMap;
	}

	@Override
	public Set<String> findRoleByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UcRole> findAllPermissionByUser(Integer userId) {
		return ucRoleMapper.findAllPermissionByUser(userId);
	}

	@Override
	public ArrayList<UcRole> selectAllRoles() {
		return ucRoleMapper.selectAllRoles();
	}

	@Override
	public List<UcRolePermissionAllocationBo> selectPermissionByRoleIds() {
		return ucRoleMapper.selectPermissionByRoleIds();
	}

	/**
	 * 判断用户是否有调用uri接口权限 返回值0代表没权限，返回值1为菜单权限，返回值2为操作权限
	 */
	@Override
	public int hasPermission(Integer userId, String uri) {
		int flag = 0;
		List<UcRole> ucRoles = JsonUtil.jsonToBeanList(JsonUtil.beanListToJson(myPermissionRedisServiceImpl.get(userId.toString())), UcRole.class);
		for (UcRole ucRole : ucRoles) {
			List<UcPermissionMenuActionBo> ucPermissionMenuActionBos = JsonUtil.jsonToBeanList(JsonUtil.beanListToJson(ucRole.getPermissions()), UcPermissionMenuActionBo.class);
			for (UcPermissionMenuActionBo ucPermissionMenuActionBo : ucPermissionMenuActionBos) {
				if (ucPermissionMenuActionBo.getMenuUrl() != null
						&& ucPermissionMenuActionBo.getMenuUrl().equals(uri)) {
					flag = 1;
					break;
				} else if (ucPermissionMenuActionBo.getActionCode() != null
						&& ucPermissionMenuActionBo.getActionCode().equals(uri)) {
					flag = 2;
					break;
				}
			}
			if (flag != 0) {
				break;
			}
		}
		return flag;
	}

	/**
	 * 组织前台菜单 json转bean将数据库表字段中Integer类型null值转为了0
	 */
	public Set<UcMenu> getMenuTrees(Integer userId) {
		Set<UcMenu> ucMenus = new LinkedHashSet<UcMenu>();
		Set<UcMenu> trees = new LinkedHashSet<UcMenu>();
		List<UcRole> ucRoles = JsonUtil.jsonToBeanList(JsonUtil.beanListToJson(myPermissionRedisServiceImpl.get(userId.toString())), UcRole.class);
		for (UcRole ucRole : ucRoles) {	
			List<UcPermissionMenuActionBo> ucPermissionMenuActionBos = JsonUtil.jsonToBeanList(JsonUtil.beanListToJson(ucRole.getPermissions()), UcPermissionMenuActionBo.class);
			for (UcPermissionMenuActionBo menuBo : ucPermissionMenuActionBos) {
				if (menuBo.getMenuId() != 0) {
					UcMenu ucMenu = new UcMenu(menuBo.getMenuId(), menuBo.getMenuName(), menuBo.getMenuUrl(),
							menuBo.getMenuParent(), menuBo.getMenuIcon());
					ucMenus.add(ucMenu);			
					if (menuBo.getMenuParent() == 0) {
						trees.add(ucMenu);
					}
				}
			}
		}
		for (UcMenu ucMenu : trees) {
			this.organizingMenuTree(ucMenu, ucMenus);
		}
		return trees;
	}

	public void organizingMenuTree(UcMenu ucMenu, Set<UcMenu> ucMenus) {
		for (UcMenu tempMenu : ucMenus) {
			if (tempMenu.getMenuParent() != 0 && ucMenu.getMenuId() == tempMenu.getMenuParent()) {
				ucMenu.getChildren().add(tempMenu);
				organizingMenuTree(tempMenu, ucMenus);
			}
		}
	}

	/**
	 * 取得当前菜单的所有操作权限
	 */
	@Override
	public Set<UcAction> getActionTrees(Integer userId, String menuUrl) {
		Set<UcAction> ucActions = new LinkedHashSet<UcAction>();
		Set<UcAction> trees = new LinkedHashSet<UcAction>();
		List<UcRole> ucRoles = JsonUtil.jsonToBeanList(JsonUtil.beanListToJson(myPermissionRedisServiceImpl.get(userId.toString())), UcRole.class);
		for (UcRole ucRole : ucRoles) {
			List<UcPermissionMenuActionBo> ucPermissionMenuActionBos = JsonUtil.jsonToBeanList(JsonUtil.beanListToJson(ucRole.getPermissions()), UcPermissionMenuActionBo.class);
			for (UcPermissionMenuActionBo actionBo : ucPermissionMenuActionBos) {
				if (actionBo.getActionId() != 0 && actionBo.getActionPreventUrl().equals(menuUrl)) {
					UcAction ucAction = new UcAction(actionBo.getActionId(), actionBo.getActionName(),
							actionBo.getActionCode(), actionBo.getActionParent(), actionBo.getActionPreventUrl());
					ucActions.add(ucAction);
					if (actionBo.getActionParent() == 0) {
						trees.add(ucAction);
					}
				}
			}
		}

		for (UcAction ucAction : trees) {
			this.organizingActionTree(ucAction, ucActions);
		}
		return trees;
	}

	public void organizingActionTree(UcAction ucAction, Set<UcAction> ucActions) {
		for (UcAction tempAction : ucActions) {
			if (tempAction.getActionParent() != 0 && ucAction.getActionId() == tempAction.getActionParent()) {
				ucAction.getChildren().add(tempAction);
				organizingActionTree(tempAction, ucActions);
			}
		}

	}

}

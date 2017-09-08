package com.osp.ucenter.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osp.ucenter.common.utils.LoggerUtils;
import com.osp.ucenter.mybatis.BaseMybatisDao;
import com.osp.ucenter.mybatis.page.Pagination;
import com.osp.ucenter.persistence.bo.UcRolePermissionAllocationBo;
import com.osp.ucenter.persistence.dao.UcRoleMapper;
import com.osp.ucenter.persistence.dao.UcUserRoleMapper;
import com.osp.ucenter.persistence.model.UcRole;
import com.osp.ucenter.service.UcRoleService;

/**
 * 角色业务类
 * @author zhangmingcheng
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class UcRoleServiceImpl extends BaseMybatisDao<UcRoleMapper>  implements UcRoleService {

	@Autowired
	UcRoleMapper ucRoleMapper;
	
	@Autowired
	UcUserRoleMapper ucUserRoleMapper;
	
	@Override
	public int findCount(){
		return ucRoleMapper.findCount();
	}
	
	@Override
	public Pagination<UcRole> findPage(Map<String, Object> resultMap,
			Integer pageNo, Integer pageSize) {
		return super.findPage(resultMap, pageNo, pageSize);
	}
	
	/*@Override
	public Pagination<RolePermissionAllocationBo> findRoleAndPermissionPage(
			Map<String, Object> resultMap, Integer pageNo, Integer pageSize) {
		return super.findPage("findRoleAndPermission", "findCount", resultMap, pageNo, pageSize);
	}*/


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
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			int count=0;
			String resultMsg = "删除成功。";
			String[] idArray = new String[]{};
			if(StringUtils.contains(ids, ",")){
				idArray = ids.split(",");
			}else{
				idArray = new String[]{ids};
			}
			
			c:for (String idx : idArray) {
				Integer id = new Integer(idx);
				if(new Integer(1).equals(id)){
					resultMsg = "操作成功，但是'系统管理员不能删除。";
					continue c;
				}else{
					count+=this.deleteByPrimaryKey(id);
					ucUserRoleMapper.deleteByRoleId(id);
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
	public List<UcRole> findNowAllPermission() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", SecurityUtils.getSubject().getPrincipal());
		return new ArrayList<UcRole>();
		//return roleMapper.findNowAllPermission(map);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<UcRole> selectAllRoles() {
		return ucRoleMapper.selectAllRoles();		
	}

	@Override
	public List<UcRolePermissionAllocationBo> selectPermissionByRoleIds() {
		return ucRoleMapper.selectPermissionByRoleIds();
	}
}

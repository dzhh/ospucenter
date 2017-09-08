package com.osp.ucenter.persistence.dao;

import java.util.List;

import com.osp.ucenter.persistence.model.UcMenu;

public interface UcMenuMapper {
	
	int deleteByPrimaryKey(Integer menuId);
	
	int insert(UcMenu record);
	
	int insertSelective(UcMenu record);
	
	UcMenu selectByPrimaryKey(Integer menuId);
	
	List<UcMenu> selectMenus();
	
	int updateByPrimaryKeySelective(UcMenu record);

	int updateByPrimaryKey(UcMenu record);
}
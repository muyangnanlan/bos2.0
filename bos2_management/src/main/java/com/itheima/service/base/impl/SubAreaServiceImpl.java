package com.itheima.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.base.SubAreaDao;
import com.itheima.domain.base.SubArea;
import com.itheima.service.base.SubAreaService;

@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

	/** 注入数据访问层对象 */
	@Autowired
	private SubAreaDao subAreaDao;

	@Override
	public void save(SubArea subArea) {
		// 调用数据访问层保存对象
		subAreaDao.save(subArea);
	}
	
	/**
	 * @see com.itheima.service.base.SubAreaService#PageFind(org.springframework.data.jpa.domain.Specification, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SubArea> PageFind(Specification<SubArea> sepc, Pageable pageable) {
		// 调用数据访问层条件分压查询
		return subAreaDao.findAll(sepc, pageable);
	}
	
	/**
	 * @see com.itheima.service.base.SubAreaService#save(java.util.List)
	 */
	@Override
	public void save(List<SubArea> subAreas) {
		// 调用数据访问层保存数据
		subAreaDao.save(subAreas);
	}

}

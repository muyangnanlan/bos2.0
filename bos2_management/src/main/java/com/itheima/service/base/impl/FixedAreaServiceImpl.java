package com.itheima.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.base.FixedAreaDao;
import com.itheima.domain.base.FixedArea;
import com.itheima.service.base.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
	/** 注入数据访问层对象   */
	@Autowired
	private FixedAreaDao fixedAreaDao;
	
	/**
	 * @see com.itheima.service.base.FixedAreaService#save(com.itheima.domain.base.FixedArea)
	 */
	@Override
	public void save(FixedArea model) {
		// 调用数据访问层保存对象
		fixedAreaDao.save(model);
	}

	@Override
	public Page<FixedArea> findAll(Specification<FixedArea> spec,
			Pageable pageable) {
		// 调用数据访问层查找数据
		return fixedAreaDao.findAll(spec, pageable);
	}
}

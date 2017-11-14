package com.itheima.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.base.StandardDao;
import com.itheima.domain.base.Standard;
import com.itheima.service.base.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	
	// 注入StandardDao对象
	@Autowired
	private StandardDao standardDao;

	@Override
	public void save(Standard standard) {
		standardDao.save(standard);
	}

	@Override
	public Page<Standard> findPage(Pageable pageable) {
		return standardDao.findAll(pageable);
	}

	@Override
	public Standard findById(Integer id) {
		return standardDao.findOne(id);
	}
	
	/**
	 * @see com.itheima.service.base.StandardService#findAll()
	 */
	@Override
	public List<Standard> findAll() {
		return standardDao.findAll();
	}
	

}

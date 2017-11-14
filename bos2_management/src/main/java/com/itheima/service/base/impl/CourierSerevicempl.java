package com.itheima.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.base.CourierDao;
import com.itheima.domain.base.Courier;
import com.itheima.service.base.CourierSerevice;

@Service
@Transactional
public class CourierSerevicempl implements CourierSerevice {
	// 注入属性CourierDao
	@Autowired
	private CourierDao courierDao;
	/**
	 * @see com.itheima.service.base.CourierSerevice#save(com.itheima.domain.base.Courier)
	 */
	@Override
	public void save(Courier courier) {
		courierDao.save(courier);
	}
	
	/**
	 * @see com.itheima.service.base.CourierSerevice#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Courier> findAll(Specification<Courier> spec, Pageable pageable) {
		return courierDao.findAll(spec, pageable);
	}

	@Override
	public void delBatch(String[] idss) {
		// 便利idss数组
		for (int i = 0; i < idss.length; i++) {
			// 将id转化成Integer类型
			Integer id = Integer.parseInt(idss[i]);
			// 调用访问层修改数据
			courierDao.delBatch(id);
		}
		
	}
	
	/*
	 * @see com.itheima.service.base.CourierSerevice#restoreCouriers(java.lang.String[])
	 */
	@Override
	public void restoreCouriers(String[] arrayIds) {
		// 遍历集合
		for(int i = 0; i < arrayIds.length; i++) {
			// 将id转化成Integer类型
			Integer id = Integer.parseInt(arrayIds[i]);
			// 茶渣id为i为快递员
			Courier courier = courierDao.findOne(id);
			// 需改快递员的deltag值（通过快照修改）
			courier.setDeltag('0');
		}
		
	}

}

package com.itheima.service.delivery.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.delivery.PromotionDao;
import com.itheima.domain.page.PageBean;
import com.itheima.domain.take_delivery.Promotion;
import com.itheima.service.delivery.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
	/** 注入宣传数据访问层  */
	@Autowired
	private PromotionDao promotionDao;
	
	/**
	 * @see com.itheima.service.delivery.PromotionService#save(com.itheima.domain.take_delivery.Promotion)
	 */
	@Override
	public void save(Promotion model) {
		// 调用数据访问层保存对象
		promotionDao.save(model);
	}
	
	/**
	 * @see com.itheima.service.delivery.PromotionService#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Promotion> findAll(Pageable pageable) {
		// 调用数据访问层分页查找所有的宣传对象
		return promotionDao.findAll(pageable);
	}
	
	/**
	 * @see com.itheima.service.delivery.PromotionService#findAll(int, int)
	 */
	@Override
	public PageBean<Promotion> findAll(int page, int rows) {
		// 创建一个分页对象
		Pageable pageable = new PageRequest(page - 1, rows);
		
		// 调用数据访问层分页查找所有的宣传对象
		Page<Promotion> page2 = promotionDao.findAll(pageable);
		
		// 创建自定义的分页对象
		PageBean<Promotion> pageBean = new PageBean<Promotion>();
		/*
		 *  将page2中的数据封装到自定义的有settter的pageBean当中，
		 *  以便前台系统可以封装数据
		 */
		pageBean.setTotalCount(page2.getTotalElements());
		pageBean.setPageData(page2.getContent());
		return pageBean;
	}
	
	/**
	 * @see com.itheima.service.delivery.PromotionService#findById(java.lang.Integer)
	 */
	@Override
	public Promotion findById(Integer id) {
		return promotionDao.findOne(id);
	}
	
	/**
	 * @see com.itheima.service.delivery.PromotionService#updatePromotion(java.util.Date)
	 */
	@Override
	public void updatePromotion(Date date) {
		System.out.println("宣传活动过期");
		// 调用数据访问层进行宣传活动的自动过期
		promotionDao.updatePromotion(date);
	}

}

package com.itheima.service.delivery.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.delivery.WayBillDao;
import com.itheima.domain.take_delivery.WayBill;
import com.itheima.service.delivery.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {
	/** 注入运单的dao层  */
	@Autowired
	private WayBillDao wayBillDao;
	
	/**
	 * @see com.itheima.service.delivery.WayBillService#save(com.itheima.domain.take_delivery.WayBill)
	 */
	@Override
	public void save(WayBill model) {
		
		/*
		 * 这个就是快递员第一次运单号输入错误，需要修正
		 * 判断运单所关联的订单是否已经关联其他的运单：如果已经关联，判断运单号是否 相同？如果相同，修改
		 */
		if (model.getOrder() != null && model.getOrder().getId() != null
				&& model.getOrder().getId() != 0) {
			// 订单id存在查询订单号所关联的运单
			WayBill wayBill = wayBillDao.findByOrder(model
					.getOrder().getOrderNum());
			
			if (wayBill != null) {
				// 调用service层保存数据
				Integer id = wayBill.getId();
				BeanUtils.copyProperties(model, wayBill);
				wayBill.setId(id);
				// 修改订单
				wayBillDao.save(wayBill);
			} else {
				// 查找指定运单号的运单
				WayBill persisWayBill = wayBillDao.findByWayBillNum(model.getWayBillNum());
				if (persisWayBill != null) {
					/*
					 *  运单号存在:将详细录入的数据全部赋值给新查出来的运单对象，
					 *  再添加id到提交到数据库
					 */
					Integer id = persisWayBill.getId();
					BeanUtils.copyProperties(model, persisWayBill);
					persisWayBill.setId(id);
					
				} else {
					// 运单号不存在:调用数据访问层保存运单
					wayBillDao.save(model);
				}
				
			}
		}
		
		

	}
	
	/**
	 * @see com.itheima.service.delivery.WayBillService#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<WayBill> findAll(Pageable pageable) {
		return wayBillDao.findAll(pageable);
	}
	
	/**
	 * @see com.itheima.service.delivery.WayBillService#findByWayBillNum(java.lang.String)
	 */
	@Override
	public WayBill findByWayBillNum(String wayBillNum) {
		return wayBillDao.findByWayBillNum(wayBillNum);
	}




}

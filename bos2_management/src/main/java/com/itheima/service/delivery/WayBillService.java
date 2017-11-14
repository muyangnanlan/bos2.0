package com.itheima.service.delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.domain.take_delivery.WayBill;

public interface WayBillService {
	
	/**
	 * 保存运单
	 * @param model
	 */
	void save(WayBill model);
	
	/**
	 * 分页查找
	 * 
	 * @param pageable
	 * @return
	 */
	Page<WayBill> findAll(Pageable pageable);
	
	/**
	 * 查找指定运单号的订单
	 * 
	 * @param wayBillNum
	 * @return
	 */
	WayBill findByWayBillNum(String wayBillNum);
	

}

package com.itheima.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.itheima.domain.take_delivery.Order;

public interface OrderDao extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order>{
	
	/**
	 * 查找指定订单号的订单
	 * 
	 * @param orderNum
	 * @return
	 */
	Order findByOrderNum(String orderNum);

}

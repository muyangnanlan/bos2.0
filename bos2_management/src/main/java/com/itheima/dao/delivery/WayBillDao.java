package com.itheima.dao.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.take_delivery.WayBill;

public interface WayBillDao extends JpaRepository<WayBill, Integer>{
	
	/**
	 * 查找指定订单号的订单
	 * 
	 * @param wayBillNum
	 * @return
	 */
	WayBill findByWayBillNum(String wayBillNum);
	
	/**
	 * 查找指定订单id的运单号
	 * 
	 * @param orderNum
	 * @return
	 */
	@Query(value = "from WayBill w where w.order.orderNum = ?")
	WayBill findByOrder(String orderNum);

}

package com.itheima.service.delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.itheima.domain.take_delivery.Order;


public interface OrderService {
	
	/**
	 * 保存订单
	 * 
	 * @param order
	 */
	@Path("/saveOrder")
	@POST
	@Consumes({"application/xml", "application/json"})
	public void saveOrder(Order order);
	
	/**
	 * 查找指定订单号的订单
	 * 
	 * @param orderNum
	 * @return
	 */
	public Order findByOrderNum(String orderNum);
}

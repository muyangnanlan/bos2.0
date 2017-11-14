package com.itheima.action.delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.action.commen.BaseAction;
import com.itheima.domain.take_delivery.Order;
import com.itheima.service.delivery.OrderService;
import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order>{
	/** 注入订单的service */
	@Autowired
	private OrderService orderService;
	
	/**
	 * 查找指定订单号的订单
	 * @return
	 */
	@Action(value = "findOne_order", results = {@Result(type="json")})
	public String findOrder() {
		// 查找指定订单号的订单
		Order order = orderService.findByOrderNum(model.getOrderNum());
		
		Map<String, Object> result = new HashMap<String, Object>();
		if (order != null) {
			result.put("success", true);
			result.put("orderData", order);
		} else {
			result.put("success", false);
		}
		// 将结果压入值栈的顶部
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
		

}

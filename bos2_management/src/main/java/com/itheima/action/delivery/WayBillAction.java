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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.SortParameters.Order;
import org.springframework.stereotype.Controller;

import com.itheima.action.commen.BaseAction;
import com.itheima.domain.take_delivery.WayBill;
import com.itheima.service.delivery.WayBillService;
import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {
	/** 日志对象 */
	private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);

	/** 注入运单的service */
	@Autowired
	private WayBillService wayBillService;

	/**
	 * 保存运单
	 * 
	 * @return
	 */
	@Action(value = "save_wayBill", results = { @Result(type = "json") })
	public String saveWayBill() {
		// 创建map集合，保存运单成功与否的结果
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			/*
			 * 无order id 保存运单时，会出现order为瞬时对象，出错
			 * 解决页面没有查出订单的问题，也就是客户直接去快递单位寄快递，当order的id为空时，将order 整个置为空
			 */
			if (model.getOrder() != null
					&& (model.getOrder().getId() == null || model.getOrder()
							.getId() == 0)) {
				model.setOrder(null);
			}

			// 修改订单
			wayBillService.save(model);
			
			// 保存成功
			result.put("success", true);
			result.put("msg", "运单保存成功");
			LOGGER.info("保存运单成功，运单号为：" + model.getWayBillNum());
		} catch (Exception e) {
			// 保存失败
			result.put("success", false);
			result.put("msg", "运单保存失败");
			e.printStackTrace();
			LOGGER.error("保存运单失败，运单号为：" + model.getWayBillNum(), e);
		}
		// 将数据压入值栈的顶部
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	@Action(value = "pageFind_wayBill", results = @Result(type = "json"))
	public String findPage() {
		// 创建分页对象
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(
				new Sort.Order(Sort.Direction.DESC, "wayBillNum")));

		// 调用业务层分页查询
		Page<WayBill> page = wayBillService.findAll(pageable);

		// 将数据压入值栈的顶部
		pushStack(page);

		return SUCCESS;
	}

	/**
	 * 查找指定运单号的订单
	 * 
	 * @return
	 */
	@Action(value = "findOne_wayBill", results = { @Result(type = "json") })
	public String findWayBill() {
		// 查找指定运单号的订单
		WayBill wayBill = wayBillService
				.findByWayBillNum(model.getWayBillNum());

		Map<String, Object> result = new HashMap<String, Object>();
		if (wayBill != null) {
			result.put("success", true);
			result.put("wayBillData", wayBill);
		} else {
			result.put("success", false);
		}
		// 将结果压入值栈的顶部
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

}

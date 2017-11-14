package com.itheima.action;


import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.crm.domain.Customer;

import com.itheima.domain.base.Area;
import com.itheima.domain.base.Courier;
import com.itheima.domain.constants.Constants;
import com.itheima.domain.take_delivery.Order;

/**
 * 订单的页面数据接收层
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年11月10日
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {

	// 属性驱动
	/** 寄件人区域信息 */
	private String sendAreaInfo;

	/** 收件人区域信息 */
	private String recAreaInfo;

	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	@Action(value = "save_order", results = @Result(name = "success", type="redirect" , location = "index.html"))
	public String saveOrder() {

		// 获取寄件人信息
		String[] strings = sendAreaInfo.split("/");
		Area sendArea = new Area();
		sendArea.setProvince(strings[0]);
		sendArea.setCity(strings[1]);
		sendArea.setDistrict(strings[2]);
		// 设置寄件人信息
		model.setSendArea(sendArea);

		// 获取收件人信息
		String[] strings2 = recAreaInfo.split("/");
		Area recArea = new Area();
		recArea.setProvince(strings2[0]);
		recArea.setCity(strings2[1]);
		recArea.setDistrict(strings2[2]);
		// 设置收件人信息
		model.setRecArea(recArea);
		

		// 关联当前登录客户
		// 从session中获取当前关联客户
		Customer customer = (Customer) ServletActionContext.getRequest()
				.getSession().getAttribute("customer");
		System.out.println(customer);
		model.setCustomer_id(customer.getId());
		System.out.println("bos1");
		// 将数据传递给bos_management项目
		WebClient
				.create(Constants.BOS_MANAGEMENT_URL + "/services"
						+ "/orderService/saveOrder")
				.type(MediaType.APPLICATION_JSON).post(model);

		System.out.println("bos2");
		return SUCCESS;
	}
}

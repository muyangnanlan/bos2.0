package com.itheima.service.delivery.impl;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.dao.base.AreaDao;
import com.itheima.dao.base.FixedAreaDao;
import com.itheima.dao.base.OrderDao;
import com.itheima.dao.delivery.WorkBillDao;
import com.itheima.domain.base.Area;
import com.itheima.domain.base.Courier;
import com.itheima.domain.base.FixedArea;
import com.itheima.domain.base.SubArea;
import com.itheima.domain.constants.Constants;
import com.itheima.domain.take_delivery.Order;
import com.itheima.domain.take_delivery.WorkBill;
import com.itheima.service.delivery.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	/** 注入数据area的数据访问层 */
	@Autowired
	private AreaDao areaDao;

	/** 注入order的数据访问层 */
	@Autowired
	private OrderDao orderDao;

	/** 注入定区的数据访问层 */
	@Autowired
	private FixedAreaDao fixedAreaDao;

	/** 注入工单的数据访问层 */
	@Autowired
	private WorkBillDao workBillDao;

	/** 注入activemq的模板 */
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	/**
	 * @see com.itheima.service.delivery.OrderService#saveOrder(com.itheima.domain.take_delivery.Order)
	 */
	@Override
	public void saveOrder(Order order) {
		System.out.println("bos_mange1");
		// 设置订单号
		order.setOrderNum(UUID.randomUUID().toString());

		// 设置下单时间
		order.setOrderTime(new Date());

		// 设置订单状态为待取件
		order.setStatus("1");

		// 设置寄件区域
		Area sendArea = order.getSendArea();
		// 查找寄件区域（有id的持久态的区域）
		Area _sendArea = areaDao.findByProvinceAndCityAndDistrict(
				sendArea.getProvince(), sendArea.getCity(),
				sendArea.getDistrict());
		order.setSendArea(_sendArea);
		System.out.println("bos_mange2");
		// 设置收件区域
		Area recArea = order.getRecArea();
		// 查找收件区域（有id的持久态的区域）
		Area _recArea = areaDao
				.findByProvinceAndCityAndDistrict(recArea.getProvince(),
						recArea.getCity(), recArea.getDistrict());
		order.setRecArea(_recArea);

		// 自动分单功能：根据订单寄件人地址信息完全匹配
		// 跨项目根据寄件地址查找关联的定区
		String fixedAreaId = WebClient
				.create(Constants.CRM_MANAGEMENT_URL
						+ "/services/customerService/findFixedAreaIdBySendAddress?sendAddress="
						+ order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);
		System.out.println("bos_mange3");
		if (fixedAreaId != null) {
			// 分局定区id查找定区
			FixedArea fixedArea = fixedAreaDao.findOne(fixedAreaId);

			// 查找定区的所有的快递员
			final Courier courier = fixedArea.getCouriers().iterator().next();

			if (courier != null) {
				// 自动分单成功
				System.out.println("自动分单成功。。。。。。。");
				
				// 保存订单
				saveOrder(order, courier);
				
				// 生成工单，给快递员发信息
				saveWorkBillAndSendMessageToCourier(order, courier);
				return;
			}

		}
		
		/*
		 *  根据寄件地址没有查找到定区id，根据关键字检索
		 *  根据订单的区域查找区域下的所有的分区，
		 *  遍历所有的分区，查找寄件地址中包含关键字的分区
		 *  获得次分区的定区id
		 *  获得定区的所有快递员
		 */
		// 遍历寄件区域
		Set<SubArea> subareas = _sendArea.getSubareas();
		for (SubArea subArea : subareas) {
			// 查找寄件地址中包含关键字的分区
			if (order.getSendAddress().contains(subArea.getKeyWords())) {
				// 获得此分区所关联的定区id
				FixedArea fixedArea = subArea.getFixedArea();
				// 获得快递员
				Courier courier = fixedArea.getCouriers().iterator().next();
				if (courier != null) {
					System.out.println("关联快递员成功");
					
					// 保存订单
					saveOrder(order, courier);
					
					// 生成工单，给快递员发信息
					saveWorkBillAndSendMessageToCourier(order, courier);
					
					return;
				}
				
			}
			
		}
		
		// 根据辅助关键字
		for (SubArea subArea : subareas) {
			// 查找寄件地址中包含关键字的分区
			if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {
				// 获得此分区所关联的定区id
				FixedArea fixedArea = subArea.getFixedArea();
				// 获得快递员
				Courier courier = fixedArea.getCouriers().iterator().next();
				if (courier != null) {
					System.out.println("关联快递员成功");
					
					// 保存订单
					saveOrder(order, courier);
					
					// 生成工单，给快递员发信息
					saveWorkBillAndSendMessageToCourier(order, courier);
					
					return;
				}
				
			}
			
		}
		
		// 自动分单失败,进入人工分单
		System.out.println("自动分单失败,进入人工分单");
		// 设置订单的分单状态为人工分单
		order.setOrderType("2");
		// 保存订单
		orderDao.save(order);
	

	}

	private void saveWorkBillAndSendMessageToCourier(Order order,
			final Courier courier) {
		// 生成工单，发送短信
		final WorkBill workBill = new WorkBill();
		workBill.setType("新");
		workBill.setPickstate("新单");
		workBill.setBuildtime(new Date());
		workBill.setRemark(order.getRemark());
		
		// 生成短信的序号，设置短信序号
		String smsNumber = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(smsNumber);
		// 设置快递员
		workBill.setCourier(courier);
		// 设置订单
		workBill.setOrder(order);

		// 保存工单
		workBillDao.save(workBill);

		// 调用activeMq给快递员发消息
		jmsTemplate.send("bos_sms_courier", new MessageCreator() {

			@Override
			public Message createMessage(Session session)
					throws JMSException {
				// 获得map集合的消息对象
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone",
						courier.getTelephone());
				mapMessage.setString("msg",
						"短信序号：" + workBill.getSmsNumber() + "寄件人地址："
								+ workBill.getOrder().getSendAddress()
								+ "寄件人电话："
								+ workBill.getOrder().getSendMobile()
								+ "给快递员捎的话 ：" +workBill.getRemark());
				return mapMessage;
			}
		});
		
		// 修改工单状态：已通知
		workBill.setPickstate("已通知");
	}

	private void saveOrder(Order order, final Courier courier) {
		// 设置订单的快递员
		order.setCourier(courier);
		
		// 设置订单的分单状态为自动分单
		order.setOrderType("1");

		// 保存订单
		orderDao.save(order);
	}
	
	/**
	 * @see com.itheima.service.delivery.OrderService#findByOrderNum(java.lang.String)
	 */
	@Override
	public Order findByOrderNum(String orderNum) {
		// 查找指定订单号的订单
		return orderDao.findByOrderNum(orderNum);
	}

}

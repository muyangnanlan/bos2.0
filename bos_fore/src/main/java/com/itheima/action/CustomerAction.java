package com.itheima.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import cn.itcast.crm.domain.Customer;

import com.itheima.domain.constants.Constants;
import com.itheima.utils.MailUtils;

@ParentPackage(value = "json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {

	/** 注入消息生成的模板:一个消息只能被一个消费这使用 */
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsQueueTemplate;

	/**
	 * 发送短信
	 * 
	 * @return
	 */
	@Action(value = "sendCode_customer")
	public String sendCode() {
		// 生成验证码
		String code = RandomStringUtils.randomNumeric(4);
		// 将短信验证码储存到session中
		ServletActionContext.getContext().getSession()
				.put(model.getTelephone(), code);
		System.out.println(code);
		// 编辑短信
		final String msg = "您好，您的短信验证是" + code + "，服务电话为：989878988";

		// 生产消息
		jmsQueueTemplate.send("bos_sms", new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// 使用session创造map集合消息
				MapMessage mapMessage = session.createMapMessage();
				// 消息的键为电话号码
				mapMessage.setString("telephone", model.getTelephone());
				// 消息的值为
				mapMessage.setString("msg", msg);
				return mapMessage;
			}
		});

		return NONE;
	}

	/** 依赖注入 */
	private String code;

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 异步校验手机验证码是否正确
	 */
	@Action(value = "checkCode_customer")
	public String checkCode() {
		// 莫得session中储存的验证码
		String sessionCode = (String) ServletActionContext.getRequest()
				.getSession().getAttribute(model.getTelephone());
		// 定义一个状态码，指定匹配是否成功：0表示不成功，1表示成功
		int statu = 0;
		if (null != sessionCode && sessionCode.equals(code)) {
			statu = 1;
		}

		try {
			ServletActionContext.getResponse().getWriter().println(statu);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/** 注入redistemplate */
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 注册功能
	 * 
	 * @return
	 */
	@Action(value = "regist_customer", results = {
			@Result(name = "success", type = "redirect", location = "signup-success.html"),
			@Result(name = "input", location = "signup.html") })
	public String saveCustomer() {
		// 先验证短信验证码是否正确
		String Sessioncode = (String) ServletActionContext.getContext()
				.getSession().get(model.getTelephone());
		if (Sessioncode == null || !Sessioncode.equals(code)) {
			System.out.println("短信验证码错误");
			return INPUT;
		} else {
			System.out.println("短信验证码正确");
			WebClient
					.create("http://localhost:8088/crm_management/services/customerService/customer")
					.type(MediaType.APPLICATION_JSON).post(model);

			// 客户信息存储到数据库，发送邮件
			// 1.生成激活码
			String activeCode = RandomStringUtils.randomNumeric(32);
			System.out.println(activeCode);

			// 2.将激活码存储到redis(24小时)
			redisTemplate.opsForValue().set(model.getTelephone(), activeCode,
					24, TimeUnit.HOURS);

			// 3.编写邮件内容
			String content = "尊敬的客户您好，请您于24小时之内，点击下面的链接，完成邮件激活功能：<a href='"
					+ MailUtils.getActiveUrl() + "?telephone="
					+ model.getTelephone() + "&activeCode=" + activeCode + "'>"
					+ activeCode + "</a>";

			// 4.发送邮件
			MailUtils.sendMail("黑马程序员", content, model.getEmail());
			return SUCCESS;
		}

	}

	/** 激活码 */
	private String activeCode;

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	/**
	 * 邮件激活
	 */
	@Action(value = "activeMail_customer")
	public String activeMail() {
		// 解决相应到页面的乱码问题
		ServletActionContext.getResponse().setContentType(
				"text/html;charset=utf-8");
		try {
			// 1.通过连接中的电话获得内存中相应的邮件激活码
			String jedisActiveCode = redisTemplate.opsForValue().get(
					model.getTelephone());

			// 2.比较内容和连接传过来的邮件激活码是否正确
			if (jedisActiveCode != null && jedisActiveCode.equals(activeCode)) {
				// 查找指定电话的顾客，判断是否是第一次激活
				Customer customer = WebClient
						.create("http://localhost:8088/crm_management/services/customerService/findCustomerByTelephone/"
								+ model.getTelephone())
						.accept(MediaType.APPLICATION_JSON).get(Customer.class);
				if (customer.getType() == null || customer.getType() != 1) {
					// 没有激活过，激活邮件
					WebClient.create(
							"http://localhost:8088/crm_management/services/customerService/activeCustomer/"
									+ model.getTelephone()).put(null);
					ServletActionContext.getResponse().getWriter()
							.println("邮件已成功激活！");

				} else {
					// 重复激活
					ServletActionContext.getResponse().getWriter()
							.println("您的邮件已经激活，请勿重复激活");
				}

				// 清除内存数据库的值
				// redisTemplate.delete(model.getTelephone());

			} else {
				// System.out.println("激活码错误");
				// 重新生成激活码，重新发送邮件
				// 查找指定电话的顾客，获得邮箱
				Customer customer = WebClient
						.create("http://localhost:8088/crm_management/services/customerService/findCustomerByTelephone/"
								+ model.getTelephone())
						.accept(MediaType.APPLICATION_JSON).get(Customer.class);
				// 1.生成激活码
				String activeCode = RandomStringUtils.randomNumeric(32);
				System.out.println(activeCode);

				// 2.将激活码存储到redis(24小时)
				redisTemplate.opsForValue().set(model.getTelephone(),
						activeCode, 24, TimeUnit.HOURS);

				// 3.编写邮件内容
				String content = "尊敬的客户您好，请您于24小时之内，点击下面的链接，完成邮件激活功能：<a href='"
						+ MailUtils.getActiveUrl() + "?telephone="
						+ model.getTelephone() + "&activeCode=" + activeCode
						+ "'>" + activeCode + "</a>";

				// 4.发送邮件
				MailUtils.sendMail("黑马程序员", content, customer.getEmail());
				ServletActionContext.getResponse().getWriter()
						.println("您的激活码有误，我们已向您的邮箱重新发送了一封新的激活码，请重新查收，激活");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}

	/**
	 * 登录
	 */
	@Action(value = "login_customer", results = {
			@Result(type = "redirect", location = "index.html#/myhome"),
			@Result(name = "error", type = "redirect", location = "login.html") })
	public String login() {
		// 调用crm系统，查找指定电话和密码的用户
		Customer customer = WebClient
				.create(Constants.CRM_MANAGEMENT_URL
						+ "/services/customerService/customer/login?telephone="
						+ model.getTelephone() + "&password="
						+ model.getPassword())
				.accept(MediaType.APPLICATION_JSON).get(Customer.class);
		// 查看customer是否为空
		if (null == customer) {
			// 登录失败
			return ERROR;
		} else {
			// 登录成功
			ServletActionContext.getRequest().getSession()
					.setAttribute("customer", customer);
			System.out.println(customer);
			return SUCCESS;
		}
	}
}

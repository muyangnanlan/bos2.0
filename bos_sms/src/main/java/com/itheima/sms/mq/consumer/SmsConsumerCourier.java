package com.itheima.sms.mq.consumer;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import com.itheima.sms.utils.SmsUtils;

/**
 * 验证码发送平台
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年11月4日
 */
@Service(value = "smsConsumerCourier")
public class SmsConsumerCourier implements MessageListener {

	/**
	 * 调用sms服务发送短信
	 */
	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage = (MapMessage) message;
		// 发送短信
		// String result  = SmsUtils.sendSmsByHTTP(mapMessage.getString("telephone"),
		//		mapMessage.getString("msg"));

		String result = "000/xxx";
		try {
			// 查看是否发送成功
			if (result.startsWith("000")) {
				// 发送成功
				
					System.out.println("发送短信成功，快递员的电话为："
							+ mapMessage.getString("telephone") + "寄件人信息为："
							+ mapMessage.getString("msg"));
				
			} else {
				// 发送失败,将失败的异常抛到页面的错误回掉函数
				throw new RuntimeException("短信发送失败，信息码：+" + result);
			}
		} catch (JMSException e) {
			
			e.printStackTrace();
		}

	}

}

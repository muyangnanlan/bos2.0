package com.itheima.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class MailUtils {
	private static String smtp_host = "smtp.163.com";//邮件供应商
	private static String username = "muyangnanlan@163.com";//邮件地址
	private static String password = "Woshizhuang521";//邮件授权密码
	private static String from = "muyangnanlan@163.com";//邮件发送地址
	private static String activeUrl = "http://localhost:9988/bos_fore/activeMail_customer"; //客户点击邮件访问工程的路径
	
	/**
	 * 获得邮件激活的访问路径
	 * 
	 * @return 邮件激活的访问路径
	 */
	public static String getActiveUrl() {
		return activeUrl;
	}
	
	/**
	 * 发送邮件
	 * 
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @param to 要发送的人
	 */
	public static void sendMail(String subject, String content, String to) {
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtp_host);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.auth", "true");
		Session session = Session.getInstance(props);
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setContent(content, "text/html;charset=utf-8");
			Transport transport = session.getTransport();
			transport.connect(smtp_host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("邮件发送失败...");
		}
	}
/*
	public static void main(String[] args) {
		sendMail("测试邮件", "你好，传智播客", "muyangnanlan@163.com", "98765");
	}*/
}

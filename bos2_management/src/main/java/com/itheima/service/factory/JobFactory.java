package com.itheima.service.factory;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Service;

@Service
public class JobFactory extends AdaptableJobFactory{
	
	@Autowired
	private AutowireCapableBeanFactory autowireCapableBeanFactory;
	
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle)
			throws Exception {
		// 创建继承job的类的对象
		Object jobInstance = super.createJobInstance(bundle);
		// 将对象自动注入
		autowireCapableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
}

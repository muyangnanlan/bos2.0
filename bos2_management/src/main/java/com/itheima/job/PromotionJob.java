package com.itheima.job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.itheima.service.delivery.PromotionService;

/**
 * 宣传活动自动结束
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年11月8日
 */
public class PromotionJob implements Job{
	
	@Autowired
	private PromotionService promotionService;

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// 调用宣传的业务层进行宣传活动的自动过期
		promotionService.updatePromotion(new Date());
		
	}

}

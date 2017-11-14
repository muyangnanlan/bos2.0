package com.itheima.dao.delivery;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.take_delivery.Promotion;

public interface PromotionDao extends JpaRepository<Promotion, Integer>{
	
	/**
	 * 宣传活动的自动过期
	 * 
	 * @param date
	 */
	@Query(value = "update Promotion set status = '2' where endDate < ? and status = '1'")
	@Modifying
	void updatePromotion(Date date);

}

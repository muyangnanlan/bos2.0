package com.itheima.service.delivery;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.domain.page.PageBean;
import com.itheima.domain.take_delivery.Promotion;

public interface PromotionService {
	
	/**
	 * 保存宣传
	 * 
	 * @param model 宣传对象
	 */
	void save(Promotion model);
	
	/**
	 * 分页查找所有的宣传对象
	 * 
	 * @param pageable
	 * @return
	 */
	Page<Promotion> findAll(Pageable pageable);
	
	/**
	 * 前台分页查找宣传对象
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@Path(value = "/promotionFindAll")
	@GET
	@Produces({"application/xml", "application/json"})
	PageBean<Promotion> findAll(@QueryParam("page") int page, @QueryParam("rows") int rows);
	
	/**
	 * 查找指定id的宣传对象
	 * 
	 * @param id
	 * @return
	 */
	@Path(value = "/findPromotionById/{id}")
	@GET
	@Produces({"application/xml", "application/json"})
	Promotion findById(@PathParam("id") Integer id);
	
	/**
	 * 宣传获得自动过期
	 * 
	 * @param date
	 */
	void updatePromotion(Date date);
}

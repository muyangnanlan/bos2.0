package com.itheima.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.domain.base.Courier;

/**
 * 快递员业务层
 * 
 * @author munan
 *
 */
public interface CourierSerevice {
	
	/**
	 * 保存快递员
	 * 
	 * @param courier 快递员对象
	 */
	void save(Courier courier);
	
	/**
	 * 无条件分页查询快递员
	 * 
	 * @param pageable
	 * @return
	 */
	// Page<Courier> findAll(Pageable pageable);
	
	/**
	 * 有条件分页查询快递员
	 * 
	 * @param pageable
	 * @param spec 条件
	 * @return
	 */
	Page<Courier> findAll(Specification<Courier> spec, Pageable pageable);
	
	/**
	 * 标记快递员为作废
	 * 
	 * @param idss
	 */
	void delBatch(String[] idss);
	
	/**
	 * 将快递员标记还原
	 * 
	 * @param arrayIds
	 */
	void restoreCouriers(String[] arrayIds);

}

package com.itheima.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.domain.base.FixedArea;

public interface FixedAreaService {
	
	/**
	 * 保存
	 * 
	 * @param model
	 */
	void save(FixedArea model);
	
	/**
	 * 条件分页查询
	 * 
	 * @param spec
	 * @param pageable
	 * @return
	 */
	Page<FixedArea> findAll(Specification<FixedArea> spec, Pageable pageable);

}

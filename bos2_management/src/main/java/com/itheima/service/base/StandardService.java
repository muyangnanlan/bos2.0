package com.itheima.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.domain.base.Standard;

public interface StandardService {

	void save(Standard standard);

	Page<Standard> findPage(Pageable pageable);

	
	
	/**
	 * 根据id查找
	 * 
	 * @param id
	 * @return 取派标准对象
	 */
	Standard findById(Integer id);
	
	/**
	 * 查找所有的取派标准
	 * 
	 * @return 所有的取派标准的集合
	 */
	List<Standard> findAll();
}

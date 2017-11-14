package com.itheima.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.itheima.domain.base.SubArea;

/**
 * 分区的业务层
 * 
 * @author munan
 *
 */
@Service
public interface SubAreaService {
	
	/**
	 * 保存对象
	 * 
	 * @param model
	 */
	void save(SubArea model);
	
	/**
	 * 条件分页查询
	 * 
	 * @param sepc
	 * @param pageable
	 * @return
	 */
	Page<SubArea> PageFind(Specification<SubArea> sepc, Pageable pageable);
	
	/**
	 * 保存集合数据
	 * 
	 * @param subAreas
	 */
	void save(List<SubArea> subAreas);
	
}

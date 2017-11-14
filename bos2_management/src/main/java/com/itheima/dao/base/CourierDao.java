package com.itheima.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.base.Courier;

public interface CourierDao extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier>{
	
	/**
	 * 将指定id的快递员作废
	 * 
	 * @param id
	 */
	@Query("update Courier set deltag = '1' where id = ?")
	@Modifying
	public void delBatch(Integer id);
	
}

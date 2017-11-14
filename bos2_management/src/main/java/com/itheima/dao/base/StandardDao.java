package com.itheima.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.base.Standard;
/**
 * 此dao继承JpaRepository
 * 
 * @author munan
 *
 */
/**
 * 此dao继承JpaRepository
 * 
 * @author munan
 *
 */
public interface StandardDao extends JpaRepository<Standard, Integer>{
	
	/*// 根据单列名称查询:findBy后面的Name必须是实体类的属性名称的首字母大写
	public List<Standard> findByName(String name);
	
	// 根据双列查询
	public List<Standard> findByNameAndMinWeight(String name, Integer minWeight);
	
	// 根据双列查询：自己命名的方法
	@Query(value="from Standard where minWeight = ?2 and name = ?1")
	public List<Standard> queryName(String name, Integer minWeight);
	
	// 根据双列查询：自己命名的方法
	@Query
	public List<Standard> queryName2(String name, Integer minWeight);
	
	// 带有条件的修改操作
	@Query(value="from Standard set minWeight = ?2 where name = ?1", nativeQuery = false)
	@Modifying // 标记修改，删除操作
	public void updateStandard(String name, Integer minWeight);
*/
}

package com.itheima.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.base.Area;

public interface AreaDao extends JpaRepository<Area, String>, JpaSpecificationExecutor<Area>{
	
	/**
	 * 获得所有的省
	 * 
	 * @return
	 */
	@Query(value="select distinct a.province from Area a")
	List<String> findProvinces();
	
	/**
	 * 获得指定省份的所有的城市
	 * 
	 * @return
	 */
	@Query(value="select distinct a.city from Area a where province = ?")
	List<String> findCities(String province);
	
	/**
	 * 获得指定省份，城市的所有的区县
	 * 
	 * @param province
	 * @param city
	 * @return
	 */
	@Query(value="select distinct a.district from Area a where province = ? and city = ?")
	List<String> findDistricts(String province, String city);
	
	/**
	 * 获得指定省市区的区域
	 * 
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 */
	Area findByProvinceAndCityAndDistrict(String province, String city,
			String district);

}

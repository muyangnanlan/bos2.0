package com.itheima.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.domain.base.Area;

/**
 * 区域的业务层接口
 * 
 * @author munan
 *
 */
public interface AreaService {
	
	/**
	 * 保存集合数据
	 * 
	 * @param areas
	 */
	void saveData(List<Area> areas);
	
	/**
	 * 查找所有的数据
	 * 
	 * @return
	 */
	List<Area> findAll();
	
	/**
	 * 条件分页查询
	 * 
	 * @param spec 条件对象
	 * @param pageable 页面对象
	 * @return 集合
	 */
	Page<Area> area_findPage(Specification<Area> spec, Pageable pageable);
	
	/**
	 * 获得所有的省份
	 * 
	 * @return 省份的list集合
	 */
	List<String> findProvinces();
	
	/**
	 * 获得指定城市的所有省份
	 * 
	 * @return 城市的list集合
	 */
	List<String> findCities(String province);
	
	/**
	 * 获得指定省份，城市的所有的区县
	 * 
	 * @param province
	 * @param city
	 * @return
	 */
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

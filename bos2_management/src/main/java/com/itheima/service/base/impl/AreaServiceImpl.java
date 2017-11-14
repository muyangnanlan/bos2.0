package com.itheima.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.itheima.dao.base.AreaDao;
import com.itheima.domain.base.Area;
import com.itheima.service.base.AreaService;

/**
 * 区域的业务层实现类
 * 
 * @author munan
 *
 */
@Service
public class AreaServiceImpl implements AreaService {
	/** 注入业务层对象  */
	@Autowired
	private AreaDao areaDao;
	
	/**
	 * @see com.itheima.service.base.AreaService#saveData(java.util.List)
	 */
	@Override
	public void saveData(List<Area> areas) {
		areaDao.save(areas);
	}
	
	/**
	 * @see com.itheima.service.base.AreaService#findAll()
	 */
	@Override
	public List<Area> findAll() {
		return areaDao.findAll();
	}
	
	/**
	 * @see com.itheima.service.base.AreaService#area_findPage(org.springframework.data.jpa.domain.Specification, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Area> area_findPage(Specification<Area> spec, Pageable pageable) {
		return areaDao.findAll(spec, pageable);
	}
	
	/**
	 * @see com.itheima.service.base.AreaService#findProvinces()
	 */
	@Override
	public List<String> findProvinces() {
		// 调用数据访问层查找所有的省
		return areaDao.findProvinces();
	}
	
	/*
	 * @see com.itheima.service.base.AreaService#findCities()
	 */
	@Override
	public List<String> findCities(String province) {
		// 调用数据访问层查找指省份的所有的城市
		return areaDao.findCities(province);
	}
	
	/*
	 * @see com.itheima.service.base.AreaService#findDistricts(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> findDistricts(String province, String city) {
		// 调用数据访问层查找指定省份，城市的所有的区县
		return areaDao.findDistricts(province, city);
	}
	
	/**
	 * @see com.itheima.service.base.AreaService#findByProvinceAndCityAndDistrict(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Area findByProvinceAndCityAndDistrict(String province,
			String city, String district) {
		// 调用数据访问层查找指定省市区的对象
		return areaDao.findByProvinceAndCityAndDistrict(province, city, district);
	}

}

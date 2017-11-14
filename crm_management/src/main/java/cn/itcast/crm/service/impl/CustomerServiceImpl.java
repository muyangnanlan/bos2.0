package cn.itcast.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerDao;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{
	/** 注入DAO层*/
	@Autowired
	private CustomerDao customerDao;
	
	/**
	 * @see cn.itcast.crm.service.CustomerService#getNoAssociationCustomer()
	 */
	@Override
	public List<Customer> getNoAssociationCustomer() {
		return customerDao.findByFixedAreaIdIsNull();
	}
	
	/**
	 * @see cn.itcast.crm.service.CustomerService#getHasAssociationCustomer(java.lang.String)
	 */
	@Override
	public List<Customer> getHasAssociationCustomer(String fixedAreaId) {
		return customerDao.findByFixedAreaId(fixedAreaId);
	}
	
	/**
	 * @see cn.itcast.crm.service.CustomerService#associationCustomerToFixedArea(java.lang.String, java.lang.String)
	 */
	@Override
	public void associationCustomerToFixedArea(String customerId,
			String fixedAreaId) {
		// 将已关联的客户id全部取消
		customerDao.clearAssociationFixedAreaId(fixedAreaId);
		
		// 判断customerId是否为空(此定区id没有关联的客户，上面已经将所有关联的客户清楚)
		if (StringUtils.isBlank(customerId) || "null".equals(customerId)) {
			return;
		}
		
		// 获得所有单个的客户id
		System.out.println(customerId);
		String[] ids = customerId.split(",");
		for (String id : ids) {
			System.out.println(id);
			Integer idd = Integer.parseInt(id);
			// 查找指定id的客户
			Customer c = customerDao.findOne(idd);
			// 关联区域id
			c.setFixedAreaId(fixedAreaId);
		}
	}

	@Override
	public void saveCustomer(Customer customer) {
		customerDao.save(customer);
	}
	
	/**
	 * @see cn.itcast.crm.service.CustomerService#findByTelephone(java.lang.String)
	 */
	@Override
	public Customer findByTelephone(String telephone) {
		return customerDao.findByTelephone(telephone);
	}
	
	/**
	 * @see cn.itcast.crm.service.CustomerService#activeCustomer(java.lang.String)
	 */
	@Override
	public void activeCustomer(String telephone) {
		customerDao.updateCustomerbyType(telephone);
	}
	
	/**
	 * @see cn.itcast.crm.service.CustomerService#login(java.lang.String, java.lang.String)
	 */
	@Override
	public Customer login(String telephone, String password) {
		System.out.println(telephone);
		System.out.println(password);
		return customerDao.findByTelephoneAndPassword(telephone, password);
	}
	
	/**
	 * @see cn.itcast.crm.service.CustomerService#findFixedAreaIdByCustomerId(java.lang.String)
	 */
	@Override
	public String findFixedAreaIdBySendAddress(String sendAddress) {
		return customerDao.findFixedAreaIdBySendAddress(sendAddress);
	}

}

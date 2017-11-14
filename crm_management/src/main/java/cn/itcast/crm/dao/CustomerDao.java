package cn.itcast.crm.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

public interface CustomerDao extends JpaRepository<Customer, Integer>{
	
	/**
	 * 查找未关联定区的客户
	 * 
	 * @return
	 */
	List<Customer> findByFixedAreaIdIsNull();
	
	/**
	 * 查询所有已关联定区的客户
	 * 
	 * @param fixedAreaId
	 * @return
	 */
	List<Customer> findByFixedAreaId(String fixedAreaId);
	
	/**
	 * 根据指定电话查找用户
	 * 
	 * @param telephone
	 * @return
	 */
	Customer findByTelephone(String telephone);
	
	/**
	 * 激活邮件功能
	 * 
	 * @param telephone
	 */
	@Query(value = "update Customer set type = 1 where telephone = ?")
	@Modifying
	void updateCustomerbyType(String telephone);
	
	/**
	 * 清楚关联定区id
	 * 
	 * @param fixedAreaId
	 */
	@Query(value = "update Customer set fixedAreaId = null where fixedAreaId = ?")
	@Modifying
	void clearAssociationFixedAreaId(String fixedAreaId);
	
	/**
	 * 查找指定电话和密码的用户
	 * 
	 * @param telephone
	 * @param password
	 * @return
	 */
	Customer findByTelephoneAndPassword(String telephone, String password);
	
	/**
	 * 根据用户id查找关联定区id
	 * 
	 * @param id
	 * @return
	 */
	@Query(value = "select fixedAreaId from Customer where address = ?")
	String findFixedAreaIdBySendAddress(String sendAddress);

}

package cn.itcast.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.crm.service.CustomerService;

/**
 * crm服务器测试
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年11月1日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CustomerServiceTest {
	@Autowired
	private CustomerService customerService;

	@Test
	public void testGetNoAssociationCustomer() {
		System.out.println(customerService.getNoAssociationCustomer());
	}

	@Test
	public void testGetHasAssociationCustomer() {
		System.out.println(customerService.getHasAssociationCustomer("dq001"));
	}

	@Test
	public void testAssociationCustomerToFixedArea() {
		fail("Not yet implemented");
	}

}

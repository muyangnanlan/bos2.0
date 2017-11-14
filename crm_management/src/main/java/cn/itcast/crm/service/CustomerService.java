package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

/**
 * 客户业务层：cxf
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年10月31日
 */
public interface CustomerService {
	/**
	 * 查询所有未关联定区的客户
	 */
	@Path(value = "/noassociationcustomer")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> getNoAssociationCustomer();

	/**
	 * 查询所有已关联的定区客户
	 */
	@Path(value = "/hasassociationcustomer/{fixedAreaId}")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> getHasAssociationCustomer(
			@PathParam("fixedAreaId") String fixedAreaId);

	/**
	 * 将客户关联到定区上
	 * 将所有客户id用,连接起来生成新的字符串
	 */
	@Path(value = "associationcustomertofixedarea")
	@PUT
	public void associationCustomerToFixedArea(
			@QueryParam("customerIds") String customerId,
			@QueryParam("fixedAreaId") String fixedAreaId);
	
	/**
	 * 保存客户
	 */
	@Path(value = "customer")
	@POST
	@Consumes({"application/xml", "application/json"})
	public void saveCustomer(Customer customer);
	
	/**
	 * 根据电话号码查询客户
	 */
	@Path(value = "findCustomerByTelephone/{telephone}")
	@GET
	@Produces({ "application/xml", "application/json" })
	public Customer findByTelephone(@PathParam("telephone") String telephone) ;
	
	/**
	 * 激活邮件功能
	 */
	@Path(value = "activeCustomer/{telephone}")
	@PUT
	public void activeCustomer(@PathParam("telephone") String telephone);
	
	/**
	 * 用户登录
	 */
	@Path(value = "/customer/login")
	@GET
	@Produces({"application/xml", "application/json"})
	public Customer login(@QueryParam("telephone") String telephone, @QueryParam("password") String password);
	
	/**
	 * 根据客户id查找客户所关联的定区id
	 */
	@Path(value = "/findFixedAreaIdBySendAddress")
	@GET
	@Produces({"application/xml", "application/json"})
	public String findFixedAreaIdBySendAddress(@QueryParam("sendAddress") String sendAddress);
}

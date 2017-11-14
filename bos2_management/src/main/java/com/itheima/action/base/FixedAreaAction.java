package com.itheima.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.crm.domain.Customer;

import com.itheima.action.commen.BaseAction;
import com.itheima.domain.base.FixedArea;
import com.itheima.service.base.FixedAreaService;
import com.opensymphony.xwork2.ActionContext;

/**
 * 定区控制层
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年10月30日
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {
	/** 注入业务层对象 */
	@Autowired
	private FixedAreaService fixedAreaService;

	/**
	 * 保存快递员
	 */
	@Action(value = "fixedArea_save", results = @Result(type = "redirect", location = "./pages/base/fixed_area.html"))
	public String save() {
		// 调用业务层保存数据
		fixedAreaService.save(model);
		return SUCCESS;
	}

	/**
	 * 条件分页查询
	 */
	@Action(value = "fixedArea_pageFind", results = @Result(type = "json"))
	public String pageFind() {
		// 创建pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);

		// 创建条件对象
		Specification<FixedArea> spec = new Specification<FixedArea>() {

			@Override
			public Predicate toPredicate(Root<FixedArea> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 创建list集合保存条件对象
				List<Predicate> preds = new ArrayList<Predicate>();

				// 定区编码条件
				if (StringUtils.isNotBlank(model.getId())) {
					Predicate p1 = cb.like(root.get("id").as(String.class), "%"
							+ model.getId() + "%");
					preds.add(p1);
				}

				// 所属公司条件
				if (StringUtils.isNotBlank(model.getCompany())) {
					Predicate p2 = cb.like(
							root.get("company").as(String.class),
							"%" + model.getCompany() + "%");
					preds.add(p2);
				}
				return cb.and(preds.toArray(new Predicate[0]));
			}

		};

		// 调用业务层对象查找
		Page<FixedArea> page = fixedAreaService.findAll(spec, pageable);

		// 将数据压入栈顶
		pushStack(page);

		return SUCCESS;
	}

	/**
	 * 查找未关联定区的客户
	 */
	@Action(value = "findnoassociationcustomer_fixedArea", results = @Result(type = "json"))
	public String findNoAssociationCustomer() {
		// 使用webService查找未管理客户的id
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:8088/crm_management/services/customerService/noassociationcustomer")
				.accept(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		System.out.println(collection);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}

	/**
	 * 查找已关联定区的客户
	 */
	@Action(value = "findhasassociationcustomer_fixedArea", results = @Result(type = "json"))
	public String findHasAssociationCustomer() {
		// 使用webService查找未管理客户的id
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:8088/crm_management/services/customerService/hasassociationcustomer/"
						+ model.getId()).accept(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		System.out.println(collection);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}

	/** 关联客户的所有的id */
	private String[] customerIds;

	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	/**
	 * 关联客户
	 */
	@Action(value = "decidedzone_assigncustomerstodecidedzone", results = { @Result(type = "redirect", location = "./pages/base/fixed_area.html") })
	public String associationCustomersToFixedArea() {
		// 将所有的id用,号连接
		String idsStr = StringUtils.join(customerIds, ",");
		System.out.println("idsStr:" + idsStr);
		System.out.println(model.getId());
		// 调用webservice关联客户
		WebClient
				.create("http://localhost:8088/crm_management/services/customerService/associationcustomertofixedarea?fixedAreaId="
						+ model.getId() + "&customerIds=" + idsStr).put(null);
		return SUCCESS;
	}
}

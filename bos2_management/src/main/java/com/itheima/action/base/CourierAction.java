package com.itheima.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
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

import com.itheima.domain.base.Courier;
import com.itheima.domain.base.Standard;
import com.itheima.service.base.CourierSerevice;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 快递员的action
 * 
 * @author munan
 *
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier>{
	// 注入CourierSerevice属性
	@Autowired
	private CourierSerevice courierSerevice;
	
	// 模型驱动
	private Courier courier = new Courier();
	@Override
	public Courier getModel() {
		// TODO Auto-generated method stub
		return courier;
	}
	
	/**
	 * 保存快递员
	 */
	@Action(value = "save_courier", results = {@Result(type="redirect", location = "./pages/base/courier.html")})
	public String save() {
		courierSerevice.save(courier);
		return SUCCESS;
	}
	
	// 属性驱动
	/** 当前页  */
	private int page;
	/** 每页显示条数  */
	private int rows;
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * 无条件分页查询快递员
	 */
	/*@Action(value="findPage_courier", results = {@Result(type="json")})
	public String findPage() {
		Pageable pageable = new PageRequest(page-1, rows);
		// 查找当前页的数据
		Page<Courier> couriers = courierSerevice.findAll(pageable);
		// 创建Map集合
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", couriers.getTotalElements());
		map.put("rows", couriers.getContent());
		// 将map压入栈顶
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}*/
	
	/**
	 * 有条件查询快递员
	 */
	@Action(value="findPage_courier", results = {@Result(type="json")})
	public String findPage() {
		
		// 条件对象
		Specification<Courier> spec = new Specification<Courier>(){
			
			/**
			 * Predicate: 相当于条件
			 * Root ：当前对象，相当于courier, 用于构建where courierNum = ?
			 */
			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				// 单表查询的条件
				/*
				 *  根据工号查询 : where courierNum = ?  ?=courier.getCourierNum()
				 */
				if (StringUtils.isNotBlank(courier.getCourierNum())) {
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					list.add(p1);
				}
				
				/*
				 * 根据所受单位查询： where company = ? ?="%" + courier.getCompany() + "%"
				 */
				if (StringUtils.isNotBlank(courier.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), "%" + courier.getCompany() + "%");
					list.add(p2);
				}
				
				/*
				 * 根据快递员类型查询 ： where type = ? ?=courier.getType()
				 */
				if (StringUtils.isNotBlank(courier.getType())) {
					Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
					list.add(p3);
				}
				
				// 多表查询
				/*
				 * 根据快递员的取件标准
				 * 构建取件标准对象(通过关联对象，内链接)
				 * standardRoot : 相当于standard对象
				 */
				Join<Courier, Standard> standardRoot = root.join("standard",JoinType.INNER);
				if (courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())) {
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class), "%" + courier.getStandard().getName() + "%");
					list.add(p4);
				}
				
				// new Predicate[0]指定list集合转换成的数组的类型
				return cb.and(list.toArray(new Predicate[0]));
			}	
		};
		System.out.println(courier.getCompany());
		System.out.println(courier.getCourierNum());
		// 分页对象
		Pageable pageable = new PageRequest(page-1, rows);
		// 查找当前页的数据
		Page<Courier> couriers = courierSerevice.findAll(spec, pageable);
		// 创建Map集合
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", couriers.getTotalElements());
		map.put("rows", couriers.getContent());
		System.out.println(map);
		// 将map压入栈顶
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	
	}
	
	// 属性驱动
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * 快递员标记作废的功能
	 */
	@Action(value = "del_couriers", results = {@Result(type = "redirect", location = "./pages/base/courier.html")})
	public String delCouriers() {
		// 将ids转化为数组
		String[] idss = ids.split(",");
		// 调用业务层标记作废
		courierSerevice.delBatch(idss);
		return SUCCESS;
	}
	
	/**
	 * 快递员还原功能
	 */
	@Action(value = "restore_couriers", results = {@Result(type="redirect", location = "./pages/base/courier.html")})
	public String restoreCouriers() {
		// 接受页面传来的ids,将其转化成数组
		String[] arrayIds = ids.split(",");
		System.out.println(arrayIds);
		// 调用业务层标记还原
		courierSerevice.restoreCouriers(arrayIds);
		return SUCCESS;
	}
}	

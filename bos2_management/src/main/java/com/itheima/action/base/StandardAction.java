package com.itheima.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.itheima.domain.base.Standard;
import com.itheima.service.base.StandardService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 收派标准的Action
 * 
 * @author munan
 *
 */
// 实例化对象
@Controller
// 多例
@Scope("prototype")
@ParentPackage("json-default")
@Namespace("/")
public class StandardAction extends ActionSupport implements ModelDriven<Standard>{
	// 注入StandardService
	@Autowired
	private StandardService standardService;
	
	/** 模型对象：取派标准  */
	private Standard standard = new Standard();

	@Override
	public Standard getModel() {
		return standard;
	}
	
	// 保存取派标准
	@Action(value="save_standard", results = {@Result(name = "success", type="redirect", location = "./pages/base/standard.html")})
	public String save() {
		standardService.save(standard);
		return SUCCESS;
	}
	
	// 分页查找的属性驱动
	private int page;
	private int rows;
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	// 分页查找
	@Action(value = "findPage_standard", results = {@Result(name = "success", type="json")})
	public String findPage() {
		/*
		 *  分页查找所有的数据:
		 *  PageRequest要求page从0开始
		 */
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Standard> standards = standardService.findPage(pageable);
		// 建立map集合
		Map<String, Object> map = new HashMap<String, Object>();
		// 添加总记录数
		map.put("total", standards.getTotalElements());
		// 添加当前页的内容
		map.put("rows", standards.getContent());
		// 将map集合压入栈顶
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	// 根据id查找
	@Action(value = "findById")
	public String findById() {
		// 解决返回的数据中文乱码问题
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		// 根据id查找取派标准
		Integer id = standard.getId();
		System.out.println(id);
		Standard exist_standard = null;
		if (id != null) {
			exist_standard = standardService.findById(id);
		}
		if (exist_standard != null) {
			Object json = JSONObject.toJSON(exist_standard);
			try {
				ServletActionContext.getResponse().getWriter().println(json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	// 查找所有的取派标准
	@Action(value = "finaAll_standard", results = {@Result(name = "success", type="json")})
	public String findAll() {
		// 查找所有的取派标准
		List<Standard> standards = standardService.findAll();
		// 将集合压入栈顶
		ActionContext.getContext().getValueStack().push(standards);
		return SUCCESS;
	}

}

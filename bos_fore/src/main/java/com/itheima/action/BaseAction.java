package com.itheima.action;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * action的代码重构优化
 * 
 * @author munan
 *
 * @param <T>
 */
public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	/**
	 * 模型驱动的抽取 当被protected修饰，只允许子类使用
	 * 当spring容器对继承BaseAction<T>的类extendsA进行初始化的时候，T是确定的，
	 * 执行的是extendsA的空参构造，而空参构造的第一行必须执行父类的空参构造，就会执行 public
	 * BaseAction(){}这个空参构造，我们在这个父类的空参构造里面对model进行 初始化，也就会被执行。
	 */
	// 模型驱动
	protected T model;

	@Override
	public T getModel() {
		return model;
	}

	/**
	 * 构造器完成model的初始化
	 */
	public BaseAction() {
		// 1.获得带有泛型的父类型:this谁调用我，我就指代谁
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		// 2.获得第一个泛型类型
		Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
		// 3.初始化泛型类型
		try {
			model = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("模型初始化失败...");
		}
	}

	/** 当前页面 */
	protected int page;

	/** 每页显示行数 */
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * 对分页查询的压栈进行抽取
	 */
	protected void pushStack(Page<T> page) {
		// 创建map集合
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());
		// 将map压入值栈的顶部
		ActionContext.getContext().getValueStack().push(map);
	}

}

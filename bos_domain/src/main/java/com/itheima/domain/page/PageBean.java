package com.itheima.domain.page;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.itheima.domain.take_delivery.Promotion;

/**
 * 自定义分页数据封装对象:
 * 		为什么我们不用spring data jpa的page对象，而是自己写一个？
 * 			因为spring data jpa的page类中没有setter方法，当bos2_manager返回数据，给
 * 			bos_fore这个系统中时，数据格式只有xml和json这两种格式。接收数据的这一端的get方法中
 * 			可以将返回的数据封装到指定的类型，这个类型必须要有setter方法。
 * 			而我们给定的page，没有setter方法，所以不能解析封装，所以我们要自己封装一个有setter方法
 * 			的pageBean类
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年11月6日
 */
@XmlRootElement(name = "PageBean")//此注解表示可以被cxf封装的类
@XmlSeeAlso(value = { Promotion.class })//表示此pagebean中的泛型可以封装的数据
public class PageBean<T> {
	/** 总记录数   */
	private long totalCount;
	
	/** 返回到页面的数据   */
	private List<T> pageData;
	
	public long getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	public List<T> getPageData() {
		return pageData;
	}
	
	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	} 

}

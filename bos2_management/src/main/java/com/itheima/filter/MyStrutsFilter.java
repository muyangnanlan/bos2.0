package com.itheima.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

/**
 * 一个让struts不拦截webservice cxf的/services的路径的拦截器
 * 
 * @author 闫惠甜娇
 * @version 1.0，2017年11月6日
 */
public class MyStrutsFilter extends StrutsPrepareAndExecuteFilter{
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		// 将servletRequest转换成http的ServletRequest
		HttpServletRequest request = (HttpServletRequest) req;
		
		// 获得访问路径
		String requestURI = request.getRequestURI();
		
		// 如果这个路径中包含/services/则放行，也就是struts不会拦截
		if (requestURI.contains("/services/")) {
			chain.doFilter(req, res);
			// 过滤器执行完成后，返回的时候，还是会经过这个过滤器，所以要return结束掉回来的执行数据。
			return ;
		}
		// 不包含/services/依旧执行父类的拦截，也就时继续会被struts拦截
		super.doFilter(req, res, chain);
	}
}

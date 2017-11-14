package com.itheima.SpringDataJPAQueryAnnoTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 如何获得注解上的内容
 * @author munan
 *
 */
public class QueryTest {
	public static void main(String[] args) {
		Query query = (Query) Proxy.newProxyInstance(Query.class.getClassLoader(), new Class[]{Query.class}, new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				// 获得这个方法的指定的注解对象
				MyQuery myQuery = method.getAnnotation(MyQuery.class);
				// 获得这个注解对象指定方法的内容
				String name = myQuery.name();
				System.out.println(name);
				
				return null;
			}
		});
		
		query.save();
	}
}

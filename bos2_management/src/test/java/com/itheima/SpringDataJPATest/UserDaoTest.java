package com.itheima.SpringDataJPATest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class UserDaoTest {
	public static void main(String[] args) {
		// 1.生成userDao这个接口的代理对象
		UserDao userDao = (UserDao) Proxy.newProxyInstance(UserDao.class.getClassLoader(), new Class[]{UserDao.class}, new InvocationHandler(){

			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				JPARepositoryImpl japImpl = new JPARepositoryImpl();
				// 调用实现类的方法
				method.invoke(japImpl, args);
				return null;
			}
			
		});
		
		// 调用方法
		userDao.save();
	}
}

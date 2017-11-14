package com.itheima.SpringDataJPATest;

public class JPARepositoryImpl implements JPARepository {

	@Override
	public void save() {
		System.out.println("save 方法执行了...");
	}

}

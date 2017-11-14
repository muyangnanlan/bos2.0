package com.itheima.SpringDataJPAQueryAnnoTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个注解
 * 
 * @author munan
 *
 */
/*
 *  表示这个注解可以再类的什么地方加， type表示可以加载类，接口等上， 
 *  	Method表示可以加在方法上（具体参照ElementType类）
 */
@Target(value = {ElementType.METHOD})
/*
 * @Retention表示这个注解生么时候生效
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MyQuery {
	String name();
}

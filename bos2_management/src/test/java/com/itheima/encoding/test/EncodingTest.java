package com.itheima.encoding.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Test;

/**
 * url编码
 * 
 * @author munan
 *
 */
public class EncodingTest {
	
	@Test
	public void Test01() throws UnsupportedEncodingException {
		// 用指定的字符集生成url编码
		String name = "张三";
		name = URLEncoder.encode(name, "utf-8");
		System.out.println(name);
		
		// 用相同的字符集进行url解码
		name = URLDecoder.decode(name, "utf-8");
		System.out.println(name);
		
		System.out.println(name.length());
		
	}
}

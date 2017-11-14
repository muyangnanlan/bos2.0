package freemaker.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * freemaker的helloword
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年11月6日
 */
public class FreeMakerHello {

	@Test
	public void test01() throws IOException, TemplateException {
		// 创建freemaker的配置对象
		Configuration configuration = new Configuration(
				Configuration.VERSION_2_3_22);

		// 设置模板文件路径
		configuration.setDirectoryForTemplateLoading(new File(
				"src/main/webapp/WEB-INF/freemaker_template"));

		// 获取模板对象
		Template template = configuration.getTemplate("hello.ftl");

		// 动态数据对象
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "helloword");
		map.put("msg", "hello freemaker....");
		
		// 合并输出
		template.process(map, new PrintWriter(System.out));
	}
}

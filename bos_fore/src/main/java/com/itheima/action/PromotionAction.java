package com.itheima.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.domain.constants.Constants;
import com.itheima.domain.page.PageBean;
import com.itheima.domain.take_delivery.Promotion;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class PromotionAction extends BaseAction<Promotion> {

	/**
	 * 前台宣传活动分页数据查询
	 */
	@Action(value = "findPage_promotion", results = @Result(type = "json"))
	public String findPage() {
		System.out.println("1");
		// 使用webservice在后台系统查找指定页的数据
		PageBean<Promotion> pageBean = WebClient
				.create("http://localhost:8080/bos2_management/services/promotionService/promotionFindAll?page="
						+ page + "&rows=" + rows)
				.accept(MediaType.APPLICATION_JSON).get(PageBean.class);

		// 将pageBean对象压入到值栈顶部
		ServletActionContext.getContext().getValueStack().push(pageBean);
		return SUCCESS;
	}
	
	/**
	 * 获得宣传活动详情页面
	 */
	@Action(value = "detail_promotion")
	public String showPromotionDetails() throws IOException, TemplateException {
		// 获得id对应的html页面的路径的文件对象
		String realPath = ServletActionContext.getServletContext().getRealPath(
				"/freemaker");
		File file = new File(realPath, model.getId() + ".html");
		System.out.println("2");
		// 判断id所对应的html页面是否存在
		if (!file.exists()) {
			// 页面不存在
			
			// 创建freemaker的配置对象
			Configuration configuration = new Configuration(
					Configuration.VERSION_2_3_22);

			// 设置模板文件路径
			configuration.setDirectoryForTemplateLoading(new File(
					ServletActionContext.getServletContext().getRealPath(
							"/WEB-INF/freemaker_template")));

			// 获得模板
			Template template = configuration
					.getTemplate("promotion_detail.ftl");
			
			// 查询指定id的宣传对象（动态数据对象）
			Promotion promotion = WebClient
					.create(Constants.BOS_MANAGEMENT_HOST
							+ "/bos2_management/services/promotionService/findPromotionById/"
							+ model.getId()).accept(MediaType.APPLICATION_JSON)
					.get(Promotion.class);
			Map<String, Object> htmlMap = new HashMap<String, Object>();
			htmlMap.put("promotion", promotion);
			System.out.println(promotion);
			// 合并输出到指定文件（id所对应的文件）
			template.process(htmlMap, new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
		}
		
		// 存在，直接将数据相应回去
		// 设置浏览器打开文件的编码格式
		ServletActionContext.getResponse().setContentType("html/text;charset=utf-8");
		FileUtils.copyFile(file, ServletActionContext.getResponse().getOutputStream());
		return NONE;
	}
}

package com.itheima.action.delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.action.commen.BaseAction;
import com.itheima.domain.take_delivery.Promotion;
import com.itheima.service.delivery.PromotionService;

/**
 * 宣传的action
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年11月5日
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {
	/** 注入宣传的业务层   */
	@Autowired
	private PromotionService promotionService;
	
	/** 上传的文件对象   */
	private File titleImgFile;
	
	/** 上传的文件名称 */
	private String titleImgFileFileName;
	
	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	@Action(value = "save_promotion", results = @Result(type = "redirect", location = "./pages/take_delivery/promotion.html"))
	public String savePromotion() {
		
		// 设置文件上传到的绝对路径
		String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
		
		// 设置文件的项目访问路径，也就是数据库中存储的路径
		String accessPath = ServletActionContext.getRequest().getContextPath() + "/upload/";
		
		// 使用uuid获得一个随机的图片名
		UUID uuid = UUID.randomUUID();
		System.out.println(titleImgFileFileName);
		String ImgFileName = uuid + titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		
		// 上传图片
		try {
			FileUtils.copyFile(titleImgFile, new File(realPath, ImgFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 给宣传对象设置图片的访问路径
		model.setTitleImg(accessPath+ImgFileName);
		model.setStatus("1");
		
		// 保存宣传路径
		promotionService.save(model);
		return SUCCESS;
	}
	
	/**
	 * 查找所有分页
	 */
	@Action(value = "findAll_promotion", results = @Result(type = "json"))
	public String findAllPromotion() {
		// 创建页面对象
		Pageable pageable = new PageRequest(page - 1, rows);
		
		// 调用业务层查找所有
		Page<Promotion> page = promotionService.findAll(pageable);
		
		// 将数据压入栈
		pushStack(page);
		return SUCCESS;
	}
	
}

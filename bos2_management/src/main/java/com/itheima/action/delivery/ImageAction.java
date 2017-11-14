package com.itheima.action.delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.action.commen.BaseAction;
import com.itheima.domain.take_delivery.Promotion;
import com.opensymphony.xwork2.ActionContext;

/**
 * kindeditor图片上传和图片管理
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年11月5日
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ImageAction extends BaseAction<Promotion> {

	/** 文件对象 */
	private File imgFile;

	/** 文件名称 */
	private String imgFileFileName;

	/** 文件的类型 */
	private String imgFileContentType;

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}

	/**
	 * editor的图片上传
	 */
	@Action(value = "imageUpload_promotion", results = @Result(type = "json"))
	public String imageUpload() {
		// 文件上传路径的绝对路径
		String realPath = ServletActionContext.getServletContext().getRealPath(
				"/upload");
		// 获得上传文件的相对路径(访问路径)
		String saveUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";

		// 生成随机图片名
		UUID name = UUID.randomUUID();
		String filename = name
				+ imgFileFileName.substring(imgFileFileName.lastIndexOf("."));

		// 上传文件
		try {
			FileUtils.copyFile(imgFile, new File(realPath, filename));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 通知浏览器上传成功
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", 0);
		map.put("url", saveUrl + filename);

		// 将map数据压入栈顶
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}

	/**
	 * editor图片管理（加载所有的图片）
	 */
	@Action(value = "imageManager_promotion", results = { @Result(type = "json") })
	public String imageManager() {
		// 获得要展示的图片文件夹的根目录的真是路径
		String rootPath = ServletActionContext.getServletContext().getRealPath(
				"/upload/");

		// 获得要展示的图片文件夹的项目的访问路径
		String rootUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";
		
		// 遍历目录文件的信息存储的集合
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
		
		//图片扩展名
		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};
		
		// 当前文件对象
		File currentPathFile = new File(rootPath);
		
		// 遍历当前文件对象（根路径）
		for (File file : currentPathFile.listFiles()) {
			// 创建一个map集合，将每一个文件的信息储存到这个集合中
			Map<String, Object> hash = new HashMap<String, Object>();
			String fileName = file.getName();
			if(file.isDirectory()) {
				hash.put("is_dir", true);
				hash.put("has_file", (file.listFiles() != null));
				hash.put("filesize", 0L);
				hash.put("is_photo", false);
				hash.put("filetype", "");
			} else if(file.isFile()){
				String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				hash.put("is_dir", false);
				hash.put("has_file", false);
				hash.put("filesize", file.length());
				hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
				hash.put("filetype", fileExt);
			}
			hash.put("filename", fileName);
			hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
			// 将每一个文件信息map集合添加到list集合中
			fileList.add(hash);
		}
		
		// 将包含所有文件的信息的集合封装到一个集合中
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		
		// 将这个集合压入栈
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}

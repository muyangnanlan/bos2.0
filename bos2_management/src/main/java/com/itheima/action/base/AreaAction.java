package com.itheima.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.action.commen.BaseAction;
import com.itheima.domain.base.Area;
import com.itheima.service.base.AreaService;
import com.itheima.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionContext;

/**
 * 区域的控制层
 *
 * @author 闫惠甜娇
 * @version 1.0，2017年10月29日
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {
	// 注入areaService
	@Autowired
	private AreaService areaService;

	public void setAreaService(AreaService areaService) {
		this.areaService = areaService;
	}

	/** 上传文件对象 */
	private File file;

	/** 上传文件名 */
	private String fileFileName;

	public void setFile(File file) {
		this.file = file;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	/**
	 * 上传文件
	 */
	@Action("area_batchImport")
	public String ImportFile() throws Exception {
		// 创建list集合存储解析的Area对象
		List<Area> areas = new ArrayList<Area>();

		// 创建WorkBook解析excel文件对象
		Workbook workbook = null;
		if (fileFileName.endsWith("xls")) {
			// HSSFWorkbook解析文件时以xls结尾的
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} else if (fileFileName.endsWith("xlsx")) {
			// XSSFWorkbook解析文件是以xlsx结尾的
			workbook = new XSSFWorkbook(new FileInputStream(file));
		}

		// 获得第一个sheet工作簿（sheet继承Iterable<Row>，相当于一个集合）
		Sheet sheet = workbook.getSheetAt(0);
		// 读取sheet的每一行
		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				// 跳过表头第一行
				continue;
			}
			if (row.getCell(0) == null
					|| StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				// 跳过空行
				continue;
			}
			// 创建Area对象
			Area area = new Area();
			// 获得每一个单元格的值,将其赋值给表格中对应的字段
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());

			// 使用pinyin4j生成简码和城市编码
			// 获得省市区的字段
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			// 去掉省市区的最后一个字
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);

			// 编写简码
			String[] strings = PinYin4jUtils.getHeadByString(province + city
					+ district);
			StringBuilder sb = new StringBuilder();
			for (String string : strings) {
				sb.append(string);
			}
			// 设置简码
			area.setShortcode(sb.toString());

			// 编写城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(area.getCity(), "");
			// 设置城市编码
			area.setCitycode(citycode);
			System.out.println(sb.toString());
			System.out.println(citycode);
			// 将area对象添加给areas
			areas.add(area);
		}

		// 将list集合保存到数据库
		areaService.saveData(areas);

		return NONE;
	}

	/**
	 * 导出文件
	 */
	@Action("area_dataExport")
	public String exportFile() {
		// 查找数据库的所有数
		List<Area> areas = areaService.findAll();

		// 创建表格，将数据添加到表格中
		// 1.创建一个解析excel的对象
		Workbook workbook = new XSSFWorkbook();

		// 2.创建sheet工作簿
		Sheet sheet = workbook.createSheet("区域数据");

		// 3.创建行，添加表头
		Row rowHeader = sheet.createRow(0);
		rowHeader.createCell(0).setCellValue("区域编号");
		rowHeader.createCell(1).setCellValue("省份");
		rowHeader.createCell(2).setCellValue("城市");
		rowHeader.createCell(3).setCellValue("区域");
		rowHeader.createCell(4).setCellValue("邮编");

		// 4.便利集合数据，将集合中的数据添加到数据表中
		for (int i = 0; i < areas.size(); i++) {
			// 获得每一条数据
			Area area = areas.get(i);
			// 获得每一条数据的内容
			String id = area.getId();
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			String postcode = area.getPostcode();
			// 创建行对象，将内容添加到行对象
			Row row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(id);
			row.createCell(1).setCellValue(province);
			row.createCell(2).setCellValue(city);
			row.createCell(3).setCellValue(district);
			row.createCell(4).setCellValue(postcode);
		}

		// 提供下载操作
		// 设置数据的导出文件名
		String fileName = "qy.xlsx";
		// 将表格导出（设置两个头）
		HttpServletResponse response = ServletActionContext.getResponse();
		// 1.获得文件的mimetype
		String mimeType = ServletActionContext.getServletContext().getMimeType(
				fileName);
		// 2.设置文件类型
		response.setContentType(mimeType);
		/*
		 * 3.设置文件在浏览器解析的方式 "content-disposition" ： 告诉浏览器打开的方式 attachment :
		 * 以附件的形式打开
		 */
		response.setHeader("content-disposition", "attachment;filename="
				+ fileName);

		// 将文件响应回去
		try {
			workbook.write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return NONE;
	}

	/**
	 * 条件分页查询
	 */
	@Action(value = "area_pageFind", results = { @Result(type = "json") })
	public String area_dataFind() {
		// 1.创建pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);

		// 2.创建条件对象
		Specification<Area> spec = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 创建集合存放条件
				List<Predicate> list = new ArrayList<Predicate>();
				// 添加省份条件
				if (StringUtils.isNotBlank(model.getProvince())) {
					Predicate p1 = cb.like(root.get("province")
							.as(String.class), "%" + model.getProvince() + "%");
					list.add(p1);
				}

				// 添加城市条件
				if (StringUtils.isNotBlank(model.getCity())) {
					if (StringUtils.isNotBlank(model.getCity())) {
						Predicate p2 = null;
						// 获得城市的第一个字符
						char first = model.getCity().charAt(0);
						if (first >= 'a' && first <= 'z') {
							p2 = cb.like(root.get("citycode").as(String.class),
									"%" + model.getCity() + "%");
						} else {
							p2 = cb.like(root.get("city").as(String.class), "%"
									+ model.getCity() + "%");
						}
						list.add(p2);
					}
				}

				// 添加区域条件
				if (StringUtils.isNotBlank(model.getDistrict())) {
					Predicate p3 = cb.like(root.get("district")
							.as(String.class), "%" + model.getDistrict() + "%");
					list.add(p3);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}

		};

		// 调用业务层条件分页查找数据
		Page<Area> page = areaService.area_findPage(spec, pageable);

		// 将数据压入值栈
		pushStack(page);
		return SUCCESS;
	}

	/**
	 * 获得所有的区域
	 */
	/*
	 * @Action(value="area_findAll", results = {@Result(type="json")}) public
	 * String findAll() { List<Area> areas = areaService.findAll(); for (int i =
	 * 0; i < areas.size(); i++) { Area area = areas.get(i); area.setAreaName();
	 * } // 将数据压入栈顶 ActionContext.getContext().getValueStack().push(areas);
	 * return SUCCESS; }
	 */

	/**
	 * 获得所有的省份
	 */
	@Action(value = "area_findProvinces", results = { @Result(type = "json") })
	public String findProvinces() {
		// 调用业务层，获得所有的省
		List<String> provinces = areaService.findProvinces();
		System.out.println(provinces);
		// 创造一个list集合将数据添加进去
		List<Area> list = new ArrayList<Area>();
		for (String province : provinces) {
			Area area = new Area();
			area.setProvince(province);
			list.add(area);
		}
		// 将数据压住栈顶
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}

	/**
	 * 获得指定城市的所有省份
	 */
	@Action(value = "area_findCities", results = @Result(type = "json"))
	public String findCities() {
		try {
			// 获得get请求的参数省份
			String province = ServletActionContext.getRequest().getParameter(
					"province");
			// 处理乱码问题
			province = new String(province.getBytes("iso-8859-1"), "utf-8");

			// 调用业务层层查找指定省份的所有城市
			List<String> cities = areaService.findCities(province);

			// 将list集合数据封装到可以转化成json的集合中
			List<Area> areas = new ArrayList<Area>();
			for (String city : cities) {
				Area area = new Area();
				area.setCity(city);
				areas.add(area);
			}
			// 将areas压入栈顶
			ActionContext.getContext().getValueStack().push(areas);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 获得指定省份，城市的所有的区县
	 */
	@Action(value = "area_findDistricts", results = @Result(type = "json"))
	public String findDistricts() {
		try {
			// 获得省
			String province = new String(model.getProvince().getBytes(
					"iso-8859-1"), "utf-8");
			// 获得市
			String city = new String(model.getCity().getBytes("iso-8859-1"),
					"utf-8");
			// 调用业务层对象获得指定省份，城市的所有的区县
			List<String> districts = areaService.findDistricts(province, city);
			
			// 将list集合数据封装到可以转化成json的集合中
			List<Area> areas = new ArrayList<Area>();
			for (String district : districts) {
				Area area = new Area();
				area.setDistrict(district);
				areas.add(area);
			}
			// 将areas压入栈顶
			ActionContext.getContext().getValueStack().push(areas);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 获得指定省市区的area
	 */
	@Action(value="area_findOne", results = @Result(type = "json"))
	public String findArea() {
		// 调用业务层对象获得指定省市区的area
		Area area = areaService.findByProvinceAndCityAndDistrict(model.getProvince(), model.getCity(), model.getDistrict());
		// 将数据压住值栈的顶部
		ActionContext.getContext().getValueStack().push(area);
		return SUCCESS;
	}

}

package com.itheima.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.itheima.domain.base.SubArea;
import com.itheima.service.base.AreaService;
import com.itheima.service.base.SubAreaService;
import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class SubAreaAction extends BaseAction<SubArea> {

	/** 注入业务层对象 */
	@Autowired
	private SubAreaService subAreaService;

	/**
	 * 保存分区
	 */
	@Action(value = "save_subArea", results = @Result(type = "redirect", location = "./pages/base/sub_area.html"))
	public String saveSubArea() {
		// 调用业务层对象保存用户
		subAreaService.save(model);
		return SUCCESS;
	}

	/**
	 * 条件分页查询
	 */
	@Action(value = "subArea_pageFind", results = @Result(type = "json"))
	public String pageFind() {
		// 创建pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);
		System.out.println(page);
		System.out.println(rows);

		// 创建条件对象
		Specification<SubArea> sepc = new Specification<SubArea>() {

			@Override
			public Predicate toPredicate(Root<SubArea> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {

				// 创建一个条件集合
				List<Predicate> pres = new ArrayList<Predicate>();

				// 1.单表查询
				// 关键字查询
				if (StringUtils.isNotBlank(model.getKeyWords())) {
					System.out.println(model.getKeyWords());
					Predicate p = cb.like(
							root.get("keyWords").as(String.class),
							"%" + model.getKeyWords() + "%");
					pres.add(p);
				}

				// 多表查询
				Area area = model.getArea();
				if (area != null) {
					// 获得关联表的对象
					Join<SubArea, Area> areaRoot = root.join("area",
							JoinType.INNER);

					// 1.省查询
					if (StringUtils.isNotBlank(area.getProvince())) {
						Predicate p = cb.like(
								areaRoot.get("province").as(String.class), "%"
										+ area.getProvince() + "%");
						// 添加省查询条件
						pres.add(p);
					}

					// 2.市查询
					if (StringUtils.isNotBlank(area.getCity())) {
						Predicate p = cb.like(
								areaRoot.get("city").as(String.class), "%"
										+ area.getCity() + "%");
						// 添加市查询条件
						pres.add(p);
					}

					// 3.区查询
					if (StringUtils.isNotBlank(area.getDistrict())) {
						Predicate p = cb.like(
								areaRoot.get("district").as(String.class), "%"
										+ area.getDistrict() + "%");
						// 添加区查询条件
						pres.add(p);
					}

				}
				return cb.and(pres.toArray(new Predicate[0]));
			}
		};

		// 调用业务层查询
		Page<SubArea> page = subAreaService.PageFind(sepc, pageable);
		System.out.println(page);

		// 将数据压入值栈
		pushStack(page);

		return SUCCESS;
	}

	/** 页面传来的文件对象 */
	private File file;

	/** 页面传来的文件名 */
	private String fileFileName;

	public void setFile(File file) {
		this.file = file;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	/** 注入区域id */
	@Autowired
	private AreaService areaService;

	/**
	 * 文件上传
	 */
	@Action(value = "fileImport", results = @Result(type = "json"))
	public String fileImport() {
		// 定义变量，用于记录导入成功或者失败
		boolean flag = true;

		try {
			// 创建Workbook对象解析文件
			Workbook workbook = null;
			if (fileFileName.endsWith("xls")) {
				workbook = new HSSFWorkbook(new FileInputStream(file));
			} else if (fileFileName.endsWith("xlsx")) {
				workbook = new XSSFWorkbook(new FileInputStream(file));
			}
			// 创建一个list集合保存
			List<SubArea> subAreas = new ArrayList<SubArea>();

			// 获得excel表格的第一个工作簿
			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				// 跳过表头第一行
				if (row.getRowNum() == 0) {
					continue;
				}
				// 跳过空行
				if (row.getCell(0) != null
						&& StringUtils.isBlank(row.getCell(0)
								.getStringCellValue())) {
					continue;
				}
				// 创建分区对象
				SubArea subArea = new SubArea();
				// 解析表中的数据，并将其添加到集合中
				subArea.setId(row.getCell(0).getStringCellValue());

				// 获得表格中的省市区字段
				String province = row.getCell(1).getStringCellValue();
				String city = row.getCell(2).getStringCellValue();
				String district = row.getCell(3).getStringCellValue();

				// 根据省市区查找区域
				Area area = areaService.findByProvinceAndCityAndDistrict(
						province, city, district);

				subArea.setArea(area);
				
				subArea.setKeyWords(row.getCell(4).getStringCellValue());
				subArea.setStartNum(row.getCell(5).getStringCellValue());
				subArea.setEndNum(row.getCell(6).getStringCellValue());
				subArea.setSingle(row.getCell(7).getStringCellValue().charAt(0));
				subArea.setAssistKeyWords(row.getCell(8).getStringCellValue());

				// 将每解析成的subArea数据添加到集合红
				subAreas.add(subArea);

				// 调用业务层保存数据
				subAreaService.save(subAreas);
			}
		} catch (IOException e) {
			flag = false;
			e.printStackTrace();
		}
		// 创建list集合
		String[] content = new String[1];
		if (flag) {
			content[0] = "文件上传成功";
		} else {
			content[0] = "文件上传失败";
		}
		// 将list集合压入栈顶
		ActionContext.getContext().getValueStack().push(content);
		return SUCCESS;
	}

}

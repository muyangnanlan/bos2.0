package com.itheima.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.itheima.domain.base.SubArea;

public interface SubAreaDao extends JpaRepository<SubArea, String>, JpaSpecificationExecutor<SubArea>{

}

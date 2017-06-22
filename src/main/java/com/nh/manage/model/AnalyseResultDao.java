package com.nh.manage.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nh.manage.entity.AnalyseResult;



@Transactional
public interface AnalyseResultDao extends CrudRepository<AnalyseResult, Integer>{
	@Query("select p from AnalyseResult p where p.category=:category")
	List<AnalyseResult> findOrderByCategory(@Param("category")int category);
}

package com.nh.manage.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nh.manage.entity.GoodForecast;
import com.nh.manage.entity.GoodsTrend;


@Transactional
public interface GoodForecastDao extends CrudRepository<GoodForecast, Integer>{
	@Query("select p from GoodForecast p where p.create_time=:create_time")
	List<GoodForecast> findGoodForecastDaoBycreateTime(@Param("create_time")int create_time);
}

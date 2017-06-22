package com.nh.manage.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nh.manage.entity.GoodsTrend;


@Transactional
public interface GoodsTrendDao extends CrudRepository<GoodsTrend, Integer>{
	@Query("select p from GoodsTrend p where p.good_id=:good_id")
	List<GoodsTrend> findOrderByGoodsId(@Param("good_id")int good_id);
}

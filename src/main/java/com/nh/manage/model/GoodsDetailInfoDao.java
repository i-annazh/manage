package com.nh.manage.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nh.manage.entity.GoodsDetailInfo;
import com.nh.manage.entity.Navigation;


@Transactional
public interface GoodsDetailInfoDao extends CrudRepository<GoodsDetailInfo, Integer>{
	@Query("select p from GoodsDetailInfo p where p.item_from=:item_from")
	List<GoodsDetailInfo> findOrderByUrl(@Param("item_from")String item_from);
	
	@Query("select p from GoodsDetailInfo p where p.item_url like CONCAT('%',:urlname,'%')")
	List<GoodsDetailInfo> findOrderByUrlName(@Param("urlname")String urlname);
	
	@Query("select p from GoodsDetailInfo p where p.name like CONCAT('%',:name,'%')")
	List<GoodsDetailInfo> findOrderByGoodName(@Param("name")String name);
	
	//多表查询
	@Query("SELECT p from GoodsDetailInfo p WHERE p.id=:good_id") 
	List<GoodsDetailInfo> findOrderByGoodId(@Param("good_id")int good_id);
}

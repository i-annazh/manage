package com.nh.manage.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nh.manage.entity.Navigation;

@Transactional
public interface NavigationDao extends CrudRepository<Navigation, Integer>{

	@Query("select p from Navigation p where p.status_flag=0 and p.source=:source")
	List<Navigation> findOrderBySourceName(@Param("source")String source);
	

	
}
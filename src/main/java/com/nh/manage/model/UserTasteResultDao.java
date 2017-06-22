package com.nh.manage.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nh.manage.entity.UserTaste;
import com.nh.manage.entity.UserTasteResult;



@Transactional
public interface UserTasteResultDao extends CrudRepository<UserTasteResult, Integer>{
	@Query("select p from UserTasteResult p where p.tel=:tel")
	List<UserTasteResult> findGoodidsByTel(@Param("tel")String tel);
}

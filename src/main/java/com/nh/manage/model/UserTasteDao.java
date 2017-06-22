package com.nh.manage.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nh.manage.entity.GoodsTrend;
import com.nh.manage.entity.UserTaste;

@Transactional
public interface UserTasteDao extends CrudRepository<UserTaste, Integer>{
	
	@Query(value = "update UserTaste p set p.goodIds =:goodIds ,p.update_time=:update_time where p.tel =:tel")
	@Modifying
	void updateGoodIds(@Param("goodIds") String goodIds,@Param("update_time") int update_time,@Param("tel") String tel);
	
	@Query("select p from UserTaste p where p.tel=:tel")
	List<UserTaste> findByTel(@Param("tel")String tel);
}

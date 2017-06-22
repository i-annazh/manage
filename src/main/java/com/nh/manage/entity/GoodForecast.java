package com.nh.manage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="goods_forecast")
public class GoodForecast {
	@Id
	@Column(name="id")
	private int id;
	
	@NotNull
	@Column(name="good_ids")
	private String good_ids;
	
	@NotNull
	@Column(name="create_time")
	private int create_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGood_ids() {
		return good_ids;
	}

	public void setGood_ids(String good_ids) {
		this.good_ids = good_ids;
	}

	public int getCreate_time() {
		return create_time;
	}

	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}

	@Override
	public String toString() {
		return "GoodsForecast [id=" + id + ", good_ids=" + good_ids + ", create_time=" + create_time + "]";
	}

	public GoodForecast(int id, String good_ids, int create_time) {
		super();
		this.id = id;
		this.good_ids = good_ids;
		this.create_time = create_time;
	}

	public GoodForecast() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

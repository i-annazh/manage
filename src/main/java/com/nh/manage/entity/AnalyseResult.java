package com.nh.manage.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="analyse_result")
public class AnalyseResult {
	
	@Id
	@Column(name="id")
	private int id;
	
	@NotNull
	@Column(name="good_id")
	private int good_id;
	
	@NotNull
	@Column(name="comment_num")
	private int comment_num;
	
	@NotNull
	@Column(name="good_rate")
	private float good_rate;
	
	@NotNull
	@Column(name="good_comment_num")
	private int good_comment_num;
	
	@NotNull
	@Column(name="mid_comment_num")
	private int mid_comment_num;
	
	@NotNull
	@Column(name="bad_comment_num")
	private int bad_comment_num;
	
	@NotNull
	@Column(name="price")
	private float price;
	
	@Column(name="create_time")
	private int create_time;
	
	@NotNull
	@Column(name="category")
	private int category;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGood_id() {
		return good_id;
	}

	public void setGood_id(int good_id) {
		this.good_id = good_id;
	}

	public int getComment_num() {
		return comment_num;
	}

	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}

	public float getGood_rate() {
		return good_rate;
	}

	public void setGood_rate(float good_rate) {
		this.good_rate = good_rate;
	}

	public int getGood_comment_num() {
		return good_comment_num;
	}

	public void setGood_comment_num(int good_comment_num) {
		this.good_comment_num = good_comment_num;
	}

	public int getMid_comment_num() {
		return mid_comment_num;
	}

	public void setMid_comment_num(int mid_comment_num) {
		this.mid_comment_num = mid_comment_num;
	}

	public int getBad_comment_num() {
		return bad_comment_num;
	}

	public void setBad_comment_num(int bad_comment_num) {
		this.bad_comment_num = bad_comment_num;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getCreate_time() {
		return create_time;
	}

	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public AnalyseResult(int id, int good_id, int comment_num, float good_rate, int good_comment_num,
			int mid_comment_num, int bad_comment_num, float price, int create_time, int category) {
		super();
		this.id = id;
		this.good_id = good_id;
		this.comment_num = comment_num;
		this.good_rate = good_rate;
		this.good_comment_num = good_comment_num;
		this.mid_comment_num = mid_comment_num;
		this.bad_comment_num = bad_comment_num;
		this.price = price;
		this.create_time = create_time;
		this.category = category;
	}
	
	public Map<String, Object> toMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("good_id", good_id);
		map.put("comment_num", comment_num);
		map.put("good_rate", good_rate);
		map.put("good_comment_num", good_comment_num);
		map.put("mid_comment_num", mid_comment_num);
		map.put("bad_comment_num", bad_comment_num);
		map.put("price", price);
		map.put("create_time", create_time);
		map.put("category", category);
		return map;
	}

	public AnalyseResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "AnalyseResult [id=" + id + ", good_id=" + good_id + ", comment_num=" + comment_num + ", good_rate="
				+ good_rate + ", good_comment_num=" + good_comment_num + ", mid_comment_num=" + mid_comment_num
				+ ", bad_comment_num=" + bad_comment_num + ", price=" + price + ", create_time=" + create_time
				+ ", category=" + category + "]";
	}
	
	
}

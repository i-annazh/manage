package com.nh.manage.entity;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="goods_detail_info")
public class GoodsDetailInfo {
	
	@Id
	@Column(name="id")
	private int id;
	
	@NotNull
	@Column(name="name")
	private String name;	
	@NotNull
	@Column(name="price")
	private float price;
	
	@NotNull
	@Column(name="merchant_name")
	private String merchant_name;
	
	@NotNull
	@Column(name="item_url")
	private String item_url;
	
	@NotNull
	@Column(name="item_id")
	private String item_id;
	
	@NotNull
	@Column(name="item_extend")
	private String item_extend;
	
	@Column(name="img_url")
	private String img_url;
	
	@NotNull
	@Column(name="item_from")
	private String item_from;
	
	@NotNull
	@Column(name="status_flag")
	private int status_flag;
	
	@NotNull
	@Column(name="score")
	private int score;
	
	@NotNull
	@Column(name="create_time")
	private int create_time;
	
	@NotNull
	@Column(name="update_time")
	private int update_time;
	
	
	
	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getMerchant_name() {
		return merchant_name;
	}

	public void setMerchant_name(String merchant_name) {
		this.merchant_name = merchant_name;
	}

	public String getItem_url() {
		return item_url;
	}

	public void setItem_url(String item_url) {
		this.item_url = item_url;
	}

	public String getItem_extend() {
		return item_extend;
	}

	public void setItem_extend(String item_extend) {
		this.item_extend = item_extend;
	}

	public String getItem_from() {
		return item_from;
	}

	public void setItem_from(String item_from) {
		this.item_from = item_from;
	}

	public int getStatus_flag() {
		return status_flag;
	}

	public void setStatus_flag(int status_flag) {
		this.status_flag = status_flag;
	}

	public int getCreate_time() {
		return create_time;
	}

	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}

	public int getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(int update_time) {
		this.update_time = update_time;
	}
	
	

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	

	public GoodsDetailInfo(int id, String name, float price, String merchant_name, String item_url, String item_id, String item_extend,
			String item_from, int status_flag, int score, int create_time, int update_time,String img_url) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.merchant_name = merchant_name;
		this.item_url = item_url;
		this.item_id = item_id;
		this.item_extend = item_extend;
		this.item_from = item_from;
		this.status_flag = status_flag;
		this.score = score;
		this.create_time = create_time;
		this.update_time = update_time;
		this.img_url = img_url;
	}
	
	public Map<String, Object>  toMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
//		try {
//			map.put("name", java.net.URLEncoder.encode(name, "utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		map.put("price", price);
		map.put("merchant_name", merchant_name);
		map.put("item_url", item_url);
		map.put("item_extend", item_extend);
		map.put("item_from", item_from);
		map.put("status_flag", status_flag);
		map.put("score", score);
		map.put("item_id", item_id);
		map.put("img_url", img_url);
		
		
		return map;
	}
	
	

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public GoodsDetailInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "GoodsDetailInfo [id=" + id + ", name=" + name + ", price=" + price + ", merchant_name=" + merchant_name
				+ ", item_url=" + item_url + ", item_id=" + item_id + ", item_extend=" + item_extend + ", img_url="
				+ img_url + ", item_from=" + item_from + ", status_flag=" + status_flag + ", score=" + score
				+ ", create_time=" + create_time + ", update_time=" + update_time + "]";
	}

	

	
	
	
}

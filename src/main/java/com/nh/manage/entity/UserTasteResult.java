package com.nh.manage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="user_taste_result")
public class UserTasteResult {
	@Id
	@Column(name="id")
	private int id;
	
	@NotNull
	@Column(name="user_id")
	private int user_id;
	
	@NotNull
	@Column(name="tel")
	private String tel;
	
	@NotNull
	@Column(name="mac")
	private String mac;
	
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

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
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
		return "UserTasteResult [id=" + id + ", user_id=" + user_id + ", tel=" + tel + ", mac=" + mac + ", good_ids="
				+ good_ids + ", create_time=" + create_time + "]";
	}

	public UserTasteResult(int id, int user_id, String tel, String mac, String good_ids, int create_time) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.tel = tel;
		this.mac = mac;
		this.good_ids = good_ids;
		this.create_time = create_time;
	}

	public UserTasteResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

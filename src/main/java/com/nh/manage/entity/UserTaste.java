package com.nh.manage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="user_taste")
public class UserTaste {
	@Id
	@Column(name="id")
	private int id;
	
	@NotNull
	@Column(name="tel")
	private String tel;
	
	@NotNull
	@Column(name="mac")
	private String mac;
	
	@NotNull
	@Column(name="good_ids")
	private String goodIds;
	
	@NotNull
	@Column(name="create_time")
	private int create_time;
	
	@NotNull
	@Column(name="update_time")
	private int update_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getGoodIds() {
		return goodIds;
	}

	public void setGoodIds(String goodIds) {
		this.goodIds = goodIds;
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

	@Override
	public String toString() {
		return "UserTaste [id=" + id + ", tel=" + tel + ", mac=" + mac + ", goodIds=" + goodIds + ", create_time="
				+ create_time + ", update_time=" + update_time + "]";
	}

	public UserTaste(int id, String tel, String mac, String goodIds, int create_time, int update_time) {
		super();
		this.id = id;
		this.tel = tel;
		this.mac = mac;
		this.goodIds = goodIds;
		this.create_time = create_time;
		this.update_time = update_time;
	}

	public UserTaste() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

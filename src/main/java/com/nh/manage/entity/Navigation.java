package com.nh.manage.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="navigation")
public class Navigation {
	
	@Override
	public String toString() {
		return "Navigation [id=" + id + ", source=" + source + ", category=" + category + ", url=" + url
				+ ", status_flag=" + status_flag + ", create_time=" + create_time + ", update_time=" + update_time
				+ "]";
	}

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="source")
	private String source;
	
	@NotNull
	@Column(name="category")
	private String category;
	
	@NotNull
	@Column(name="url")
	private String url;
	
	@NotNull
	@Column(name="status_flag")
	private int status_flag;
	
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Navigation(int id, String source, String category, String url, int status_flag, int create_time,
			int update_time) {
		super();
		this.id = id;
		this.source = source;
		this.category = category;
		this.url = url;
		this.status_flag = status_flag;
		this.create_time = create_time;
		this.update_time = update_time;
	}

	public Navigation() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}

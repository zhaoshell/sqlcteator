package com.platform.kit.mapping.exam;

import java.io.Serializable;
import java.util.Date;

import com.platform.kit.mapping.annotations.Fields;

public class MapperObj2 implements Serializable {

	private static final long serialVersionUID = 7294679835548504971L;

	@Fields(name = "cid")
	private Integer id;

	@Fields(name = "nickName", isMapping = false)
	private String name;

	@Fields(name = "description")
	private String desc;

	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MapperObj [id=" + id + ", name=" + name + ", desc=" + desc + ", createDate=" + createDate + "]";
	}
}

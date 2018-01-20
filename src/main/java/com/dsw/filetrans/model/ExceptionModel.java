package com.dsw.filetrans.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 异常实体类
 * @author Zhuxs
 *
 */
@Entity
@Table(name="exception_info")
public class ExceptionModel {
	/**
	 * 异常ID，自生成
	 */
	@Column(name="ID")
	@Id
	private UUID id;
	
	/**
	 * 创建时间
	 */
	@Column(name="createTime")
	private Date createTime;
	
	/**
	 * 状态
	 */
	@Column(name="status")
	private Integer status;
	
	/**
	 * 内容
	 */
	@Column(name="content")
	private String content;
	
	@Column(name="backup1")
	private String backup1;
	
	@Column(name="backup2")
	private String backup2;
	
	@Column(name="backup3")
	private String backup3;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBackup1() {
		return backup1;
	}

	public void setBackup1(String backup1) {
		this.backup1 = backup1;
	}

	public String getBackup2() {
		return backup2;
	}

	public void setBackup2(String backup2) {
		this.backup2 = backup2;
	}

	public String getBackup3() {
		return backup3;
	}

	public void setBackup3(String backup3) {
		this.backup3 = backup3;
	}

	@Override
	public String toString() {
		return "ExceptionModel [id=" + id + ", createTime=" + createTime + ", status=" + status + ", content=" + content
				+ "]";
	}
	
	
	
	
}

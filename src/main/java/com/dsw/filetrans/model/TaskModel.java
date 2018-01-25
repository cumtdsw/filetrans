package com.dsw.filetrans.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 任务实体类
 * @author Zhuxs
 *
 */
@Entity
@Table(name="task_info")
public class TaskModel {
	/**
	 * 任务ID，通过消息中的任务信息获取
	 */
	@Column(name="ID")
	@Id
	private String id;
	
	/**
	 * 创建时间
	 */
	@Column(name="createTime")
	private Date createTime;
	
	/**
	 * 开始时间
	 */
	@Column(name="startTime")
	private Date startTime;
	
	/**
	 * 结束时间
	 */
	@Column(name="endTime")
	private Date endTime;
	
	/**
	 * 状态
	 */
	@Column(name="status")
	private int status;
	
	/**
	 * 源文件类型(文件、文件组、文件夹)
	 */
	@Column(name="dataType")
	private Integer dataType;
	
	/**
	 * 源路径
	 */
	@Column(name="dataPath")
	private String dataPath;
	
	/**
	 * 目标IP
	 */
	@Column(name="toIP")
	private String toIP;
	
	/**
	 * 目标路径
	 */
	@Column(name="toPath")
	private String toPath;
	
	@Column(name="backup1")
	private String backup1;
	
	@Column(name="backup2")
	private String backup2;
	
	@Column(name="backup3")
	private String backup3;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public String getDataPath() {
		return dataPath;
	}
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}
	public String getToIP() {
		return toIP;
	}
	public void setToIP(String toIP) {
		this.toIP = toIP;
	}
	public String getToPath() {
		return toPath;
	}
	public void setToPath(String toPath) {
		this.toPath = toPath;
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
		return "TaskModel [id=" + id + ", createTime=" + createTime + ", startTime=" + startTime + ", endTime="
				+ endTime + ", status=" + status + ", dataType=" + dataType + ", dataPath=" + dataPath + ", toIP="
				+ toIP + ", toPath=" + toPath + "]";
	}
	
	
	
}

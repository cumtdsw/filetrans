package com.dsw.filetrans.query.condition;

import java.util.Date;

public class TaskModelCondition extends BaseCondition{
	
	/**
	 * 任务ID，通过消息中的任务信息获取
	 */
	private String id;
	
	/**
	 * 开始时间
	 */
	private Date startTime;
	
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	/**
	 * 状态
	 */
	private Integer status;
	
	/**
	 * 源文件类型(文件、文件组、文件夹)
	 */
	private Integer dataType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	
}

package com.dsw.filetrans.manager.queue;


import java.util.Date;
/**
 * task队列
 * @author Zhuxs
 *
 */
public class TaskQueue {
	private String taskID;
	
	private Date addTime;
	//0:等待执行，1：已开始
	private int state;

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}


	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}


	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
}


package com.dsw.filetrans.manager.queue;

import java.util.Date;
import java.util.UUID;
/**
 * 消息发送异常队列
 * @author Zhuxs
 *
 */
public class ExceptionQueue {

	private UUID exceptionID;
	
	private Date addTime;
	
	private int status;

	public UUID getExceptionID() {
		return exceptionID;
	}

	public void setExceptionID(UUID exceptionID) {
		this.exceptionID = exceptionID;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}

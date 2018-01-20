package com.dsw.filetrans.message;

/**
 * 从消息队列取到的任务创建信息
 * @author Administrator
 *
 */
public class TaskMessage {
	
	public TaskMessage(){
		
	}
	/**
	 * 任务ID
	 */
	private String taskID;
	/**
	 * 文件传输发送方IP
	 */
	private String sourceIP;
	/**
	 * 文件传输源路径
	 */
	private String sourcePath;
	/**
	 * 文件传输目标路径
	 */
	private String toPath;
	/**
	 * 文件传输接收方IP
	 */
	private String toIP;

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getToPath() {
		return toPath;
	}

	public void setToPath(String toPath) {
		this.toPath = toPath;
	}

	public String getToIP() {
		return toIP;
	}

	public void setToIP(String toIP) {
		this.toIP = toIP;
	}
	
	
}

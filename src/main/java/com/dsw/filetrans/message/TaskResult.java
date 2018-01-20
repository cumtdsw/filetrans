package com.dsw.filetrans.message;

/**
 * 返回给消息队列的任务执行结果
 * @author Zhuxs
 *
 */
public class TaskResult{
	
	public TaskResult(){
		
	}
	/**
	 * 任务ID
	 */
	private String taskID;
	/**
	 * 任务执行结果
	 */
	private int result;//0:成功,1:失败
	/**
	 * 备注
	 */
	private String note;

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	
}

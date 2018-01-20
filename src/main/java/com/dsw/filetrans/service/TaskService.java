package com.dsw.filetrans.service;

import java.util.Date;
import java.util.List;

import com.dsw.filetrans.model.TaskModel;
/**
 * 任务Service
 * @author Zhuxs
 *
 */
public interface TaskService {
	/**
	 * 添加任务
	 * @param task
	 * @return
	 */
	public boolean add(TaskModel task);
	/**
	 * 更新任务
	 * @param task
	 * @return
	 */
	public boolean update(TaskModel task);
	/**
	 * 根据任务ID更新任务状态
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean updateStatus(String id,int status);
	/**
	 * 系统初始化时获取任务列表
	 * @return
	 */
	public List<TaskModel> getInitTaskList();
	/**
	 * 根据ID获取任务信息
	 * @param id
	 * @return
	 */
	public TaskModel getTaskByID(String id);
	/**
	 * 根据ID和type更新时间,type:0更新start_time，1：更新end_time
	 * @param id
	 * @param time
	 * @param type
	 * @return
	 */
	public boolean updateTime(String id,Date time,int type);
	/**
	 * 获取初始化任务数量
	 * @param strWhere
	 * @return
	 */
	public int getInitTaskCount();
	
}

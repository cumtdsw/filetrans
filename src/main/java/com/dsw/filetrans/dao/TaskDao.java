package com.dsw.filetrans.dao;

import java.util.Date;
import java.util.List;

import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.query.condition.TaskModelCondition;
import com.dsw.filetrans.query.result.QueryResult;
/**
 * 任务DAO
 * @author Zhuxs
 *
 */
public interface TaskDao {
	
	public QueryResult query(TaskModelCondition condition);
	/**
	 * 插入task信息到表
	 * @param model
	 * @return
	 */
	public boolean add(TaskModel taskModel);
	/**
	 * 更新task信息
	 * @param model
	 * @return
	 */
	public boolean update(TaskModel taskModel);
	
	/**
	 * 通过ID查询task
	 * @param id
	 * @return
	 */
	public TaskModel getTaskByID(String id);
	
	/**
	 * 根据条件更新状态
	 * @param where
	 * @param status
	 * @return
	 */
	public boolean updateStatus(String where, int status);
	/**
	 * 根据id更新开始时间
	 * @param taskID
	 * @param time
	 * @return
	 */
	public boolean updateStartTime(String taskID, Date time);
	/**
	 * 根据id更新结束时间
	 * @param taskID
	 * @param time
	 * @return
	 */
	public boolean updateEndTime(String taskID, Date time);
	/**
	 * 将ResultSet转换成任务列表
	 * @param rs
	 * @return
	 */
	public List<TaskModel> dataTableToList();
	
	/**
	 * 查询个数
	 * @param sql
	 * @return
	 */
	public int querySize(String sql);

}

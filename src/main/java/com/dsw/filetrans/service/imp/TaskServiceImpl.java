package com.dsw.filetrans.service.imp;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsw.filetrans.dao.TaskDao;
import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.service.TaskService;
import com.dsw.filetrans.util.TimeUtil;

@Service
public class TaskServiceImpl implements TaskService {
	
	static Logger logger = LogManager.getLogger(TaskServiceImpl.class);
	
	@Autowired
	protected TaskDao dao;

	@Override
	public boolean add(TaskModel task) {
		TaskModel hasModel = dao.getTaskByID(task.getId());
		if(hasModel==null){
			return dao.add(task);
		}else if(hasModel.getStatus()==2||hasModel.getStatus()==3){
			return dao.update(task);
		}else{
			logger.error("Task[Task ID:"+task.getId()+"]: is excuting");
			return false;
		}
	}

	@Override
	public boolean update(TaskModel task) {
		return dao.update(task);
	}

	@Override
	public boolean updateStatus(String id , int status) {
		String where = " ID = '"+ id+"'";
		return dao.updateStatus(where, status);
	}

	@Override
	public List<TaskModel> getInitTaskList() {
		return dao.dataTableToList();
	}

	@Override
	public TaskModel getTaskByID(String id) {
		return dao.getTaskByID(id);
	}

	@Override
	public boolean updateTime(String id, Date time, int type) {
		boolean result = false;		
		if(type == 0){
			result = dao.updateStartTime(id, time);
		}else if(type==1){
			result = dao.updateEndTime(id, time);
		}		
		return result;
	}

	@Override
	public int getInitTaskCount() {
		java.sql.Timestamp twelve = new java.sql.Timestamp(TimeUtil.getTime(12).getTime());
		java.sql.Timestamp zero = new java.sql.Timestamp(TimeUtil.getTime(0).getTime());
		java.sql.Timestamp now = new java.sql.Timestamp(new Date().getTime());
		StringBuilder sb = new StringBuilder();
		sb.append("select * from task_info where ");
		if(TimeUtil.isPM()){
			sb.append(" createTime <= '"+now+"' and createTime > '"+twelve+"'");
		}else{
			sb.append(" createTime <= '"+now+"' and createTime > '"+zero+"'");
		}
		return dao.querySize(sb.toString());
	}



}
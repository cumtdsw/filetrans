package com.dsw.filetrans.service.imp;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dsw.filetrans.Constants;
import com.dsw.filetrans.dao.ExceptionDao;
import com.dsw.filetrans.dao.TaskDao;
import com.dsw.filetrans.jms.JMSProducer;
import com.dsw.filetrans.message.TaskMessage;
import com.dsw.filetrans.model.ExceptionModel;
import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.query.condition.TaskModelCondition;
import com.dsw.filetrans.query.result.QueryResult;
import com.dsw.filetrans.service.TaskService;
import com.dsw.filetrans.util.JsonUtil;
import com.dsw.filetrans.util.TimeUtil;

@Service
public class TaskServiceImpl implements TaskService {

	static Logger logger = LogManager.getLogger(TaskServiceImpl.class);

	@Autowired
	protected TaskDao taskDao;

	@Autowired
	protected JMSProducer jmsProducer;
	
	@Autowired
	protected ExceptionDao exceptionDao;

	@Override
	public boolean add(TaskModel task) {
		return taskDao.add(task);
	}

	@Override
	public boolean update(TaskModel task) {
		return taskDao.update(task);
	}

	@Override
	public boolean updateStatus(String id, int status) {
		String where = " ID = '" + id + "'";
		return taskDao.updateStatus(where, status);
	}

	@Override
	public List<TaskModel> getInitTaskList() {
		return taskDao.dataTableToList();
	}

	@Override
	@Transactional
	public TaskModel getTaskByID(String id) {
		return taskDao.getTaskByID(id);
	}

	@Override
	public boolean updateTime(String id, Date time, int type) {
		boolean result = false;
		if (type == 0) {
			result = taskDao.updateStartTime(id, time);
		} else if (type == 1) {
			result = taskDao.updateEndTime(id, time);
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
		if (TimeUtil.isPM()) {
			sb.append(" createTime <= '" + now + "' and createTime > '" + twelve + "'");
		} else {
			sb.append(" createTime <= '" + now + "' and createTime > '" + zero + "'");
		}
		return taskDao.querySize(sb.toString());
	}

	@Override
	public QueryResult queryTask(TaskModelCondition condition) {
		return taskDao.query(condition);
	}

	@Override
	public boolean startTask(TaskModel task) {
		TaskModel taskExisting = taskDao.getTaskByID(task.getId());
		TaskMessage taskMsg = new TaskMessage();
		taskMsg.setSourceIP(Constants.LocalIP);
		taskMsg.setSourcePath(task.getDataPath());
		if (taskExisting == null) {
			taskMsg.setTaskID(UUID.randomUUID().toString());
		} else {
			taskMsg.setTaskID(task.getId());
		}
		taskMsg.setToIP(task.getToIP());
		taskMsg.setToPath(task.getToPath());
		String message = JsonUtil.Object2Json(taskMsg);
		boolean result = jmsProducer.send2Queue("jacky.queue", message);
		if (!result) {
			ExceptionModel model = new ExceptionModel();
			model.setContent(message);
			model.setId(UUID.randomUUID());
			model.setCreateTime(new Date());
			model.setStatus(0);
			exceptionDao.add(model);		
		}
		return result;
	}

}
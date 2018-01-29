package com.dsw.filetrans.dao.imp;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dsw.filetrans.dao.TaskDao;
import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.query.condition.TaskModelCondition;
import com.dsw.filetrans.query.result.QueryResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-beans.xml","/spring-jms.xml" })
public class TaskDaoImpTest {
	static Logger logger = LogManager.getLogger(TaskDaoImpl.class);
	
	@Autowired
	protected TaskDao taskDao;
	
	@Test
	public void add() {
		TaskModel taskModel = new TaskModel();
		taskModel.setId(UUID.randomUUID().toString());
		taskDao.add(taskModel);
	}
	
	@Test
	public void getTaskByID() {
		TaskModel taskModel = taskDao.getTaskByID("1dc03523-cdbd-49ba-8f7c-07134bde9066");
		logger.info("taskModel:" + taskModel.toString());
	}
	
	@Test
	public void query() {
		TaskModelCondition condition = new TaskModelCondition();
		QueryResult result = taskDao.query(condition);
		for (Object obj : result.getResults()) {
			TaskModel taskModel =(TaskModel) obj;
			logger.info("taskModel:" + taskModel.toString());
		}
	}

}

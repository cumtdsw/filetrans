package com.dsw.filetrans.service.imp;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.query.condition.TaskModelCondition;
import com.dsw.filetrans.query.result.QueryResult;
import com.dsw.filetrans.service.TaskService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-beans.xml","/spring-jms.xml" })
public class TaskServiceImpTest {
	static Logger logger = LogManager.getLogger(TaskServiceImpTest.class);
	
	@Autowired 
	protected TaskService taskService;
	
	@Test
	public void queryTask() {
		TaskModelCondition condition = new TaskModelCondition();
		QueryResult result = taskService.queryTask(condition);
		for (Object obj : result.getResults()) {
			TaskModel taskModel =(TaskModel) obj;
			logger.info("taskModel:" + taskModel.toString());
		}
	}
	
	@Test
	public void add() {
		TaskModel task = new TaskModel();
		task.setId(UUID.randomUUID().toString());
		taskService.add(task);
	}

}

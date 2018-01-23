package com.dsw.filetrans.jms.imp;

import javax.jms.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsw.filetrans.jms.JMSConsumer;
import com.dsw.filetrans.service.TaskService;

@Service
public class JMSConsumerImp implements JMSConsumer {
	static Logger logger = LogManager.getLogger(JMSConsumerImp.class);
	
	@Autowired
	protected TaskService taskService;

	@Override
	public void onMessage(Message message) {
		/*try {
			String text = ((TextMessage) message).getText();
			System.out.println(text);
			TaskMessage taskmsg = JsonUtil.json2Object(text, TaskMessage.class);
			if (null == taskmsg.getSourceIP() || taskmsg.getSourceIP().trim().equals("")
					|| !taskmsg.getSourceIP().equals(InitParams.LocalIP)) {
				logger.info("[TaskID:" + taskmsg.getTaskID() + "]This Task doesn't transfer from here!");
				return;
			}
			TaskModel task = new TaskModel();
			task.setCreateTime(new Date());
			task.setId(taskmsg.getTaskID());
			task.setStatus(Constants.getTaskStatus().get("waiting"));
			task.setToIP(taskmsg.getToIP());
			task.setToPath(taskmsg.getToPath());
			task.setDataPath(taskmsg.getSourcePath());
			task.setDataType(0);
			if (taskService.add(task)) {
				TaskManager.getInstance().addTask(taskmsg.getTaskID());
			}
		} catch (JMSException e) {
			logger.error("Receiving message error", e);
			return;
		}*/
	}

}

package com.dsw.filetrans.jms.imp;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsw.filetrans.Constants;
import com.dsw.filetrans.StatusFlag;
import com.dsw.filetrans.jms.JMSConsumer;
import com.dsw.filetrans.message.TaskMessage;
import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.service.TaskService;
import com.dsw.filetrans.service.sync.TaskExcute;
import com.dsw.filetrans.util.JsonUtil;

@Service
public class JMSConsumerImp implements JMSConsumer {
	static Logger logger = LogManager.getLogger(JMSConsumerImp.class);
	
	@Autowired
	protected TaskService taskService;
	
	@Autowired
	protected TaskExcute taskExcute;

	@Override
	public void onMessage(Message message) {
		/*try {
			String msg = ((TextMessage) message).getText();
			System.out.println("QueueReceiver接收到消息:" + msg);
			message.acknowledge();//// 手动向broker确认接收成功，如果发生异常，就不反回ack
		} catch (JMSException e) {
			e.printStackTrace();
		} */
		try {
			String text = ((TextMessage) message).getText();
			logger.info("msg:" + text);
			TaskMessage taskmsg = null;
			try {
				taskmsg = JsonUtil.json2Object(text, TaskMessage.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("sourceIP:" + taskmsg.getSourceIP());
			logger.info("toIP:" + taskmsg.getToIP());
			logger.info("sourcePath:" + taskmsg.getSourcePath());
			logger.info("toPath:" + taskmsg.getToPath());
			if (null == taskmsg.getSourceIP() || taskmsg.getSourceIP().trim().equals("")
					|| !taskmsg.getSourceIP().equals(Constants.LocalIP)) {
				logger.info("[TaskID:" + taskmsg.getTaskID() + "]This Task doesn't transfer from here!");
				return;
			}
			//创建taskModel对象
			TaskModel task = new TaskModel();
			task.setCreateTime(new Date());
			task.setId(taskmsg.getTaskID());
			task.setStatus(StatusFlag.TASK_STATUS_WATITING);
			task.setToIP(taskmsg.getToIP());
			task.setToPath(taskmsg.getToPath());
			task.setDataPath(taskmsg.getSourcePath());
			task.setDataType(0);
			try {
				taskExcute.excute(task);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			Future<Integer> future = taskExcute.excute(task);
			/*Integer result = 0;
			while (true) {
	            if (future.isDone()) {
	            	try {
						result = future.get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
	                break;
	            }
	        }
			logger.info("result:" + result);*/
			message.acknowledge();//// 手动向broker确认接收成功，如果发生异常，就不反回ack
		} catch (JMSException e) {
			logger.error("Receiving message error", e);
			return;
		}
	}

}

package com.dsw.filetrans.manager.execute;

import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.dsw.filetrans.Constants;
import com.dsw.filetrans.jms.JMSProducer;
import com.dsw.filetrans.manager.ExceptionManager;
import com.dsw.filetrans.model.ExceptionModel;
import com.dsw.filetrans.service.ExceptionService;
import com.dsw.filetrans.service.imp.ExceptionServiceImpl;


/**
 * 消息发送异常处理类
 * @author Zhuxs
 *
 */
public class ExceptionExecute implements Callable<String>{
	
	static Logger logger = Logger.getLogger(ExceptionExecute.class);
	
	private ExceptionModel nowException;
	
	private int result = -1;
	
	private ExceptionService expSvc = new ExceptionServiceImpl();
	
	private JMSProducer jmsProducer;
	
	private boolean ExceptionStop = false;

	public ExceptionExecute(UUID id) {
		nowException = expSvc.getExpByID(id);
	}
	
	@Override
	public String call() throws Exception {
		Thread.currentThread().setName(ExceptionManager.class.getSimpleName()+ ":Exception(" + nowException.getId() + ")");
		start();		
		return nowException.getId()+"@"+result;
	}
	
	private void start(){
		if(nowException==null){
			return;
		}
		changeStatus(Constants.exceptionStatus.get("executing"));
		onExceptionStart();
		result = sendMessage(nowException.getContent());		
	}
	/**
	 * 发送信息到消息队列
	 * @param message
	 * @return
	 */
	private int sendMessage(String message){
		if(ExceptionStop){
			onExceptionStop();
			return -1;
		}
		boolean sendResult = jmsProducer.send2Queue("", message);
		
		if(sendResult){
			onExceptionSuccess();
			return 0;
		}
		logger.error("Sending Message failed");
		return -1;
		
	}
	/**
	 * 修改exception_info表中的状态
	 * @param status
	 */
	private void changeStatus(int status){
		expSvc.updateStatus(nowException.getId(), status);
	}
	
	private void onExceptionStart(){
		logger.info("Exception Start:"+nowException.toString());
	}
	private void onExceptionSuccess(){
		logger.info("Exception Success:"+nowException.toString());
	}
	private void onExceptionStop(){
		logger.info("Exception Stop:"+nowException.toString());
	}

}

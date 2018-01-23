package com.dsw.filetrans.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dsw.filetrans.Constants;
import com.dsw.filetrans.manager.execute.ExceptionExecute;
import com.dsw.filetrans.manager.queue.ExceptionQueue;
import com.dsw.filetrans.manager.queue.ExceptionQueueCptor;
import com.dsw.filetrans.model.ExceptionModel;
import com.dsw.filetrans.service.ExceptionService;
import com.dsw.filetrans.service.imp.ExceptionServiceImpl;


/**
 * 消息发送异常管理类
 * @author Zhuxs
 *
 */
public class ExceptionManager implements Runnable{
	
	static Logger logger = LogManager.getLogger(ExceptionManager.class);
	
	private static volatile ExceptionManager instance;
	
	private static Object syncRoot = new Object();
	
	private static Object executeLock = new Object();
	
	private static Lock exceptionLock = new ReentrantLock();
	private static Condition exceptionCond = exceptionLock.newCondition();
	
	private static Object syncExceptionList = new Object();
	private List<ExceptionQueue> exceptionList = new ArrayList<ExceptionQueue>();
	//异常处理类的MAP
	private Map<UUID,ExceptionExecute> executeDict = new HashMap<UUID,ExceptionExecute>();
	
	private final ExecutorService expExecPool = Executors.newFixedThreadPool(Constants.MaxExecuteExceptionNum);
	
	private ExceptionService expSvc = new ExceptionServiceImpl();
	
	
	
	public static ExceptionManager getInstance(){
		synchronized (syncRoot) {
			if(null == instance){
				ExceptionManager.instance = new ExceptionManager();
			}
			return ExceptionManager.instance;
		}
	}
	@Override
	public void run() {
		Thread.currentThread().setName(ExceptionManager.class.getSimpleName());
		Future<String> future;
		while(true){
			UUID nextExecuteID;
			do{
				exceptionLock.lock();
					try {
						while(getExceptionQueueStatusNum(Constants.queueStatus.get("started"))>=Constants.MaxExecuteExceptionNum||getExceptionQueueStatusNum(Constants.queueStatus.get("waiting"))==0)
							exceptionCond.await();
					} catch (InterruptedException e) {
						logger.error("ExceptionQueue error",e);
					}finally{
						exceptionLock.unlock();
					}
				nextExecuteID = getNextExceptionExecute();
			}while(nextExecuteID == null);
			future = startException(nextExecuteID);
			String result;
			try {
				logger.info("ExceptionExecute result:----->" +future.get());
			} catch (InterruptedException e1) {
				logger.error("ExceptionExecute error",e1);
			} catch (ExecutionException e1) {
				logger.error("ExceptionExecute error",e1);
			}
			if(future.isDone()){
				try {
					UUID exceptionID;
					result = future.get();
					String[] results = result.split("@");
					exceptionID = UUID.fromString(results[0]);
					if(results[1].equals("0")){//0为成功
						expSvc.updateStatus(exceptionID, Constants.exceptionStatus.get("completed"));
						removeExceptionExecute(exceptionID);
						removeException(exceptionID);					
					}else{
						expSvc.updateStatus(exceptionID, Constants.exceptionStatus.get("waiting"));
						expSvc.updateCreateTime(exceptionID, new Date());	
						removeExceptionExecute(exceptionID);
						removeException(exceptionID);
						addException(exceptionID);
						}
				} catch (InterruptedException e) {
					logger.error("ExceptionManage error",e);
				} catch (ExecutionException e) {
					logger.error("ExceptionManage error",e);
				}	
			}
		}
		
	}
	
	/**
	 * 获取队列中对应状态的异常的数量
	 * @param status
	 * @return
	 */
	private int getExceptionQueueStatusNum(int status){
		int num = 0;
		synchronized (exceptionList) {
			for(ExceptionQueue queue:exceptionList){
				if(queue.getStatus()==status){
					num++;
				}
			}
		}
		return num;
	}
	
	/**
	 * 获取下一个需要处理的异常ID
	 * @return
	 */
	private UUID getNextExceptionExecute(){
		synchronized (syncExceptionList) {
			List<ExceptionQueue> startedExceptions = new ArrayList<ExceptionQueue>();
			for(ExceptionQueue queue:exceptionList){
				if(queue.getStatus() == Constants.queueStatus.get("started")){
					startedExceptions.add(queue);
				}
			}
			for(ExceptionQueue queue:exceptionList){
				if(queue.getStatus() == Constants.queueStatus.get("waiting")&&startedExceptions.size() == 0){
					queue.setStatus(Constants.queueStatus.get("started"));
					return queue.getExceptionID();
				}
			}
		}
		return null;
	}
	
	private Future<String> startException(final UUID id){
		ExceptionExecute execute = new ExceptionExecute(id);
		addExceptionExecute(id, execute);
		return expExecPool.submit(execute);
	}
	
	/**
	 * 添加异常处理线程进MAP中
	 * @param id
	 * @param ee
	 */
	private void addExceptionExecute(UUID id,ExceptionExecute ee){
		
		synchronized (executeLock) {
			if(executeDict.containsKey(id)){
				executeDict.remove(id);
			}
			executeDict.put(id, ee);
		}		
	}
	private void removeExceptionExecute(UUID id){		
		synchronized (executeLock) {
			if(!executeDict.containsKey(id))
				return;
			executeDict.remove(id);
		}		
	}
	
	/**
	 * 根据ID从队列中删除
	 * @param id
	 */
	private void removeException(UUID id){
		synchronized (syncExceptionList) {
			Iterator<ExceptionQueue> it = exceptionList.iterator();
			while(it.hasNext()){
				ExceptionQueue exp = it.next();
				if(exp.getExceptionID().toString().equalsIgnoreCase(id.toString()))
					it.remove();
			}
		}
	}
	/**
	 * 根据ID添加到队列中
	 * @param id
	 */
	public void addException(UUID id){
		ExceptionQueue queue = new ExceptionQueue();
		queue.setExceptionID(id);
		queue.setAddTime(new Date());
		queue.setStatus(Constants.queueStatus.get("waiting"));
		ExceptionModel model = expSvc.getExpByID(id);
		if(null != model){
			queue.setAddTime(model.getCreateTime());
		}
		synchronized (syncExceptionList) {
			exceptionList.add(queue);
			Collections.sort(exceptionList,new ExceptionQueueCptor());
		}
		exceptionLock.lock();
		try {
			exceptionCond.signal();
		} finally {
			exceptionLock.unlock();
		}		
	}
	/**
	 * 添加list到队列中
	 * @param list
	 */
	public void addExceptionList(List<ExceptionModel> list){
		for(ExceptionModel model:list){
			addException(model.getId());
		}
	}
	/**
	 * 初始化队列
	 */
	public void initList(){
		List<ExceptionModel> list = expSvc.getInitExceptionQueue();
		addExceptionList(list);
	}
	
	

}

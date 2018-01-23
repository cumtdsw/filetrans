package com.dsw.filetrans.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dsw.filetrans.Constants;
import com.dsw.filetrans.manager.execute.TaskExecute;
import com.dsw.filetrans.manager.queue.TaskQueue;
import com.dsw.filetrans.manager.queue.TaskQueueCptor;
import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.service.TaskService;



/**
 * 文件传输任务管理类
 * @author Zhuxs
 *
 */
public class TaskManager implements Runnable{
	
	static Logger logger = LogManager.getLogger(TaskManager.class);
	private TaskService taskService;
	
	public TaskManager() {
	}
	
	public TaskManager(TaskService taskService) {
		this.taskService = taskService;
	}
	
	private static Object syncRoot = new Object();
	private static Object executeLock = new Object();
	
	private static Lock taskLock = new ReentrantLock();
	private static Condition taskCond = taskLock.newCondition();
	
	private static Object syncTaskList = new Object();
	private List<TaskQueue> taskList = new ArrayList<TaskQueue>();
	
	private Map<String, TaskExecute> executeDict = new ConcurrentHashMap<String, TaskExecute>();
	
	
	
	private final ExecutorService taskExecPool = Executors.newFixedThreadPool(Constants.MaxExecuteTaskNum);
	
	private static volatile TaskManager instance;	
	
	public static TaskManager getInstance(){
		synchronized (syncRoot) {
			if (TaskManager.instance == null) {
				TaskManager.instance = new TaskManager();
			}
		}
		return TaskManager.instance;
	}
	@Override
	public void run() {
		Thread.currentThread().setName(TaskManager.class.getSimpleName());
		List<Future<String>> results = new ArrayList<Future<String>>();
		while (true) {
			String nextExeTaskID = null;
			do {
				taskLock.lock();
				try {
					while (getTaskQueueStateNum(Constants.queueStatus.get("started")) >= Constants.MaxExecuteTaskNum || getTaskQueueStateNum(Constants.queueStatus.get("waiting")) == 0) {
						taskCond.await();
					}
				} catch (InterruptedException e) {
					logger.error("TaskQueue error",e);
				} finally {
					taskLock.unlock();
				}
				nextExeTaskID = getNextExecuteTask();
			} while (nextExeTaskID == null);
			results.add(startTask(nextExeTaskID));
			while (results.size() == Constants.MaxExecuteTaskNum) {
				Iterator<Future<String>> it = results.iterator();
				while (it.hasNext()) {
					Future<String> future = it.next();
					if (future.isDone()) {
						it.remove();
						String executeResult;
						try {
							String taskID;
							executeResult = future.get();
							String[] executeResults = executeResult.split("@");
							taskID = executeResults[0].trim();
							removeTaskExecute(taskID);
							removeTask(taskID);
						} catch (Exception e) {
							logger.error("Get result of task failed", e);
						}
					}
				}
			}
		}
	}
		
	/**
	 * 获取下一个要执行的任务的ID
	 * @return
	 */
	private String getNextExecuteTask() {
		synchronized (taskList) {
			List<TaskQueue> startedTasks = new ArrayList<TaskQueue>();
			for (TaskQueue taskQueue : taskList) {
				if (taskQueue.getState() == Constants.queueStatus.get("started"))
					startedTasks.add(taskQueue);
			}
			for (TaskQueue taskQueue : taskList) {
				if (taskQueue.getState() == Constants.queueStatus.get("waiting") && (startedTasks.size() < Constants.MaxExecuteTaskNum)) {
					taskQueue.setState(Constants.queueStatus.get("started"));
					return taskQueue.getTaskID();
				}
			}
		}
		return null;
    }
	
	public void initTaskCount(){
		int count = taskService.getInitTaskCount();
		Constants.taskCount = count;
	}
	/**
	 * 获取队列中对应状态的任务的数量
	 * @param state
	 * @return
	 */
	private int getTaskQueueStateNum(int state) {
		int num = 0;
		synchronized (syncTaskList) {
			for (TaskQueue task : taskList) {
				if (task.getState() == state)
					num++;
			}
		}
		return num;
    }
	/**
	 * 初始化任务队列
	 */
	public void initTaskList(){
		List<TaskModel> list = taskService.getInitTaskList();
		if(list.size()>0)
			addTaskList(list);
	}
	/**
	 * 开始任务执行线程
	 * @param taskID
	 * @return
	 */
	public Future<String> startTask(final String taskID) {
		TaskExecute te = new TaskExecute(taskID);
		addTaskExecute(taskID, te);
		return taskExecPool.submit(te);
    }
	/**
	 * 将任务执行线程添加到MAP中
	 * @param taskID
	 * @param te
	 */
	private void addTaskExecute(String taskID, TaskExecute te) {
		synchronized (executeLock) {
			if (executeDict.containsKey(taskID)) {
				executeDict.remove(taskID);
			}
			executeDict.put(taskID, te);
		}
	}
	
	private void removeTaskExecute(String taskID) {
		synchronized (executeLock) {
			if (!executeDict.containsKey(taskID))
				return;
			executeDict.remove(taskID);
		}
	}
	
	
	/**
	 * 通过ID添加任务信息到队列
	 * @param id
	 */
	protected void addTask(String id){
		TaskQueue queue = new TaskQueue();
		queue.setTaskID(id);
		queue.setState(Constants.queueStatus.get("waiting"));
		queue.setAddTime(new Date());
		
		TaskModel task = taskService.getTaskByID(id);
		if(null != task){
			queue.setAddTime(task.getCreateTime());
		}
		synchronized (syncTaskList) {
			Constants.taskCount = Constants.taskCount+1;
			taskList.add(queue);
			Collections.sort(taskList, new TaskQueueCptor());
		}
		logger.info("TaskCount:"+Constants.taskCount+";TaskTotal:"+Constants.taskTotal);
		taskLock.lock();
		try {
			taskCond.signal();
		} finally {
			taskLock.unlock();
		}
	}
	/**
	 * 添加任务信息list到队列
	 * @param tasks
	 */
	public void addTaskList(List<TaskModel> tasks) {
		for (TaskModel taskModel : tasks)
			addTask(taskModel.getId());
	}
	/**
	 * 从队列移除对应ID的任务
	 * @param taskID
	 */
	private void removeTask(String taskID) {
		synchronized (syncTaskList) {
			Iterator<TaskQueue> it = taskList.iterator();
			while (it.hasNext()) {
				TaskQueue task = it.next();
				if (task.getTaskID().toString().equalsIgnoreCase(taskID.toString())) {
					it.remove();
				}
			}
		}
	}

}

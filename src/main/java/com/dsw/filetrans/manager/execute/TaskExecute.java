package com.dsw.filetrans.manager.execute;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dsw.filetrans.Constants;
import com.dsw.filetrans.jms.JMSProducer;
import com.dsw.filetrans.manager.ExceptionManager;
import com.dsw.filetrans.manager.TaskManager;
import com.dsw.filetrans.message.TaskResult;
import com.dsw.filetrans.model.ExceptionModel;
import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.service.ExceptionService;
import com.dsw.filetrans.service.TaskService;
import com.dsw.filetrans.util.JsonUtil;

/**
 * 任务执行线程
 * 
 * @author Zhuxs
 *
 */
public class TaskExecute implements Callable<String> {

	static Logger logger = LogManager.getLogger(TaskExecute.class);

	private TaskModel nowTask;

	private volatile boolean taskStop = false;

	private TaskService taskService;

	private ExceptionService exceptionService;

	private JMSProducer jmsProducer;

	private int result = -1;

	private String errorMsg = "";

	/**
	 * 构造函数，通过ID获取当前执行的任务信息
	 * 
	 * @param id
	 */
	public TaskExecute(String id) {
		nowTask = taskService.getTaskByID(id);
	};

	@Override
	public String call() throws Exception {
		Thread.currentThread().setName(TaskManager.class.getSimpleName() + ":Task(" + nowTask.getId() + ")");
		start();
		return nowTask.getId() + "@" + result;
	}

	private void start() {
		if (nowTask == null)
			return;
		//changeTaskStatus(Constants.taskStatus.get("executing"));
		onTaskStart();
		TaskResult taskResult = new TaskResult();
		result = beginTransfer();
		taskResult.setTaskID(nowTask.getId());
		if (result == 0) {
			onTaskSuccess();
			//changeTaskStatus(Constants.taskStatus.get("completed"));
			taskResult.setResult(0);
			taskResult.setNote("");
		} else {
			onTaskStop();
			//changeTaskStatus(Constants.taskStatus.get("failed"));
			taskResult.setResult(1);
			taskResult.setNote(errorMsg);
		}
		String message = JsonUtil.Object2Json(taskResult);

		boolean sendReuslt = jmsProducer.send2Queue("", message);
		if (!sendReuslt) {
			ExceptionModel model = new ExceptionModel();
			model.setContent(message);
			model.setId(UUID.randomUUID());
			model.setCreateTime(new Date());
			model.setStatus(0);
			if (exceptionService.add(model)) {
				ExceptionManager.getInstance().addException(model.getId());
			}
		}

	}

	private int beginTransfer() {
		if (taskStop)
			return -1;
		String path = nowTask.getDataPath();
		File file = new File(path);
		if (!file.exists()) {
			logger.error("File doesn't exist!");
			errorMsg = "File doesn't exist!";
			return -1;
		}
		String commands[] = { "rsync", "-aR", nowTask.getDataPath(),
				Constants.TOUserName + "@" + nowTask.getToIP() + ":" + nowTask.getToPath() };

		return transfer(commands);
	}

	private int transfer(String[] command) {
		if (command == null || command.equals(""))
			return -1;
		int result = -1;
		Process pcs = null;
		try {
			pcs = Runtime.getRuntime().exec(command);
			errorMsg = getInputStream(pcs.getErrorStream(), "ERROR");
			result = pcs.waitFor();
			if (errorMsg.equals("mkdir successful")) {
				result = 0;
			}
			taskService.updateTime(nowTask.getId(), new Date(), 1);
		} catch (Exception e) {
			logger.error("transfer error", e);
			errorMsg = "Runtime execute ERROR";
		} finally {
			// pcs.destroy();
		}

		return result;
	}

	private String getInputStream(InputStream is, String type) {
		String error = "";
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = "";
			StringBuilder sb = new StringBuilder("");
			while ((line = br.readLine()) != null) {
				if (type.equals("ERROR")) {
					sb.append(line);
					logger.error("Commands execute ERROR:" + line);

				} else {
					logger.info("Commands execute INFO:" + line);
				}
			}
			error = sb.toString();
			if (error.indexOf("No such file or directory") != -1) {
				error = mkDir(nowTask.getToPath());
				beginTransfer();
			}
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			logger.error("getInputStream error", e);
		}
		return error;
	}

	private String mkDir(String path) {
		String mkDirCommand = "ssh " + Constants.TOUserName + "@" + nowTask.getToIP() + " mkdir -p " + path;
		Process pcs = null;
		String msg = "";
		try {
			pcs = Runtime.getRuntime().exec(mkDirCommand);
			getInputStream(pcs.getInputStream(), "INPUT");
			getInputStream(pcs.getErrorStream(), "ERROR");
			result = pcs.waitFor();
			if (result == 0) {
				msg = "mkdir successful";
			}
		} catch (Exception e) {
			logger.error("make DIR error", e);
			errorMsg = "Runtime execute ERROR";
		} finally {
			// pcs.destroy();
		}
		return msg;
	}

	private void changeTaskStatus(int status) {
		taskService.updateStatus(nowTask.getId(), status);
		/*if (status != Constants.taskStatus.get("executing")) {
			return;
		}*/
		taskService.updateTime(nowTask.getId(), new Date(), 0);
	}

	private void onTaskStart() {
		logger.info("Task Start:" + nowTask.toString());
	}

	private void onTaskSuccess() {
		logger.info("Task Success:" + nowTask.toString());
	}

	private void onTaskStop() {
		logger.info("Task Stop:" + nowTask.toString());
	}

}

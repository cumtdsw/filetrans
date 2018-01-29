package com.dsw.filetrans.service.sync.imp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.dsw.filetrans.Constants;
import com.dsw.filetrans.StatusFlag;
import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.service.TaskService;
import com.dsw.filetrans.service.sync.TaskExcute;

@Service
public class TaskExcuteImp implements TaskExcute {
	private static Logger logger = LogManager.getLogger(TaskExcuteImp.class);

	@Autowired
	private TaskService taskService;

	@Async
	@Override
	public Future<Integer> excute(TaskModel taskModel) {
		logger.info("now enter TaskExcuteImp.excute, taskId:" + taskModel.getId());
		Integer result = -2;
		TaskModel taskModelExisting = taskService.getTaskByID(taskModel.getId());
		if (taskModelExisting == null) {
			taskService.add(taskModel);
		}
		// 1.记录任务信息
		taskService.updateStatus(taskModel.getId(), StatusFlag.TASK_STATUS_EXCUTING);
		// 2.相关参数条件判断
		String path = taskModel.getDataPath();
		File file = new File(path);
		logger.info("file is directory? :" + file.isDirectory());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File fileIn : files) {
				logger.info("fileName:" + fileIn.getName());
			}
		}
		if (!file.exists()) {
			// 如果文件不存在，更新任务执行状态为失败
			taskService.updateStatus(taskModel.getId(), StatusFlag.TASK_STATUS_FAILED);
			logger.error("File doesn't exist!");
			return new AsyncResult<Integer>(-1);
		}
		String commands[] = { "rsync", "-aR", taskModel.getDataPath(),
				Constants.TOUserName + "@" + taskModel.getToIP() + ":" + taskModel.getToPath() };
		result = transfer(commands, taskModel);
		if (result != 0) {
			// 如果执行结果不为0，更新任务状态为失败
			taskService.updateStatus(taskModel.getId(), StatusFlag.TASK_STATUS_FAILED);
		} else {
			taskService.updateStatus(taskModel.getId(), StatusFlag.TASK_STATUS_COMPLETED);
		}

		logger.info("now leave TaskExcuteImp.excute, taskId:" + taskModel.getId());
		return new AsyncResult<Integer>(result);
	}

	private int transfer(String[] command, TaskModel taskModel) {
		if (command == null || command.equals(""))
			return -1;
		int result = -1;
		Process pcs = null;
		String errorMsg;
		try {
			pcs = Runtime.getRuntime().exec(command);
			errorMsg = getInputStream(pcs.getErrorStream(), "ERROR", taskModel);
			result = pcs.waitFor();
			if (errorMsg.equals("mkdir successful")) {
				result = 0;
			}
			taskService.updateTime(taskModel.getId(), new Date(), 1);
		} catch (Exception e) {
			logger.error("transfer error", e);
			errorMsg = "Runtime execute ERROR";
		} finally {
			// pcs.destroy();
		}
		return result;
	}

	private String getInputStream(InputStream is, String type, TaskModel taskModel) throws Exception {
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
				logger.info(taskModel.getToPath() + " is not exist, now make this directory.");
				error = mkDir(taskModel.getToPath(), taskModel);
				excute(taskModel);
			}
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			logger.error("getInputStream error", e);
		}
		return error;
	}

	private String mkDir(String path, TaskModel taskModel) {
		String mkDirCommand = "ssh " + Constants.TOUserName + "@" + taskModel.getToIP() + " mkdir -p " + path;
		Process pcs = null;
		String msg = "";
		int result = -1;
		try {
			pcs = Runtime.getRuntime().exec(mkDirCommand);
			getInputStream(pcs.getInputStream(), "INPUT", taskModel);
			getInputStream(pcs.getErrorStream(), "ERROR", taskModel);
			result = pcs.waitFor();
			if (result == 0) {
				msg = "mkdir successful";
				logger.info(msg);
			}
		} catch (Exception e) {
			logger.error("make DIR error", e);
		} finally {
			// pcs.destroy();
		}
		return msg;
	}

}

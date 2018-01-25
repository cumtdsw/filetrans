package com.dsw.filetrans.service.sync;

import java.util.concurrent.Future;

import com.dsw.filetrans.model.TaskModel;

public interface TaskExcute {
	
	/**
	 * 异步方法
	 * @param taskModel
	 * @return
	 */
	public Future<Integer> excute(TaskModel taskModel);

}

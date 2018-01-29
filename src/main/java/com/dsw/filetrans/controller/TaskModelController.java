package com.dsw.filetrans.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.query.condition.TaskModelCondition;
import com.dsw.filetrans.query.result.QueryResult;
import com.dsw.filetrans.service.TaskService;
import com.dsw.filetrans.util.JsonUtil;

@RequestMapping("task")
@Controller
public class TaskModelController {

	@Autowired
	protected TaskService taskService;

	@RequestMapping(value = "query", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public QueryResult queryTask(TaskModelCondition condition) {
		QueryResult result = taskService.queryTask(condition);
		return result;
	}

	@RequestMapping(value = "addTask", method = RequestMethod.POST, consumes="application/json;charset=UTF-8")
	@ResponseBody
	public String addTask(@RequestBody String json) {
		TaskModel taskModel = null;
		try {
			taskModel = JsonUtil.json2Object(json, TaskModel.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean result = taskService.startTask(taskModel);
		if (result)
			return "SUCCESS";
		return "FAILED";
	}
	
	@RequestMapping(value = "reSend", method = RequestMethod.GET)
	@ResponseBody
	public String reSendTask(String taskId) {
		TaskModel taskModel = taskService.getTaskByID(taskId);
		boolean result = false;
		if (taskModel != null) {
			result = taskService.startTask(taskModel);
		}
		if (result)
			return "SUCCESS";
		return "FAILED";
	}

	/*
	 * @RequestMapping("query") public ModelAndView queryTask(TaskModelCondition
	 * condition) {
	 * 
	 * ModelAndView mav = new ModelAndView(); mav.setViewName("2"); QueryResult
	 * result = taskService.queryTask(condition); mav.addObject(result);
	 * 
	 * return mav; }
	 */

}

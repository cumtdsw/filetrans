package com.dsw.filetrans.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.dsw.filetrans.model.ExceptionModel;

public interface ExceptionService {

	/**
	 * 添加异常
	 * @param model
	 * @return
	 */
	public boolean add(ExceptionModel model);
	/**
	 * 更新异常
	 * @param model
	 * @return
	 */
	public boolean update(ExceptionModel model);
	/**
	 * 根据异常ID更新异常状态
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean updateStatus(UUID id,int status);
	/**
	 * 系统初始化时获取异常列表
	 * @return
	 */
	public List<ExceptionModel> getInitExceptionQueue();
	/**
	 * 根据异常ID获取异常
	 * @param id
	 * @return
	 */
	public ExceptionModel getExpByID(UUID id);
	/**
	 * 根据ID更新异常创建时间
	 * @param id
	 * @param time
	 * @return
	 */
	public boolean updateCreateTime(UUID id,Date time);
	

}

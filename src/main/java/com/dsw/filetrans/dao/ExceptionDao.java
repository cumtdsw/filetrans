package com.dsw.filetrans.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.dsw.filetrans.model.ExceptionModel;
/**
 * 异常DAO
 * @author Zhuxs
 *
 */
public interface ExceptionDao {
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
	 * 根据状态获取异常列表
	 * @param status
	 * @return
	 */
	public List<ExceptionModel> getExceptionListByStatus(int status);
	/**
	 * 获取初始化时的异常列表
	 * @return
	 */
	public List<ExceptionModel> getInitExcpetionQueue();
	/**
	 * 将ResultSet转化成异常列表
	 * @param rs
	 * @return
	 */
	public List<ExceptionModel> dataTableToList();

	/**
	 * 根据条件更新状态
	 * @param where
	 * @param status
	 * @return
	 */
	public boolean updateStatus(String where, int status);
	/**
	 * 根据ID获取异常
	 * @param id
	 * @return
	 */
	public ExceptionModel getExceptionByID(UUID id);
	/**
	 * 根据条件更新创建时间
	 * @param where
	 * @param time
	 * @return
	 */
	public boolean updateCreateTime(String where,Date time);
}

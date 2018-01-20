package com.dsw.filetrans.dao.imp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.dsw.filetrans.dao.AbstractDao;
import com.dsw.filetrans.dao.TaskDao;
import com.dsw.filetrans.model.ExceptionModel;
import com.dsw.filetrans.model.TaskModel;

@Repository
public class TaskDaoImpl extends AbstractDao implements TaskDao {
	
	static Logger logger = LogManager.getLogger(TaskDaoImpl.class);

	@Override
	public boolean add(TaskModel model) {
		persist(model);
		return true;
	}
	@Override
	public boolean updateStatus(String where, int status) {
		Query query = getSession().createSQLQuery("update task_info set status = " + status + " where " + where);
		query.executeUpdate();
		return true;
	}

	@Override
	public TaskModel getTaskByID(String id) {
		Criteria criteria = getSession().createCriteria(TaskModel.class);
		criteria.add(Restrictions.eq("id", id));
		return (TaskModel) criteria.uniqueResult();
	}

	@Override
	public boolean update(TaskModel model) {
		getSession().update(model);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaskModel> dataTableToList() {
		Criteria criteria = getSession().createCriteria(ExceptionModel.class);
		criteria.add(Restrictions.in("status", new Object[] { 0, 1 }));
		return (List<TaskModel>) criteria.list();
	
	}
	
	@Override
	public boolean updateStartTime(String taskID, Date time) {
		Timestamp sqlTime = new Timestamp(time.getTime());
		Query query = getSession().createSQLQuery("update task_info set startTime = '" + sqlTime + "' where ID = '" + taskID.toString() + "'");
		query.executeUpdate();
		return true;
	}

	@Override
	public boolean updateEndTime(String taskID, Date time) {
		Timestamp sqlTime = new Timestamp(time.getTime());
		Query query = getSession().createSQLQuery("update task_info set endTime = '" + sqlTime + "' where ID = '" + taskID.toString() + "'");
		query.executeUpdate();
		return true;	
	}
	
	public int querySize(String sql) {
		Query query = getSession().createQuery(sql);
		return query.list().size();
	}

}

package com.dsw.filetrans.dao.imp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.dsw.filetrans.dao.AbstractDao;
import com.dsw.filetrans.dao.TaskDao;
import com.dsw.filetrans.model.ExceptionModel;
import com.dsw.filetrans.model.TaskModel;
import com.dsw.filetrans.query.condition.TaskModelCondition;
import com.dsw.filetrans.query.result.QueryResult;

@Repository
public class TaskDaoImpl extends AbstractDao implements TaskDao {

	static Logger logger = LogManager.getLogger(TaskDaoImpl.class);

	@Override
	public boolean add(TaskModel taskModel) {
		persist(taskModel);
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
	public boolean update(TaskModel taskModel) {
		getSession().update(taskModel);
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
		Query query = getSession().createSQLQuery(
				"update task_info set startTime = '" + sqlTime + "' where ID = '" + taskID.toString() + "'");
		query.executeUpdate();
		return true;
	}

	@Override
	public boolean updateEndTime(String taskID, Date time) {
		Timestamp sqlTime = new Timestamp(time.getTime());
		Query query = getSession().createSQLQuery(
				"update task_info set endTime = '" + sqlTime + "' where ID = '" + taskID.toString() + "'");
		query.executeUpdate();
		return true;
	}

	public int querySize(String sql) {
		Query query = getSession().createQuery(sql);
		return query.list().size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public QueryResult query(TaskModelCondition condition) {
		if (condition == null) {
			logger.error("condition is null");
			return new QueryResult();
		}
		
		logger.info("isPaged is:" + condition.isPaged());
		logger.info("currPage is:" + condition.getCurrPage());
		logger.info("pageSize is:" + condition.getPageSize());
		logger.info("maxPages is:" + condition.getMaxPages());
		logger.info("isQueryTotalPages is:" + condition.isQueryTotalPages());
		
		logger.info("id is:" + condition.getId());
		logger.info("startTime is:" + condition.getStartTime());
		logger.info("endTime is:" + condition.getEndTime());;
		logger.info("status is:" + condition.getStatus());
		logger.info("dataType is:" + condition.getDataType());
		
		QueryResult result = new QueryResult();
		
		Session session = getSession();
		Criteria criteria = session.createCriteria(TaskModel.class);
		if (null != condition.getId() && ! "".equals(condition.getId())) {
			criteria.add(Restrictions.eq("id", condition.getId()));
		}
		if (null != condition.getStartTime()) {
			criteria.add(Restrictions.ge("startTime", condition.getStartTime()));
		}
		if (null != condition.getEndTime()) {
			criteria.add(Restrictions.le("startTime", condition.getEndTime()));
		}
		if (null != condition.getStatus() && ! "".equals(condition.getStatus())) {
			criteria.add(Restrictions.eq("status", condition.getStatus()));
		}
		
		Long totalCount = (Long) criteria.setProjection(Projections.count("id")).uniqueResult();
		result.setTotalCount(totalCount);
		criteria.setProjection(null);
		if (!condition.isPaged()) {
			result.setResults(criteria.list());
		}
		int currentPage = condition.getCurrPage();
		int pageSize = condition.getPageSize();
		int maxPages = (condition.getMaxPages() <= 0 ? 10 : condition.getMaxPages());
		int startPage = currentPage / maxPages * maxPages + (currentPage % maxPages == 0 ? -(maxPages - 1) : 1);

		int first = (startPage - 1) * pageSize;
		criteria.setFirstResult(first);
		criteria.setMaxResults(pageSize * (maxPages + 1));
		List<TaskModel> listRtn = criteria.list();

		// SplitFirstModel sf = new SplitFirstModel();
		int nCount = listRtn.size();
		int nPageNumber = nCount % pageSize > 0 ? (nCount / pageSize + 1) : (nCount / pageSize);

		// set paged info
		result.setTotalPages(nPageNumber + startPage - 1);
		result.setCurrPage(currentPage);
		result.setIsPaged(true);
		result.setPageSize(pageSize);
		result.setStartPage(startPage);
		int endPage = startPage + nPageNumber - 1;
		endPage = endPage > (startPage + maxPages - 1) ? (startPage + maxPages - 1) : endPage;
		result.setEndPage(endPage);
		result.setMaxPages(maxPages);

		int fromIdx = (currentPage - startPage) * pageSize;
		fromIdx = (fromIdx > nCount ? nCount : fromIdx);
		int toIdx = fromIdx + pageSize;
		toIdx = (toIdx > nCount ? nCount : toIdx);
		for (int i = fromIdx; i < toIdx; ++i) {
			result.addResult(listRtn.get(i));
		}

		return result;
	}

}

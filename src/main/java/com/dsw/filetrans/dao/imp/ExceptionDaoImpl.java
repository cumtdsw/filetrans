package com.dsw.filetrans.dao.imp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dsw.filetrans.dao.AbstractDao;
import com.dsw.filetrans.dao.ExceptionDao;
import com.dsw.filetrans.model.ExceptionModel;

@Repository
public class ExceptionDaoImpl extends AbstractDao implements ExceptionDao {

	static Logger logger = LogManager.getLogger(ExceptionDaoImpl.class);

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Override
	public boolean add(ExceptionModel model) {
		persist(model);
		return true;

	}

	@Override
	public boolean update(ExceptionModel model) {
		getSession().update(model);
		return true;

	}

	@Override
	public List<ExceptionModel> getExceptionListByStatus(int status) {

		return null;
	}

	@Override
	public List<ExceptionModel> getInitExcpetionQueue() {

		return null;
	}

	@Override
	public boolean updateStatus(String where, int status) {
		Query query = getSession().createSQLQuery("update exception_info set status = " + status + " where " + where);
		query.executeUpdate();
		return true;
	}

	@Override
	public ExceptionModel getExceptionByID(UUID id) {
		Criteria criteria = getSession().createCriteria(ExceptionModel.class);
		criteria.add(Restrictions.eq("id", id));
		return (ExceptionModel) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExceptionModel> dataTableToList() {
		Criteria criteria = getSession().createCriteria(ExceptionModel.class);
		criteria.add(Restrictions.in("status", new Object[] { 0, 1 }));
		return (List<ExceptionModel>) criteria.list();
	}

	@Override
	public boolean updateCreateTime(String where, Date time) {
		Timestamp sqlTime = new Timestamp(time.getTime());
		Query query = getSession().createSQLQuery("update exception_info set createTime = '" + sqlTime + "' where " + where);
		query.executeUpdate();
		return true;
	}


}

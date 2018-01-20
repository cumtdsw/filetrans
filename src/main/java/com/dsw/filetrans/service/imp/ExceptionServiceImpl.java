package com.dsw.filetrans.service.imp;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsw.filetrans.dao.ExceptionDao;
import com.dsw.filetrans.model.ExceptionModel;
import com.dsw.filetrans.service.ExceptionService;

@Service
public class ExceptionServiceImpl implements ExceptionService {
	
	@Autowired
	protected ExceptionDao exceptionDao;

	@Override
	public boolean add(ExceptionModel model) {
		UUID id = model.getId();
		ExceptionModel hasModel = exceptionDao.getExceptionByID(id);
		if(hasModel==null){
			return exceptionDao.add(model);
		}
		return exceptionDao.update(model);
	}

	@Override
	public boolean update(ExceptionModel model) {
		return exceptionDao.update(model);
	}

	@Override
	public boolean updateStatus(UUID id, int status) {
		String where = " ID = '"+id+"'";
		return exceptionDao.updateStatus(where, status);
	}

	@Override
	public List<ExceptionModel> getInitExceptionQueue() {
		return exceptionDao.dataTableToList();
	}
	
	@Override
	public ExceptionModel getExpByID(UUID id){
		return exceptionDao.getExceptionByID(id);
	}

	@Override
	public boolean updateCreateTime(UUID id, Date time) {
		String where = " ID = '"+id+"'";
		return exceptionDao.updateCreateTime(where, time);
	}

}

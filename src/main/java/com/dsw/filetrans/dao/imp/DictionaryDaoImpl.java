package com.dsw.filetrans.dao.imp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.dsw.filetrans.dao.AbstractDao;
import com.dsw.filetrans.dao.DictionaryDao;

@Repository
public class DictionaryDaoImpl extends AbstractDao implements DictionaryDao {

	static Logger logger = LogManager.getLogger(DictionaryDaoImpl.class);

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	public Map<String, Map<String, Integer>> getDicMap() {
		final Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
		String sql = "select dict.name,dict.value,dictdata.dataName,dictdata.dataValue from dictionary dict,dictionary_data dictdata where dict.value = dictdata.dictValue;";
		jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet resultSet) throws SQLException {
				Map<String, Integer> dataMap = new HashMap<String, Integer>();
				String dictName = resultSet.getString("name");
				String dataName = resultSet.getString("dataName");
				int dataValue = Integer.parseInt(resultSet.getString("dataValue"));
				dataMap.put(dataName, dataValue);
				if (map.containsKey(dictName)) {
					map.get(dictName).put(dataName, dataValue);
				} else {
					map.put(dictName, dataMap);
				}
			}
		});
		return map;
	}

}

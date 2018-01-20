package com.dsw.filetrans.dao;

import java.util.Map;
/**
 * 字典DAO
 * @author Zhuxs
 *
 */
public interface DictionaryDao {

	/**
	 * 获取字典数据
	 * @return
	 */
	public Map<String, Map<String, Integer>> getDicMap();
	
}

package com.dsw.filetrans.service.init.imp;

import java.io.FileNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.dsw.filetrans.Constants;
import com.dsw.filetrans.dao.DictionaryDao;
import com.dsw.filetrans.service.init.InitDataService;
import com.dsw.filetrans.util.Dom4jUtil;

public class InitDataServiceImp implements InitDataService {
	private static Logger logger = LogManager.getLogger(InitDataServiceImp.class);

	@Autowired
	protected DictionaryDao dictionaryDao;
	
	@Override
	public void constantsInit() {
		logger.info("init constantsInit.................");
		Document doc = null;
		try {
			doc = Dom4jUtil.getDocument("config.xml");
		} catch (DocumentException | FileNotFoundException e) {
			e.printStackTrace();
		}
		Element rootElement = doc.getRootElement();

		Constants.LocalIP = rootElement.element("Basic").element("LocalIP").getTextTrim();
		logger.info("LocalIP: " + Constants.LocalIP);

		Constants.TOUserName = rootElement.element("Basic").element("TOUserName").getTextTrim();
		logger.info("TOUserName: " + Constants.TOUserName);
		Constants.MQURL = rootElement.element("Basic").element("MQURL").getTextTrim();
		logger.info("MQURL: " + Constants.MQURL);



	}

}

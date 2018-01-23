package com.dsw.filetrans.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlUtil {

	private static Logger logger = LogManager.getLogger(XmlUtil.class);

	static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

	/**
	 * 解析文件路径下的文件（绝对路径或相对路径)
	 * @param filePath :文件路径
	 * @return
	 */
	public static Document parseFileInFileSystemPath(String filePath) {
		logger.info("filePath: " + filePath);
		Document document = null;
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			document = builder.parse(new File(filePath));
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage(), e);
		} catch (SAXException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return document;
	}
	
	/**
	 * 解析classpath下的文件，(src/main/resource)
	 * @param fileName 文件名称
	 * @return
	 */
	public static Document parseFileInClassPath(String fileName) {
		File file = FileUtil.getFile(fileName);
		Document document = null;
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			document = builder.parse(file);
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage(), e);
		} catch (SAXException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return document;
	}
	
}

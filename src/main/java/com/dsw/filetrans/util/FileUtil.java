package com.dsw.filetrans.util;

import java.io.File;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtil {
	private static Logger logger = LogManager.getLogger(FileUtil.class);
	/**
	 * 读取classpath下的文件（src/main/resources)
	 * @param fileName
	 */
	public static File getFile(String fileName) {
		logger.info("fileName:" + fileName);
		ClassLoader classLoader = FileUtil.class.getClassLoader();
		/**
		 * getResource()方法会去classpath下找这个文件，获取到url resource,
		 * 得到这个资源后，调用url.getFile获取到 文件 的绝对路径
		 */
		URL url = classLoader.getResource(fileName);
		/**
		 * url.getFile() 得到这个文件的绝对路径
		 */
		logger.info("文件存在的绝对路径: " + url.getFile());
		System.out.println(url.getFile());
		File file = new File(url.getFile());
		logger.info("文件是否存在:" + file.exists());
		return file;
	}

}

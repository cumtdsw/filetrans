package com.dsw.filetrans.util;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 对dom4j打包一些常用的方法
 */
public class Dom4jUtil {

	public static void main(String[] args) throws DocumentException {
		Document doc = getDocument("/config.xml");
		print(doc);
		System.out.println("root:" + doc.getRootElement().element("Basic").element("MQURL").getText());
	}
	
	/**
	 * 通过Path获得Dom4j的Document对象 Document对象代表整个xml文件
	 * path是标准的URI，如"/myxml.xml"表示classpath根目录下的myxml.xml
	 * 
	 * @throws DocumentException
	 */
	public static Document getDocument(String path) throws DocumentException {
		// 产生一个解析器对象
		SAXReader reader = new SAXReader();
		// 读取对应的xml文件
		InputStream input = Class.class.getResourceAsStream(path);
		// 将xml文档转换为Document的对象
		return reader.read(input);
	}
	
	public static Document getDocumentFromString(String str) throws DocumentException {
		SAXReader reader = new SAXReader();
		return reader.read(new ByteArrayInputStream(str.getBytes()));
	}

	/**
	 * 将Document写入到xml文件中
	 * 
	 * @throws IOException
	 */
	public static void writeDocument(Document document, String path)
			throws IOException {
		XMLWriter writer = new XMLWriter(new FileOutputStream(path));
		writer.write(document);
		writer.close();
	}

	public static void println(Object obj) {
		System.out.println(obj);
	}

	/**
	 * 打印document 一个Document表示一份xml文档
	 * 此时相当于以原本xml格式显示
	 */
	public static void print(Document document) {
		// 以XML格式输出Document
		println("-----------Document As XML------------");
		println(document.asXML());
		println("--------------------------------------");
	}

	/**
	 * 打印Element 一个Element表示XML的以某个结点为根的树
	 */
	public static void print(Element element) {
		println(element.asXML());
	}
	
}

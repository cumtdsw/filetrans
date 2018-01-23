package com.dsw.filetrans;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;


public class Constants {
	static Logger logger = LogManager.getLogger(Constants.class);

	/** 本地IP */
	public static String LocalIP;
	/** 中心节点IP */
	public static String MasterIP;
	/** 最大同时执行任务数 */
	public static int MaxExecuteTaskNum;
	/** 最大消息重发次数 */
	public static int MaxMessageRetryNum;
	/** MQ服务的URL*/
	public static String MQURL;	
	/** 发送到的服务器用户名*/
	public static String TOUserName;
	/** 最大处理异常线程数 */
	public static int MaxExecuteExceptionNum;
	/**	消息接收者的clientID */
	public static String cliendID;
	
	public static String XBasic = "Basic";
	public static String XTask = "Task";
    public static String XAdvanced = "Advanced";
    public static String XDefault = "Default";
    public static Element AllElement;//根节点getRootElement();
    public static String XmlPath;
	/**
	 * 任务状态。0:waiting;1:executing;2:completed;3:failed;
	 */
    public static Map<String,Integer> taskStatus; 
    /**
     * 消息发送异常状态。0:waiting;1:executing;2:completed;
     */
    public static Map<String,Integer> exceptionStatus;
    /**
     * 管理队列中的状态0:waiting;1:started;
     */
    public static Map<String,Integer> queueStatus;
    /**
     * 传输文件的路径类型0:file;1:file group;2:folder;
     */
    public static Map<String,Integer> dataType;
    public static String os;
    public static Document doc;//java新增变量
    
    public static int folderSize;
    public static int taskCount;
    public static int taskTotal;
    
    public static String realPath;   
    
}

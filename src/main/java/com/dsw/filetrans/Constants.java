package com.dsw.filetrans;

public class Constants {

	/** 本地IP */
	public static String LocalIP;

	/** MQ服务的URL*/
	public static String MQURL;	
	
	/** 发送到的服务器用户名*/
	public static String TOUserName;
	
	/**文件上传消息头**/
	public static String HTTP_HEAD_TASKMODEL = "TASKMODEL";
	
	/**任务传输状态**/
	public static int TASK_STATUS_WATITING = 0;
	public static int TASK_STATUS_EXCUTING = 1;
	public static int TASK_STATUS_COMPLETED = 2;
	public static int TASK_STATUS_FAILED = 3;
    
}

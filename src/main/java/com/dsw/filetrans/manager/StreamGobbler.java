package com.dsw.filetrans.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 读取输入流并打印
 * @author Zhuxs
 *
 */
public class StreamGobbler implements Callable<String>{
	
	static Logger logger = LogManager.getLogger(StreamGobbler.class);

	InputStream is;
	String type;
	
	public StreamGobbler(InputStream is,String type){
		this.is = is;
		this.type = type;
	}
	
	public void run(){
		
	}

	@Override
	public String call() throws Exception {
		String error = "";
		try {			
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = "";
			StringBuilder sb = new StringBuilder("");
			while((line = br.readLine())!=null){
				if(type.equals("[FILETRANFER ERROR]")){
					sb.append(line);
					logger.error("Commands execute ERROR:"+line);
					
				}else{
					logger.info("Commands execute INFO:"+line);
				}
			}
			error = sb.toString();
			br.close();
			isr.close();
			is.close();				
			} catch (IOException e) {
				logger.error("getInputStream error",e);
			}
			return error;
	}

	
	
	
}

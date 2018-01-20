package com.dsw.filetrans.util;

import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	

	public static boolean isPM(){
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE,00);
		calendar.set(Calendar.SECOND, 0);
		Date time = calendar.getTime();
		if(now.getTime()>time.getTime()){
			return true;
		}
		return false;
	}
	
	public static Date getTime(int hour){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE,00);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	
}

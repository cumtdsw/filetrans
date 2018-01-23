package com.dsw.filetrans.manager.queue;

import java.util.Comparator;
/**
 * Exception队列排序
 * @author Zhuxs
 *
 */
public class ExceptionQueueCptor implements Comparator<ExceptionQueue>{

	@Override
	public int compare(ExceptionQueue o1, ExceptionQueue o2) {
		if (o1 == null && o2 != null) {
			return -1;
		}
		
		if (o1 != null && o2 == null) {
			return 1;
		}
		
		if (o1 != null && o2 != null) {
			
				if (o1.getAddTime() == null && o2.getAddTime() == null) {
					return 0;
				}
				
				if (o1.getAddTime() == null && o2.getAddTime() != null) {
					return -1;
				}
				
				if (o1.getAddTime() != null && o2.getAddTime() == null) {
					return 1;
				}
				
				return o1.getAddTime().compareTo(o2.getAddTime());
		}
		
		return 0;
	}

}

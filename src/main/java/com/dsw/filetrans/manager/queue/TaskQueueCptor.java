package com.dsw.filetrans.manager.queue;

import java.util.Comparator;

/**
 * task队列排序
 * @author Zhuxs
 *
 */
public class TaskQueueCptor implements Comparator<TaskQueue> {

	@Override
	public int compare(TaskQueue o1, TaskQueue o2) {
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
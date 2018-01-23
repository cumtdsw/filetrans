package com.dsw.filetrans.jms;

public interface JMSProducer {
	public boolean send2Queue(String queueName, final String message);
	public boolean send2Topic(String topicName, final String message);
}

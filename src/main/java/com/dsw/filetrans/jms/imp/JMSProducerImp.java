package com.dsw.filetrans.jms.imp;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.dsw.filetrans.jms.JMSProducer;

@Service
public class JMSProducerImp implements JMSProducer {

	@Autowired
	protected JmsTemplate jmsQueueTemplate;// queue
	@Autowired
	protected JmsTemplate jmsTopicTemplate;// topic

	@Override
	public void send2Queue(String queueName, final String message) {
		jmsQueueTemplate.send(queueName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	}

	@Override
	public void send2Topic(String topicName, final String message) {
		jmsTopicTemplate.send(topicName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});

	}

}

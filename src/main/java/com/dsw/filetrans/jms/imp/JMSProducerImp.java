package com.dsw.filetrans.jms.imp;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.dsw.filetrans.jms.JMSProducer;

/**
 * 消息发送接口
 * @author dsw
 *
 */
@Service
public class JMSProducerImp implements JMSProducer {

	@Autowired
	protected JmsTemplate jmsQueueTemplate;// queue
	@Autowired
	protected JmsTemplate jmsTopicTemplate;// topic

	@Override
	public boolean send2Queue(String queueName, final String message) {
		try {
			jmsQueueTemplate.send(queueName, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(message);
				}
			});
			return true;
		} catch (JmsException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean send2Topic(String topicName, final String message) {
		try {
			jmsTopicTemplate.send(topicName, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(message);
				}
			});
			return true;
		} catch (JmsException e) {
			e.printStackTrace();
			return false;
		}

	}

}

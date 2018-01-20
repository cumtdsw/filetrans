package com.dsw.filetrans.jms.imp;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.stereotype.Service;

import com.dsw.filetrans.jms.JMSConsumer;

@Service
public class JMSConsumerImp implements JMSConsumer{

	@Override
	public void onMessage(Message message) {
		try {
            System.out.println("QueueReceiver2接收到消息:"+((TextMessage)message).getText());
            message.acknowledge();////手动向broker确认接收成功，如果发生异常，就不反回ack
        } catch (JMSException e) {
            e.printStackTrace();
        }
		
	}

}

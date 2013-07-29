package de.blogspot.wrongtracks.prost.msg.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import de.blogspot.wrongtracks.prost.msg.api.MessageToClientService;

public class MessageToClientServiceImpl implements MessageToClientService {

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory factory;
	@Resource(mappedName = "java:/deg/topic/gui/update")
	private Topic topic;
	private static final Logger logger = Logger
			.getLogger(MessageToClientServiceImpl.class.getName());
	private static final String CONNECTION_USER = "prozessstMessaging";
	private static final String CONNECTION_PWD = "messaging";

	public void sendGuiUpdateMessage() {
		Connection connection = null;
		try {
			connection = factory.createConnection(CONNECTION_USER,
					CONNECTION_PWD);
			connection.start();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(topic);
			Message message = session.createMessage();
			logger.log(
					Level.FINE,
					"Sending message: " + message.toString() + " with ID: "
							+ message.getJMSMessageID() + " and topic: "
							+ topic.getTopicName());
			messageProducer.send(message);
			messageProducer.close();
			session.close();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				connection.stop();
				connection.close();
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}

	}

}

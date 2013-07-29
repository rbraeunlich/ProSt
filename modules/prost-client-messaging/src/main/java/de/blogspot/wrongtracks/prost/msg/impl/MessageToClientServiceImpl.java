package de.blogspot.wrongtracks.prost.msg.impl;

import java.io.IOException;
import java.util.Properties;
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
	private Properties props = new Properties();
	private static final String CONNECTION_USER = "connectionUser";
	private static final String CONNECTION_PWD = "connectionPwd";
	private static Connection connection;

	public void init() {
		Connection connection = null;
		try {
			props.load(MessageToClientServiceImpl.class
					.getResourceAsStream("connection.properties"));
			connection = factory.createConnection(
					props.getProperty(CONNECTION_USER),
					props.getProperty(CONNECTION_PWD));
			connection.start();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void destroy() {
		try {
			connection.stop();
			connection.close();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendGuiUpdateMessage() {
		try {
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(topic);
			Message message = session.createMessage();
			logger.log(Level.FINE, "Sending message: " + message.toString()
					+ " with ID: " + message.getJMSMessageID() + " and topic: "
					+ topic.getTopicName());
			messageProducer.send(message);
			messageProducer.close();
			session.close();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}

	}

}

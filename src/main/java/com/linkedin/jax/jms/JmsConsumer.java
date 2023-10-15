package com.linkedin.jax.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Logger;

/**
 * Message-Driven Bean implementation class for: JmsConsumer
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "/jms/queue/HsportsQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")}, mappedName = "/jms/queue/HsportsQueue")
public class JmsConsumer implements MessageListener {
    private Logger logger = Logger.getLogger(JmsConsumer.class.getName());

    /**
     * Default constructor.
     */
    public JmsConsumer() {
    }

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        logger.info("From JMS Consumer MDB:");

        try {
            logger.info(message.getBody(String.class));
        } catch (JMSException e) {
            logger.warning(e.getMessage());
        }
    }

}

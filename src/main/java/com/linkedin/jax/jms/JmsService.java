package com.linkedin.jax.jms;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.util.logging.Logger;

@ApplicationScoped
public class JmsService {

    private Logger logger = Logger.getLogger(JmsService.class.getName());

    // jms queue that we added via jboss cli messaging-activemq datasource
    @Resource(mappedName = "java:/jms/queue/HsportsQueue")
    private Queue hsportsQueue;

    @Inject
    @JMSConnectionFactory("java:/ConnectionFactory")
    private JMSContext context;

    /**
     * Creating a producer
     *
     * @param message is a message we produce
     */
    public void send(String message) {
        try {
            TextMessage textMessage = context.createTextMessage(message);
            context.createProducer().send(this.hsportsQueue, textMessage);
            logger.info("Message sent to the queue...");
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }
}

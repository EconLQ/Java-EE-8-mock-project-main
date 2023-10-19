package com.linkedin.jax.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import java.io.StringReader;
import java.util.logging.Logger;

/**
 * Message-Driven Bean implementation class for: JmsConsumer
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "/jms/queue/HsportsQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")}, mappedName = "/jms/queue/HsportsQueue")
public class JmsConsumer implements MessageListener {
    private Logger logger = Logger.getLogger(JmsConsumer.class.getName());

    public JmsConsumer() {
    }

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        logger.info("From JMS Consumer MDB:");

        try {
            String json = message.getBody(String.class);        // get serialized object

            parseJsonObject(json);  // parsing JSON object and log data

            logger.info(message.getBody(String.class));

        } catch (JMSException e) {
            logger.warning(e.getMessage());
        }
    }

    private void parseJsonObject(String jsonQuery) {
        try (JsonParser jsonParser = Json.createParser(new StringReader(jsonQuery))) {
            // get each event of the parsed object
            while (jsonParser.hasNext()) {
                Event event = jsonParser.next();

                // parse certain events to log data
                switch (event) {
                    case KEY_NAME:
                    case VALUE_STRING:
                        logger.info(jsonParser.getString());
                        break;
                    case VALUE_NUMBER:
                        logger.info(String.valueOf(jsonParser.getLong()));
                        break;
                    default:
                        break;
                }
            }
        }
    }

}

package com.linkedin.jax.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonPointer;
import javax.json.JsonReader;
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
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonQuery))) {
            JsonObject jsonObject = jsonReader.readObject();
            // creating a pointer to get 1st quantity value of inventoryItems array
            JsonPointer pointer = Json.createPointer("/inventoryItems/0/quantity".toUpperCase());

            long quantity = Long.parseLong(pointer.getValue(jsonObject).toString());

            logger.info("Quantity before promo (2x): " + quantity);

            jsonObject = Json.createPatchBuilder()
                    .add("/promo", "double")
                    .replace("/inventoryItems/0/quantity".toUpperCase(), Long.toString(quantity * 2L))
                    .remove("/storeName".toUpperCase())
                    .build()
                    .apply(jsonObject);

            logger.info("jsonObject: " + jsonObject);

        }
    }
}
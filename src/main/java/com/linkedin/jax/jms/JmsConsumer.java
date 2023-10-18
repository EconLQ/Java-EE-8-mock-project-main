package com.linkedin.jax.jms;

import com.linkedin.jax.InventoryItem;
import com.linkedin.jax.pojos.Order;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyNamingStrategy;
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
            JsonbConfig config = new JsonbConfig()
                    .withPropertyNamingStrategy(PropertyNamingStrategy.CASE_INSENSITIVE);

            Jsonb jsonb = JsonbBuilder.create(config);
            String json = message.getBody(String.class);        // get serialized object
            Order order = jsonb.fromJson(json, Order.class);    // deserialize json to the Order object

            order.getItems().stream()
                    .map(InventoryItem::getName)
                    .forEach(System.out::println);  // log each inventory item's name

            logger.info(message.getBody(String.class));
        } catch (JMSException e) {
            logger.warning(e.getMessage());
        }
    }

}

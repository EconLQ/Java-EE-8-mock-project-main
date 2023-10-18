package com.linkedin.jax.rest;

import com.linkedin.jax.jms.JmsService;
import com.linkedin.jax.pojos.Order;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@RequestScoped
@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderEndpoint {

    Logger logger = Logger.getLogger(OrderEndpoint.class.getName());
    @Inject
    private JmsService jmsService;

    @POST
    public void placeOrder(Order order) {
        JsonbConfig config = new JsonbConfig().withFormatting(true);    // JSON pretty format
        String json;
        try (Jsonb jsonb = JsonbBuilder.create(config)) {
            // serialize an object
            json = jsonb.toJson(order).toUpperCase();
            logger.info(json);
            jmsService.send(json);
        } catch (Exception e) {
            logger.severe("Error ith creating Jsonb object" + e.getMessage());
        }
    }
}

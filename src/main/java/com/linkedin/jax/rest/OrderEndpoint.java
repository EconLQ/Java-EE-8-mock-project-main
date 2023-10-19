package com.linkedin.jax.rest;

import com.linkedin.jax.InventoryItem;
import com.linkedin.jax.jms.JmsService;
import com.linkedin.jax.pojos.Order;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.*;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RequestScoped
@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderEndpoint {

    Logger logger = Logger.getLogger(OrderEndpoint.class.getName());
    @Inject
    private JmsService jmsService;
    private JsonbConfig config = new JsonbConfig().withFormatting(true);    // JSON pretty format

    /**
     * Write Order's data to JSOn with Json Generator. Minus is a tedious process, but it's pretty
     * error-prone if a lot of data needs to written
     *
     * @param order object to serialize
     */
//    @POST
    public void writeObjectWithJsonGenerator(Order order) {
        StringWriter writer = new StringWriter();
        try (JsonGenerator generator = Json.createGenerator(writer)) {

            generator.writeStartObject();
            generator.write("orderId", order.getOrderId());
            generator.write("storeName", order.getStoreName());

            generator.writeStartArray("items");

            for (InventoryItem item : order.getItems()) {
                generator.writeStartObject();
                generator.write("inventoryItemId", item.getInventoryItemId());
                generator.write("catalogItemId", item.getCatalogItemId());
                generator.write("name", item.getName());
                generator.write("quantity", item.getQuantity());
                generator.writeEnd();
            }

            generator.writeEnd();
            generator.writeStartObject("customer");
            generator.write("firstName", order.getCustomer().getFirstName());
            generator.write("lastName", order.getCustomer().getLastName());
            generator.writeEnd();

            generator.writeEnd();

            generator.flush();  // flush the created object
            jmsService.send(writer.toString()); // send the written object to a consumer
        }
    }


    @POST
    public void placeOrder(Order order) {
        String json;
        try (Jsonb jsonb = JsonbBuilder.create(config)) {
            // serialize an object
            json = jsonb.toJson(order).toUpperCase();
            logger.info(json);

            addTrackingDataToJsonObject(json);
            jmsService.send(json);
        } catch (Exception e) {
            logger.severe("Error creating Jsonb object" + e.getMessage());
        }
    }

    /**
     * Adds tracking data to JSON object which includes order date and array with statuses
     *
     * @param jsonQuery accepts a string which represents a json object serialized by {@link Jsonb#toJson(Object)}
     */
    private void addTrackingDataToJsonObject(String jsonQuery) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonQuery))) {
            JsonObject jsonObject = jsonReader.readObject();

            // add new data to JSON object
            JsonObjectBuilder jsonTrackingObjectBuilder = Json.createObjectBuilder()
                    .add("orderDate", LocalDateTime.now().toString());

            // add new statuses
            JsonArrayBuilder jsonStatusArrayBuilder = Json.createArrayBuilder()
                    .add(Json.createObjectBuilder()
                            .add("status", "RECEIVED")
                            .build())
                    .add(Json.createObjectBuilder()
                            .add("status", "SENT_FOR_PROCESSING")
                            .build());

            jsonTrackingObjectBuilder.add("statuses", jsonStatusArrayBuilder.build());

            // add updated jsonTrackingObjectBuilder object to the initial jsonObject
            jsonObject = Json.createObjectBuilder(jsonObject)
                    .add("tracking", jsonTrackingObjectBuilder.build())
                    .build();

            buildJsonWriter(jsonObject);    // writes JSON object to a consumer

            logger.info(jsonObject.toString());
        } catch (JsonException exception) {
            logger.severe("Error reading Json object: " + exception.getMessage());
        }
    }

    /**
     * Builds a JSON writer which consumes JSON object to JMS
     *
     * @param jsonObject to be consumed
     */
    private void buildJsonWriter(JsonObject jsonObject) {
        // pretty print JSON
        Map<String, Boolean> configMap = new HashMap<>();
        configMap.put(JsonGenerator.PRETTY_PRINTING, true); // enable pretty printing

        JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(configMap);
        StringWriter stringWriter = new StringWriter();

        try (JsonWriter writer = jsonWriterFactory.createWriter(stringWriter)) {
            writer.writeObject(jsonObject); // write JSON object to JMS consumer
            logger.info("Write object to JMS Consumer: " + stringWriter);
            jmsService.send(stringWriter.toString());
        } catch (JsonException exception) {
            logger.severe("Error writing Json object: " + exception.getMessage());
        }
    }
}

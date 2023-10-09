package com.linkedin.hsportscatalogjsf;

import com.linkedin.interceptors.Logging;
import com.linkedin.jax.InventoryItem;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Random;
import java.util.logging.Logger;

@ApplicationScoped
@RemoteService
public class RemoteInventoryService implements InventoryService {
    private final String API_URL = "http://localhost:8080/hsports-catalog-jsf-1.0-SNAPSHOT/hsports/api/";
    Logger logger = Logger.getLogger(RemoteInventoryService.class.getName());

    /**
     * Use client from JAX-RS lib to set up a target
     * with base url to the {@link com.linkedin.jax.InventoryItem}.
     * The method build call to REST API with JAX-RS client
     */
    @Override
    @Logging
    public void createItem(Long catalogItemId, String name) {
        if (catalogItemId == null) {
            logger.severe("catalogItemId is null");
            return;
        }

        Client client = ClientBuilder.newClient();

        Response response = client.target(API_URL)
                .path("inventoryitems")
                .request()
                .post(Entity.json(
                        new InventoryItem(null, catalogItemId, name, (long) new Random().nextInt(10)))
                );

        logger.info("catalogItemId is: " + catalogItemId);
        logger.info(String.valueOf(response.getStatus()));
        logger.info(response.getLocation().getPath());
    }

    @Override
    @Logging
    public Long getQuantity(Long catalogItemId) {
        Client client = ClientBuilder.newClient();

        Response response = client.target(API_URL)
                .path("inventoryitems")
                .path("catalog")
                .queryParam("catalogItemId", catalogItemId.toString())
                .request()
                .get();

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            InventoryItem inventoryItem = response.readEntity(InventoryItem.class);
            logger.info(inventoryItem.toString());

            return inventoryItem.getQuantity();
        } else {
            logger.severe("Error: " + response.getStatus());
            logger.severe("Error message: " + response.readEntity(String.class) + " at: " + response.getLocation());
            return null;
        }
    }
}

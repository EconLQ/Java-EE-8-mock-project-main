package com.linkedin.hsportscatalogjsf;

import com.linkedin.interceptors.Logging;
import com.linkedin.jax.InventoryItem;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.Response;
import java.util.Random;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
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

        try (Response response = client.target(API_URL)
                .path("inventoryitems")
                .request()
                .post(Entity.json(
                        new InventoryItem(null, catalogItemId, name, (long) new Random().nextInt(10)))
                )) {

            logger.info("catalogItemId is: " + catalogItemId);
            logger.info(String.valueOf(response.getStatus()));
            logger.info(response.getLocation().getPath());
        } catch (ResponseProcessingException responseProcessingException) {
            logger.warning("Bad request: " + responseProcessingException);
        }
    }

    @Override
    @Logging
    public Long getQuantity(Long catalogItemId) {
        Client client = ClientBuilder.newClient();

        Response response = client.target(API_URL)
                .path("inventoryitems")
                .path("catalog")
                .path("{catalogItemId}")
                .resolveTemplate("catalogItemId", catalogItemId.toString())
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

    /**
     * Retrieve a future of type InventoryItem and make a call to retrieve the response
     *
     * @param catalogItemId is a random value we get from {@link CatalogItemDetailBean#getQuantity()}
     * @return future of InventoryItem
     */
    @Override
    public Future<InventoryItem> asyncGetQuantity(Long catalogItemId) {
        Client client = ClientBuilder.newClient();

        return client.target(API_URL)
                .path("inventoryitems")
                .path("catalog")
                .path("{catalogItemId}")
                .resolveTemplate("catalogItemId", catalogItemId.toString())
                .request()
                .async()
                .get(InventoryItem.class);
    }

    @Override
    public CompletionStage<InventoryItem> reactiveGetQuantity(Long catalogItemId) {
        Client client = ClientBuilder.newClient();

        // only difference from the asnyc method is the switch after request from async to rx (reactive)
        return client.target(API_URL)
                .path("inventoryitems")
                .path("catalog")
                .path("{catalogItemId}")
                .resolveTemplate("catalogItemId", catalogItemId.toString())
                .request()
                .rx()
                .get(InventoryItem.class);
    }
}

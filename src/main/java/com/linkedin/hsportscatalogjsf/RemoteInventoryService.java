package com.linkedin.hsportscatalogjsf;

import com.linkedin.interceptors.Logging;
import com.linkedin.jax.InventoryItem;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Random;

@ApplicationScoped
@RemoteService
public class RemoteInventoryService implements InventoryService {

    private String apiUrl = "http://localhost:8080/hsports-catalog-jsf-1.0-SNAPSHOT/hsports/api/";

    /**
     * Use client from JAX-RS lib to set up a target
     * with base url to the {@link com.linkedin.jax.InventoryItem}.
     * The method build call to REST API with JAX-RS client
     */
    @Override
    @Logging
    public void createItem(Long catalogItemId, String name) {
        Client client = ClientBuilder.newClient();

        Response response = client.target(apiUrl)
                .path("inventoryitems")
                .request()
                .post(Entity.json(
                        new InventoryItem(null, catalogItemId, name, (long) new Random().nextInt(10)))
                );

        System.out.println(response.getStatus());
        System.out.println(response.getLocation().getPath());
    }

    @Override
    public Long getQuantity(Long catalogItemId) {
        return (long) new Random().nextInt(10);
    }
}

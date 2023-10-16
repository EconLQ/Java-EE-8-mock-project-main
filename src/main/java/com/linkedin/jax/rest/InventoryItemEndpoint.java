package com.linkedin.jax.rest;


import com.linkedin.jax.InventoryItem;
import com.linkedin.jax.jms.JmsService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

@RequestScoped
@Path("/inventoryitems")
@Produces("application/json")
@Consumes("application/json")
public class InventoryItemEndpoint {
    Logger logger = Logger.getLogger(InventoryItemEndpoint.class.getName());
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private JmsService jmsService;

    @Transactional
    @POST
    public Response create(final InventoryItem inventoryitem) {
        this.entityManager.persist(inventoryitem);

        // send a message to a queue with the name of the newly created inventory item
        this.jmsService.send(inventoryitem.getName());

        return Response.created(
                        UriBuilder.fromResource(InventoryItemEndpoint.class)
                                .path(String.valueOf(inventoryitem.getInventoryItemId())).build())
                .build();
    }

    @GET
    @Path("/{id:[0-9]+}")
    public Response findById(@PathParam("id") final Long id) {

        InventoryItem inventoryitem = this.entityManager.find(InventoryItem.class, id);
        if (inventoryitem == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        // set random quantity for this item
        inventoryitem.setQuantity(ThreadLocalRandom.current().nextLong(1, 100));
        return Response.ok(inventoryitem).build();
    }

    @GET
    @Path("/catalog/{catalogItemId}")
    public void asyncFindByCatalogId(@NotNull @PathParam("catalogItemId") Long catalogItemId,
                                     @Suspended AsyncResponse asyncResponse) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.warning(e.getMessage());
            }
            // return resources from the response after the 5s delay
            asyncResponse.resume(findByCatalogId(catalogItemId));
        }).start();
    }

    public InventoryItem findByCatalogId(Long catalogItemId) {
        TypedQuery<InventoryItem> query = this.entityManager
                .createQuery("select i from InventoryItem i where i.catalogItemId =:catalogItemId", InventoryItem.class)
                .setParameter("catalogItemId", catalogItemId);

        InventoryItem item = new InventoryItem();
        try {
            item = query.getSingleResult();
        } catch (NoResultException e) {
            // TODO: investigate why the entity hasn't been created
            logger.severe("No entity with such catalogItemId...");
        }
        // set a random value for the quantity
        item.setQuantity(ThreadLocalRandom.current().nextLong(1, 100));
        return item;
    }

    @GET
    public List<InventoryItem> listAll(@QueryParam("start") final Integer startPosition,
                                       @QueryParam("max") final Integer maxResult) {
        TypedQuery<InventoryItem> query = this.entityManager.createQuery(
                "select i from InventoryItem i", InventoryItem.class
        );

        final List<InventoryItem> inventoryItems = query.getResultList();
        if (inventoryItems == null) {
            System.out.println("There is no inventory items in the list...");
            return null;
        }
        return inventoryItems;
    }

    @Transactional
    @PUT
    @Path("/{id:[0-9]+}")
    public Response update(@PathParam("id") Long id, final InventoryItem inventoryitem) {
        this.entityManager.merge(inventoryitem);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id:[0-9]+}")
    public Response deleteById(@PathParam("id") final Long id) {
        this.entityManager.remove(this.entityManager.find(InventoryItem.class, id));
        return Response.noContent().build();
    }
}
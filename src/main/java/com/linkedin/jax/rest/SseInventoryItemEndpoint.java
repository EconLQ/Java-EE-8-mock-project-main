package com.linkedin.jax.rest;

import com.linkedin.hsportscatalogejb.CatalogItem;
import com.linkedin.hsportscatalogejb.CatalogLocal;
import com.linkedin.hsportscatalogjsf.InventoryService;
import com.linkedin.hsportscatalogjsf.RemoteService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;
import java.util.List;
import java.util.logging.Logger;

/**
 * <a href="https://www.baeldung.com/java-ee-jax-rs-sse">Server-Sent Events (SSE)</a> is an HTTP based specification that provides a way
 * to establish a long-running and mono-channel connection from the server to the client.
 */
@Singleton
@Path("/sse/inventoryitems")
@Produces("application/json")
@Consumes("application/json")
public class SseInventoryItemEndpoint {
    Logger logger = Logger.getLogger(SseInventoryItemEndpoint.class.getName());
    @Inject
    private CatalogLocal catalogBean;

    @Inject
    @RemoteService
    private InventoryService inventoryService;

    @GET
    @Path("/{inventoryItemId}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void sseItemQuantity(@PathParam("inventoryItemId") Long inventoryItemId,
                                @Context SseEventSink sseEventSink,
                                @Context Sse sse) {
        List<CatalogItem> catalogItems = catalogBean.getItems();
        try (SseEventSink sink = sseEventSink) {
            for (CatalogItem item : catalogItems) {
                Long quantity = inventoryService.getQuantity(item.getItemId());

                // used on the Server API and designs a send event
                // create event to get catalogItem
                OutboundSseEvent catalogItemEvent;

                // createEvent to get quantity of that item
                OutboundSseEvent inventoryServiceEvent;
                if (quantity != null) {
                    catalogItemEvent = sse.newEventBuilder()
                            .id("catalogItemEventId")
                            .name("catalogItem").data(item.toString())
                            .comment("info about catalog item")
                            .build();
                    inventoryServiceEvent = sse.newEvent("inventoryService", String.valueOf(quantity));
                } else {
                    catalogItemEvent = sse.newEvent("catalogItem", "no such catalog item");
                    inventoryServiceEvent = sse.newEvent("inventoryService", "no quantity of that item");
                    logger.warning("There is no quantity for that item...");
                }

                logger.info("Item: " + catalogItemEvent.getData().toString()
                        + " . Quantity: " + inventoryServiceEvent.getData().toString());
                sink.send(catalogItemEvent);
                sink.send(inventoryServiceEvent);
            }
        }
    }
}

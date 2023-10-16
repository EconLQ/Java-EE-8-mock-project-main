package com.linkedin.hsportscatalogjsf;

import com.linkedin.hsportscatalogejb.CatalogItem;
import com.linkedin.hsportscatalogejb.CatalogLocal;
import com.linkedin.hsportscatalogejb.ItemManager;
import com.linkedin.jax.InventoryItem;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Named
@ConversationScoped
public class CatalogItemDetailBean implements Serializable {
    Logger logger = Logger.getLogger(CatalogItemDetailBean.class.getName());
    private long itemId;
    private CatalogItem item;
    private Long quantity;
    @Inject
    private Conversation conversation;
    @Inject
    private CatalogLocal catalogBean;
    @Inject
    @RemoteService
    private InventoryService inventoryService;
    private ItemManager manager = new ItemManager();

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public ItemManager getManager() {
        return manager;
    }

    public void setManager(ItemManager manager) {
        this.manager = manager;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public CatalogItem getItem() {
        return item;
    }

    public void setItem(CatalogItem item) {
        this.item = item;
    }

    public void fetchItem() throws InterruptedException, ExecutionException {
        this.item = this.catalogBean.findItem(this.itemId);

        // keep the number of call we expect to make and number of calls we receive back
        CountDownLatch latch = new CountDownLatch(1);

        this.inventoryService.reactiveGetQuantity(this.itemId)
                .thenApply(InventoryItem::getQuantity)
                .thenAccept(quantity -> {
                    this.setQuantity(quantity);
                    logger.info("The quantity of " + this.item.getName() + " is: " + quantity.toString());
                    latch.countDown();
                });

        logger.info("Request is complete...");
        latch.await();  // waits for the countdown to be done
    }

    public void addManager() {
        this.manager.getCatalogItems().add(this.item);
        this.item.getItemManagers().add(this.manager);

        this.catalogBean.saveItem(this.item);
        this.item = this.catalogBean.findItem(itemId);
    }

    @PostConstruct
    public void init() {
        this.conversation.begin();
    }

    @PreDestroy
    public void destroy() {
        this.conversation.end();
    }

}

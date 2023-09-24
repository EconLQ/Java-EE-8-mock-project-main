package com.linkedin.hsportscatalogjsf;

import javax.enterprise.inject.Produces;
import java.time.LocalDateTime;

public class InventoryServiceFactory {
    @Produces
    public InventoryService createInstance() {
        InventoryService inventoryService = null;
        if (LocalDateTime.now().getHour() > 12) {
            inventoryService = new LocalInventoryService();
        } else {
            inventoryService = new RemoteInventoryService();
        }
        return inventoryService;
    }
}

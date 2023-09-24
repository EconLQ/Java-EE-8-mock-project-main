package com.linkedin.hsportscatalogjsf;

import java.time.LocalDateTime;

public class InventoryServiceFactory {
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

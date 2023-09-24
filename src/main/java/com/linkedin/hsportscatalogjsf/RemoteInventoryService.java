package com.linkedin.hsportscatalogjsf;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
@Alternative
public class RemoteInventoryService implements InventoryService {
    private Map<Long, InventoryItem> items = new HashMap<>();

    @Override
    public void createItem(Long catalogItemId, String name) {
        long inventoryItemID = items.size() + 1;
        this.items.put(inventoryItemID, new InventoryItem(inventoryItemID, catalogItemId, name, 0L));
        this.printInventory();
    }

    private void printInventory() {
        System.out.println("Remote inventory contains: ");
        this.items.forEach((key, value) -> System.out.println(value.getName()));
    }

    @Override
    public Long getQuantity(Long catalogItemId) {
        return items.isEmpty() ? 0 : (long) items.size() + 1;
    }
}

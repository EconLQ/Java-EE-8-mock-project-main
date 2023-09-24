package com.linkedin.hsportscatalogjsf;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
@Alternative
public class LocalInventoryService implements InventoryService {
    private Map<Long, InventoryItem> items = new HashMap<>();

    @Override
    public void createItem(Long catalogItemId, String name) {
        long inventoryItemId = items.size() + 1;
        this.items.put(inventoryItemId, new InventoryItem(inventoryItemId, catalogItemId, name, 0L));
        this.printInventory();
    }

    private void printInventory() {
        System.out.println("Local inventory contains: ");
        this.items.forEach((key, value) -> System.out.println(value.getName()));
    }

    @Override
    public Long getQuantity(Long catalogItemId) {
        return this.items.isEmpty() ? 0 : (long) this.items.size();
    }
}

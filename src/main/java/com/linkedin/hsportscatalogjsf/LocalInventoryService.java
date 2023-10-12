package com.linkedin.hsportscatalogjsf;

import com.linkedin.interceptors.Logging;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

@ApplicationScoped
public class LocalInventoryService implements InventoryService {
    private Map<Long, InventoryItem> items = new HashMap<>();

    @Override
    @Logging    // cause interceptor logic to be executed everytime when we invoke a  method
    public void createItem(Long catalogItemId, String name) {
        long inventoryItemId = items.size() + 1;
        this.items.put(inventoryItemId, new InventoryItem(inventoryItemId, catalogItemId, name, 0L));
        this.printInventory();
    }

    private void printInventory() {
        System.out.print("Local inventory contains: ");
        this.items.forEach((key, value) -> System.out.print(value.getName() + ", "));
    }

    @Override
    public Long getQuantity(Long catalogItemId) {
        return this.items.isEmpty() ? 0 : (long) this.items.size();
    }

    @Override
    public Future<com.linkedin.jax.InventoryItem> asyncGetQuantity(Long catalogItemId) {
        return null;
    }

    @Override
    public CompletionStage<com.linkedin.jax.InventoryItem> reactiveGetQuantity(Long catalogItemId) {
        return null;
    }
}

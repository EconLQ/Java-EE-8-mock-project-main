package com.linkedin.hsportscatalogjsf;

import com.linkedin.jax.InventoryItem;

import java.io.Serializable;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

public interface InventoryService extends Serializable {
    void createItem(Long catalogItemId, String name);

    Long getQuantity(Long catalogItemId);

    Future<InventoryItem> asyncGetQuantity(Long catalogItemId);

    /**
     * Allows us to chain different calls asynchronously
     */
    CompletionStage<InventoryItem> reactiveGetQuantity(Long catalogItemId);
}

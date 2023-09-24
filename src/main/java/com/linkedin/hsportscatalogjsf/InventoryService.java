package com.linkedin.hsportscatalogjsf;

import java.io.Serializable;

public interface InventoryService extends Serializable {
    void createItem(Long catalogItemId, String name);

    Long getQuantity(Long catalogItemId);
}

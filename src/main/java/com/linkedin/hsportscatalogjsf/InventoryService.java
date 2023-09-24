package com.linkedin.hsportscatalogjsf;

import java.io.Serializable;

public interface InventoryService extends Serializable {
    public void createItem(Long catalogItemId, String name);

    public Long getQuantity(Long catalogItemId);
}

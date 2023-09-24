package com.linkedin.hsportscatalogjsf;

import java.util.Objects;

public class InventoryItem {
    private Long inventoryItemId;
    private Long catalogItemId;
    private String name;
    private Long quantity;

    public InventoryItem(Long inventoryItemId, Long catalogItemId, String name, Long quantity) {
        this.inventoryItemId = inventoryItemId;
        this.catalogItemId = catalogItemId;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(Long inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public Long getCatalogItemId() {
        return catalogItemId;
    }

    public void setCatalogItemId(Long catalogItemId) {
        this.catalogItemId = catalogItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "inventoryItemId=" + inventoryItemId +
                ", catalogItemId=" + catalogItemId +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryItem)) return false;
        InventoryItem that = (InventoryItem) o;
        return Objects.equals(getInventoryItemId(), that.getInventoryItemId()) && Objects.equals(getCatalogItemId(), that.getCatalogItemId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getQuantity(), that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInventoryItemId(), getCatalogItemId(), getName(), getQuantity());
    }
}

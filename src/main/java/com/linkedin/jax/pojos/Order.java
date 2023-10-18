package com.linkedin.jax.pojos;

import com.linkedin.jax.InventoryItem;

import javax.json.bind.annotation.JsonbProperty;
import java.util.List;

public class Order {
    private long orderId;
    private String storeName;
    private Customer customer;
    @JsonbProperty("inventoryItems")
    private List<InventoryItem> items;

    public Order() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    /**
     * Placing Jsonb annotation on a setter is going to affect only the <b>deserialization</b>
     *
     * @param items are inventoryItems param of a serialized object
     */
    @JsonbProperty("inventoryItems")
    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }
}

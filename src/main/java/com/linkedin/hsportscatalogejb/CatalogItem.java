package com.linkedin.hsportscatalogejb;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "CATALOG_ITEM")
public class CatalogItem {

    @Id
    @Column(name = "CATALOG_ITEM_ID")
    @GeneratedValue(strategy = GenerationType.AUTO) // selects the appropriate type automatically
    private Long itemId;

    @Column(name = "NAME")
    private String name;
    @Column(name = "MANUFACTURER")
    private String manufacturer;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "AVAILABLE_DATE")
    private LocalDate availableDate;

    public CatalogItem() {
    }

    public CatalogItem(String name, String manufacturer, String description, LocalDate availableDate) {
        super();
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.availableDate = availableDate;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    @Override
    public String toString() {
        return "CatalogItem [itemId=" + itemId + ", name=" + name + ", manufacturer=" + manufacturer + ", description="
                + description + ", availableDate=" + availableDate + "]";
    }

}

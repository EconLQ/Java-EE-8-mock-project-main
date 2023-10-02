package com.linkedin.hsportscatalogejb;

import com.linkedin.hsportscatalogejb.constraints.PermittedManufacturer;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATALOG_ITEM")
public class CatalogItem {

    @Id
    @Column(name = "CATALOG_ITEM_ID")
    @GeneratedValue(strategy = GenerationType.AUTO) // selects the appropriate type automatically
    private Long itemId;
    /*
     * @Size - is a bean validation to ensure that each item can have no or 3 managers at max.
     * CascadeType.ALL - any operation on this entity is going to persist to any associated entities
     * FetchType.EAGER - when we retrieve this entity from the database we want to load all associated
     * entities with this entity
     * */
    @Size(max = 3)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "CATALOG_ITEM_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEM_MANAGER_ID"))
    private List<ItemManager> itemManagers = new ArrayList<>();
    @NotBlank   // checks if string is equal to null or length doesn't equal zero
    @Column(name = "NAME")
    private String name;
    @PermittedManufacturer  // custom constraint validator to check the manufacturer
    @Column(name = "MANUFACTURER")
    private String manufacturer;

    @Alphabetic // custom bean validator for strings
    @Column(name = "DESCRIPTION")
    private String description;
    @Future(message = "Must be a future date")
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

    public List<ItemManager> getItemManagers() {
        return itemManagers;
    }

    public void setItemManagers(List<ItemManager> itemManagers) {
        this.itemManagers = itemManagers;
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

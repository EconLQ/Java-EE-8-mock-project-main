package com.linkedin.hsportscatalogejb;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ITEM_MANAGER")
public class ItemManager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ITEM_MANAGER_ID")
    private Long itemManagerId;

    // mapping to the itemManagers variable in CatalogItem, which done joining columns
    @ManyToMany(mappedBy = "itemManagers")
    private List<CatalogItem> catalogItems = new ArrayList<>();
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;


    public Long getItemManagerId() {
        return itemManagerId;
    }

    public void setItemManagerId(Long itemManagerId) {
        this.itemManagerId = itemManagerId;
    }

    public List<CatalogItem> getCatalogItems() {
        return catalogItems;
    }

    public void setCatalogItems(List<CatalogItem> catalogItems) {
        this.catalogItems = catalogItems;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "ItemManager{" +
                "itemManagerId=" + itemManagerId +
                ", catalogItems=" + catalogItems +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

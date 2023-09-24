package com.linkedin.hsportscatalogjsf;

import com.linkedin.hsportscatalogejb.CatalogItem;
import com.linkedin.hsportscatalogejb.CatalogLocal;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named
public class CatalogItemFormBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @Inject
    private CatalogLocal catalogBean;
    @Inject
    private InventoryService inventoryService;
    @Inject
    private InventoryService remoteInventoryService;

    private CatalogItem item = new CatalogItem();

    private List<CatalogItem> items = new ArrayList<>();

    public String addItem() {
        long itemId = catalogBean.getItems().size() + 1;

        catalogBean.addItem(new CatalogItem(itemId, this.item.getName(), this.item.getManufacturer(),
                this.item.getDescription(), this.item.getAvailableDate()));

//        catalogBean.getItems().forEach(item -> System.out.println(item.toString()));
        this.inventoryService.createItem(this.item.getItemId(), this.item.getName());
        return "list?faces-redirect=true";
    }

    public void init() {
        this.items = this.catalogBean.getItems();
    }

    public CatalogItem getItem() {
        return item;
    }

    public void setItem(CatalogItem item) {
        this.item = item;
    }

    public List<CatalogItem> getItems() {
        return items;
    }

    public void setItems(List<CatalogItem> items) {
        this.items = items;
    }

}

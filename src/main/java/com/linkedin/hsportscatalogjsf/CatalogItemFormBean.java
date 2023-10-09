package com.linkedin.hsportscatalogjsf;

import com.linkedin.hsportscatalogejb.CatalogItem;
import com.linkedin.hsportscatalogejb.CatalogLocal;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@SessionScoped
@Named
public class CatalogItemFormBean implements Serializable {
    private static final long serialVersionUID = 1L;
    Logger logger = Logger.getLogger(CatalogItemFormBean.class.getName());
    @Inject
    private CatalogLocal catalogBean;
    @Inject
    @RemoteService
    private InventoryService inventoryService;

    private CatalogItem item = new CatalogItem();

    private List<CatalogItem> items = new ArrayList<>();
    private String searchText;

    public void init() {
        this.items = this.catalogBean.getItems();
    }

    public String addItem() {
        this.catalogBean.addItem(this.item);
        logger.info(this.item.toString());  // log the created object

        this.inventoryService.createItem(this.item.getItemId(), this.item.getName());
        return "list?faces-redirect=true";
    }

    public void searchByName() {
        this.items = this.catalogBean.searchByName(searchText);
    }

    public CatalogItem getItem() {
        return item;
    }

    public void setItem(CatalogItem item) {
        this.item = item;
    }

    public List<CatalogItem> getItems() {
        return this.catalogBean.getItems();
    }

    public void setItems(List<CatalogItem> items) {
        this.items = items;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public CatalogLocal getCatalogBean() {
        return catalogBean;
    }

    public void setCatalogBean(CatalogLocal catalogBean) {
        this.catalogBean = catalogBean;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

}

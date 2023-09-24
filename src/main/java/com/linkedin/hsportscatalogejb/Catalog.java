package com.linkedin.hsportscatalogejb;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton bean implementation class Catalog
 */
@Singleton
@LocalBean
public class Catalog implements CatalogLocal {
    private List<CatalogItem> items = new ArrayList<>();

    public Catalog() {
    }

    @Override
    public List<CatalogItem> getItems() {
        return items;
    }

    @Override
    public void addItem(CatalogItem item) {
        items.add(item);
    }
}

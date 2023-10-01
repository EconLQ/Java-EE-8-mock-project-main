package com.linkedin.hsportscatalogejb;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton bean implementation class Catalog
 */
@Singleton
@LocalBean
public class Catalog implements CatalogLocal {
    @PersistenceContext
    private EntityManager entityManager;

    private List<CatalogItem> items = new ArrayList<>();

    public Catalog() {
    }

    @Override
    public List<CatalogItem> getItems() {
        return this.entityManager.createQuery("select c from CatalogItem c", CatalogItem.class).getResultList();
    }

    @Override
    public void addItem(CatalogItem item) {
        this.entityManager.persist(item);
    }

    @Override
    public CatalogItem findItem(Long itemId) {
        return this.entityManager.find(CatalogItem.class, itemId);
    }

    @Override
    public void deleteItem(CatalogItem catalogItem) {
        this.entityManager.remove(
                this.entityManager.contains(catalogItem)
                        ? catalogItem
                        : this.entityManager.merge(catalogItem));
    }

    @Override
    public List<CatalogItem> searchByName(String name) {
        return this.entityManager.createQuery("select c from CatalogItem c " +
                        "where c.name=:name", CatalogItem.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    @Override
    public void saveItem(CatalogItem item) {
        this.entityManager.merge(item);
    }
}

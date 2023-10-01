package com.linkedin.hsportscatalogjsf;

import com.linkedin.hsportscatalogejb.CatalogItem;
import com.linkedin.hsportscatalogejb.CatalogLocal;
import com.linkedin.hsportscatalogejb.ItemManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ConversationScoped
public class CatalogItemDetailBean implements Serializable {
    private long itemId;
    private CatalogItem item;
    @Inject
    private Conversation conversation;
    @Inject
    private CatalogLocal catalogBean;
    private ItemManager manager = new ItemManager();

    public ItemManager getManager() {
        return manager;
    }

    public void setManager(ItemManager manager) {
        this.manager = manager;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public CatalogItem getItem() {
        return item;
    }

    public void setItem(CatalogItem item) {
        this.item = item;
    }

    public void fetchItem() {
        this.item = this.catalogBean.findItem(this.itemId);
    }

    public void addManager() {
        this.manager.getCatalogItems().add(this.item);
        this.item.getItemManagers().add(this.manager);

        this.catalogBean.saveItem(this.item);
        this.item = this.catalogBean.findItem(itemId);
    }

    @PostConstruct
    public void init() {
        this.conversation.begin();
    }

    @PreDestroy
    public void destroy() {
        this.conversation.end();
    }

}

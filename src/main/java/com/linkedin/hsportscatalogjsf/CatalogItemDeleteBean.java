package com.linkedin.hsportscatalogjsf;

import com.linkedin.hsportscatalogejb.CatalogItem;
import com.linkedin.hsportscatalogejb.CatalogLocal;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ConversationScoped
public class CatalogItemDeleteBean implements Serializable {

	private long itemId;

	private CatalogItem item;

	@Inject
	private CatalogItemFormBean catalogItemFormBean;
	@Inject
	private CatalogLocal catalogBean;
	@Inject
	private Conversation conversation;    // to work with @ConversationScoped (specifically for JSF)

	public void fetchItem() {
		conversation.begin();
		this.item = catalogBean.findItem(this.itemId);
	}

	public String removeItem() {
		this.catalogBean.deleteItem(item);
		conversation.end();    // to end the conversation lifecycle scope for this bean
		return "list?faces-redirect=true";
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

	public CatalogItemFormBean getCatalogItemFormBean() {
		return catalogItemFormBean;
	}

	public void setCatalogItemFormBean(CatalogItemFormBean catalogItemFormBean) {
		this.catalogItemFormBean = catalogItemFormBean;
	}


}


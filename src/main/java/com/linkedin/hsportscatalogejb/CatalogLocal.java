package com.linkedin.hsportscatalogejb;

import javax.ejb.Local;
import java.io.Serializable;
import java.util.List;

@Local
public interface CatalogLocal extends Serializable {

    public List<CatalogItem> getItems();

    public void addItem(CatalogItem item);
}

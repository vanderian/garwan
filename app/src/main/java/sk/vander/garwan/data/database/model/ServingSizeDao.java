package sk.vander.garwan.data.database.model;

import com.orm.SugarRecord;

import java.math.BigDecimal;

import sk.vander.garwan.data.api.model.ServingSize;

/**
 * Created by arashid on 21/06/16.
 */
public class ServingSizeDao extends SugarRecord {
  String size;
  BigDecimal price;

  public ServingSizeDao() {}

  public ServingSizeDao(ServingSize servingSize) {
    size = servingSize.size();
    price = servingSize.price();
  }

  public String getSize() {
    return size;
  }

  public BigDecimal getPrice() {
    return price;
  }
}

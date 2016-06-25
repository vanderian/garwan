package sk.vander.meals.data.api.model;

import java.math.BigDecimal;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 24/06/16.
 */
@AutoParcelGson
public abstract class ServingSize {
  public abstract BigDecimal price();
  public abstract String size();
}

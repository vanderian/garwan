package sk.vander.garwan.data.api.model;

import java.math.BigDecimal;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 24/06/16.
 */
@AutoParcelGson
public abstract class Packaging {
  public abstract String descrition();
  public abstract BigDecimal price();
}

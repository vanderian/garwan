package sk.vander.meals.data.api.model;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 24/06/16.
 */
@AutoParcelGson
public abstract class AddOn {
  public abstract String id();
  public abstract AddOnCategory category();
  public abstract String name();
  public abstract ServingSize servingSize();
  public abstract String description();
  public abstract int displaySeq();
}

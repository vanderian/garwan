package sk.vander.garwan.data.api.model;

import java.util.List;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 24/06/16.
 */
@AutoParcelGson
public abstract class Meal {
  public abstract String id();
  public abstract MealCategory category();
  public abstract String name();
  public abstract ServingSize servingSize();
  public abstract String description();
  public abstract List<String> addOnIds();
  public abstract int displaySeq();
}

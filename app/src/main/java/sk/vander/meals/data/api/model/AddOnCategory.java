package sk.vander.meals.data.api.model;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 24/06/16.
 */
@AutoParcelGson
public abstract class AddOnCategory {
  public abstract String id();
  public abstract String name();
  public abstract String description();
  public abstract SelectionOption selectionOption();
  public abstract int displaySeq();
}

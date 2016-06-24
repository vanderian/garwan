package sk.vander.garwan.data.api.model;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 24/06/16.
 */
@AutoParcelGson
public abstract class SelectionOption {
  public abstract int min();
  public abstract int max();
}

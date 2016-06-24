package sk.vander.garwan.data.api.model;

import java.util.List;

/**
 * Created by arashid on 24/06/16.
 */
public abstract class ListResponse<T> {
  public abstract int version();
  public abstract List<T> list();
}

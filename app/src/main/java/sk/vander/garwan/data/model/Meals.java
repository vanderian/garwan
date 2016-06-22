package sk.vander.garwan.data.model;

import java.util.List;

/**
 * Created by arashid on 22/06/16.
 */
public class Meals {
  int version;
  List<Meal> meals;

  public List<Meal> getList() {
    return meals;
  }
}

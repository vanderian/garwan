package sk.vander.garwan.data.model;

import java.util.List;

/**
 * Created by arashid on 22/06/16.
 */
public class MealCategories {
  int version;
  List<MealCategory> mealCategories;

  public List<MealCategory> getList() {
    return mealCategories;
  }
}

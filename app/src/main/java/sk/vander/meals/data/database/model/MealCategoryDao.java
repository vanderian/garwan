package sk.vander.meals.data.database.model;

import com.orm.SugarRecord;
import com.orm.dsl.Column;

import sk.vander.meals.data.api.model.MealCategory;

/**
 * Created by arashid on 21/06/16.
 */
public class MealCategoryDao extends SugarRecord {
  @Column(name = "uid", unique = true) String uid;
  String name;

  public MealCategoryDao() {
  }

  public MealCategoryDao(MealCategory mealCategory) {
    uid = mealCategory.id();
    name = mealCategory.name();
  }

  public String getName() {
    return name;
  }

  public String getUid() {
    return uid;
  }
}

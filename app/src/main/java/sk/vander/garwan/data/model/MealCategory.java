package sk.vander.garwan.data.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

/**
 * Created by arashid on 21/06/16.
 */
public class MealCategory extends SugarRecord {
  String id;
  String name;
  @Ignore List<Meal> meals;

  public MealCategory() {}
}

package sk.vander.garwan.data.model;

import com.google.gson.annotations.SerializedName;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by arashid on 21/06/16.
 */
@Table
public class MealCategory {
  transient Long id;
  @SerializedName("id") @Column(name = "str_id", unique = true) String strId;
  String name;
  @Ignore transient List<Meal> meals;

  public MealCategory() {}

  public void setMeals(List<Meal> meals) {
    this.meals = meals;
  }

  public String getStrId() {
    return strId;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }
}

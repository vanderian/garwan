package sk.vander.garwan.data.database.model;

import android.text.TextUtils;

import com.orm.SugarRecord;
import com.orm.dsl.Column;

import sk.vander.garwan.data.api.model.Meal;

/**
 * Created by arashid on 21/06/16.
 */
public class MealDao extends SugarRecord {
  @Column(name = "uid", unique = true) String uid;
  String name;
  String addOnIds;
  ServingSizeDao servingSizeDao;
  String catUid;

  public MealDao() {}

  public MealDao(Meal meal) {
    this.uid = meal.id();
    this.name = meal.name();
    this.addOnIds = TextUtils.join(",", meal.addOnIds());
    this.servingSizeDao = new ServingSizeDao(meal.servingSize());
    this.catUid = meal.category().id();
  }

  public String getName() {
    return name;
  }

  public String getAddOnIds() {
    return addOnIds;
  }

  public ServingSizeDao getServingSizeDao() {
    return servingSizeDao;
  }

  public String getCatUid() {
    return catUid;
  }
}

package sk.vander.meals.data.database.model;

import com.orm.SugarRecord;
import com.orm.dsl.Column;

import sk.vander.meals.data.api.model.AddOnCategory;

/**
 * Created by arashid on 21/06/16.
 */
public class AddOnCategoryDao extends SugarRecord {
  @Column(name = "uid", unique = true) String uid;
  String name;

  public AddOnCategoryDao() {
  }

  public AddOnCategoryDao(AddOnCategory addOnCategory) {
    name = addOnCategory.name();
    uid = addOnCategory.id();
  }

  public String getName() {
    return name;
  }

  public String getUid() {
    return uid;
  }
}

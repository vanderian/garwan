package sk.vander.garwan.data.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by arashid on 21/06/16.
 */
@Table
public class Meal {
  transient Long id;
  @SerializedName("id") @Column(name = "str_id", unique = true) String strId;
  String name;
  String description;
  String addOnIdsJoined;
  ServingSize servingSize;
  String catId;
  @Ignore MealCategory category;
  @Ignore List<String> addOnIds;

  public Meal() {}

  public Long getId() {
    return id;
  }

  public String getCatId() {
    return catId;
  }

  public void initSave() {
    servingSize.save();
    addOnIdsJoined = TextUtils.join(",", addOnIds);
    catId = category.getStrId();
  }

  public String getName() {
    return name;
  }

  public ServingSize getServingSize() {
    return servingSize;
  }
}

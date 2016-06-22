package sk.vander.garwan.data.model;

import com.google.gson.annotations.SerializedName;
import com.orm.dsl.Column;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

/**
 * Created by arashid on 21/06/16.
 */
@Table
public class AddOn {
  transient Long id;
  @SerializedName("id") @Column(name = "str_id", unique = true) String strId;
  String name;
  ServingSize servingSize;
  String catId;
  @Ignore AddOnCategory category;

  public AddOn() {}

  public Long getId() {
    return id;
  }

  public void initSave() {
    servingSize.save();
    catId = category.getStrId();
  }

  public String getStrId() {
    return strId;
  }
}

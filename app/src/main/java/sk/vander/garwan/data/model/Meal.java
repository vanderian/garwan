package sk.vander.garwan.data.model;

import com.google.common.base.Strings;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

/**
 * Created by arashid on 21/06/16.
 */
public class Meal extends SugarRecord {
  String id;
  String name;
  ServingSize servingSize;
  String description;
  String addOnIdsJoined;
  @Ignore List<String> addOnIds;

  public Meal() {}
}

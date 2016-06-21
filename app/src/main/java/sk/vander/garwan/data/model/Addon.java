package sk.vander.garwan.data.model;

import com.orm.SugarRecord;

/**
 * Created by arashid on 21/06/16.
 */
public class Addon extends SugarRecord {
  String id;
  String name;
  AddonCategory category;
  ServingSize servingSize;

  public Addon() {}
}

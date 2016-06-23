package sk.vander.garwan.ui.model;

import auto.parcelgson.AutoParcelGson;
import sk.vander.garwan.data.model.AddOn;
import sk.vander.garwan.data.model.Meal;

/**
 * Created by arashid on 23/06/16.
 */
@AutoParcelGson
public abstract class ListDetailItem {
  public abstract String name();
  public abstract String price();
  public abstract String size();
  public abstract long getId();

  public static ListDetailItem create(Meal meal) {
    return new AutoParcelGson_ListDetailItem(meal.getName(), meal.getServingSize().getPrice(), meal.getServingSize().getSize(), meal.getId());
  }

  public static ListDetailItem create(AddOn addOn) {
    return new AutoParcelGson_ListDetailItem(addOn.getName(), addOn.getServingSize().getPrice(), addOn.getServingSize().getSize(), addOn.getId());
  }
}

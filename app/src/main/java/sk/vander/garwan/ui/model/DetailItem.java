package sk.vander.garwan.ui.model;

import auto.parcelgson.AutoParcelGson;
import sk.vander.garwan.data.model.AddOn;
import sk.vander.garwan.data.model.Meal;

/**
 * Created by arashid on 23/06/16.
 */
@AutoParcelGson
public abstract class DetailItem {
  public abstract String name();
  public abstract String price();
  public abstract String size();
  public abstract long getId();

  public static DetailItem create(Meal meal) {
    return new AutoParcelGson_DetailItem(meal.getName(), meal.getServingSize().getPrice(), meal.getServingSize().getSize(), meal.getId());
  }

  public static DetailItem create(AddOn addOn) {
    return new AutoParcelGson_DetailItem(addOn.getName(), addOn.getServingSize().getPrice(), addOn.getServingSize().getSize(), addOn.getId());
  }
}

package sk.vander.garwan.ui.model;

import java.util.List;

import auto.parcelgson.AutoParcelGson;
import rx.Observable;
import sk.vander.garwan.data.model.AddOn;
import sk.vander.garwan.data.model.AddOnCategory;
import sk.vander.garwan.data.model.Meal;
import sk.vander.garwan.data.model.MealCategory;
import sk.vander.garwan.ui.adapter.ExpandGroupItem;

/**
 * Created by arashid on 23/06/16.
 */
@AutoParcelGson
public abstract class GroupItem extends ExpandGroupItem {
  public abstract String name();
  public abstract List<DetailItem> items();

 public static GroupItem create(long id, MealCategory mc, List<Meal> meals) {
   return new AutoParcelGson_GroupItem(id, mc.getName(),
       Observable.from(meals).map(DetailItem::create).toList().toBlocking().first());
 }

 public static GroupItem create(long id, AddOnCategory ac, List<AddOn> addOns) {
   return new AutoParcelGson_GroupItem(id, ac.getName(),
       Observable.from(addOns).map(DetailItem::create).toList().toBlocking().first());
 }
}
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
public abstract class ListGroupItem extends ExpandGroupItem {
  public abstract String name();
  public abstract List<ListDetailItem> items();

 public static ListGroupItem create(MealCategory mc, List<Meal> meals) {
   return new AutoParcelGson_ListGroupItem(mc.getId(), mc.getName(),
       Observable.from(meals).map(ListDetailItem::create).toList().toBlocking().first());
 }

 public static ListGroupItem create(AddOnCategory ac, List<AddOn> addOns) {
   return new AutoParcelGson_ListGroupItem(ac.getId(), ac.getName(),
       Observable.from(addOns).map(ListDetailItem::create).toList().toBlocking().first());
 }
}
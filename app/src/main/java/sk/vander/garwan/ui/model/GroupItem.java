package sk.vander.garwan.ui.model;

import java.util.List;

import auto.parcelgson.AutoParcelGson;
import rx.Observable;
import sk.vander.garwan.data.database.model.AddOnDao;
import sk.vander.garwan.data.database.model.AddOnCategoryDao;
import sk.vander.garwan.data.database.model.MealDao;
import sk.vander.garwan.data.database.model.MealCategoryDao;
import sk.vander.garwan.ui.adapter.ExpandGroupItem;

/**
 * Created by arashid on 23/06/16.
 */
@AutoParcelGson
public abstract class GroupItem extends ExpandGroupItem {
  public abstract String name();
  public abstract List<DetailItem> items();

  public static GroupItem create(MealCategoryDao mc, List<MealDao> mealDaos) {
    return new AutoParcelGson_GroupItem(mc.getId(), mc.getName(),
        Observable.from(mealDaos).map(DetailItem::create).toList().toBlocking().first());
  }

  public static GroupItem create(AddOnCategoryDao ac, List<AddOnDao> addOnDaos) {
    return new AutoParcelGson_GroupItem(ac.getId(), ac.getName(),
        Observable.from(addOnDaos).map(DetailItem::create).toList().toBlocking().first());
  }
}
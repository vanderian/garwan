package sk.vander.garwan.ui.model;

import java.math.BigDecimal;

import auto.parcelgson.AutoParcelGson;
import sk.vander.garwan.data.database.model.AddOnDao;
import sk.vander.garwan.data.database.model.MealDao;

/**
 * Created by arashid on 23/06/16.
 */
@AutoParcelGson
public abstract class DetailItem {
  public abstract String name();
  public abstract BigDecimal price();
  public abstract String size();
  public abstract long getId();
  public abstract String addOnIds();

  public static DetailItem create(MealDao mealDao) {
    return new AutoParcelGson_DetailItem(
        mealDao.getName(),
        mealDao.getServingSizeDao().getPrice(),
        mealDao.getServingSizeDao().getSize(),
        mealDao.getId(),
        mealDao.getAddOnIds());
  }

  public static DetailItem create(AddOnDao addOnDao) {
    return new AutoParcelGson_DetailItem(
        addOnDao.getName(),
        addOnDao.getServingSizeDao().getPrice(),
        addOnDao.getServingSizeDao().getSize(),
        addOnDao.getId(),
        "");
  }
}

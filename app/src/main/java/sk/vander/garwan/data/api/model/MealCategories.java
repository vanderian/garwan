package sk.vander.garwan.data.api.model;

import java.util.List;

import auto.parcelgson.AutoParcelGson;
import auto.parcelgson.gson.annotations.SerializedName;

/**
 * Created by arashid on 22/06/16.
 */
@AutoParcelGson
public abstract class MealCategories extends ListResponse<MealCategory> {
  @Override @SerializedName("mealCategories") public abstract List<MealCategory> list();
}

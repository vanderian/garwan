package sk.vander.meals.data.api.model;

import java.util.List;

import auto.parcelgson.AutoParcelGson;
import auto.parcelgson.gson.annotations.SerializedName;

/**
 * Created by arashid on 22/06/16.
 */
@AutoParcelGson
public abstract class Meals extends ListResponse<Meal> {
  @Override @SerializedName("meals") public abstract List<Meal> list();
}

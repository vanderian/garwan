package sk.vander.garwan.data.api.model;

import java.util.List;

import auto.parcelgson.AutoParcelGson;
import auto.parcelgson.gson.annotations.SerializedName;

/**
 * Created by arashid on 22/06/16.
 */
@AutoParcelGson
public abstract class AddOnCategories extends ListResponse<AddOnCategory> {
  @Override @SerializedName("addOnCategories") public abstract List<AddOnCategory> list();
}

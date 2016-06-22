package sk.vander.garwan.data.service;

import retrofit2.http.GET;
import rx.Observable;
import sk.vander.garwan.data.model.AddOnCategories;
import sk.vander.garwan.data.model.AddOns;
import sk.vander.garwan.data.model.MealCategories;
import sk.vander.garwan.data.model.Meals;

/**
 * Created by arashid on 21/06/16.
 */
public interface MenuService {
  @GET("meal") Observable<Meals> getMeals();
  @GET("meal/category") Observable<MealCategories> getMealCategories();
  @GET("addon") Observable<AddOns> getAddons();
  @GET("addon/category") Observable<AddOnCategories> getAddonCategories();
}

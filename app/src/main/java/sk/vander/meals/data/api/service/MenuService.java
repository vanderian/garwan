package sk.vander.meals.data.api.service;

import retrofit2.http.GET;
import rx.Observable;
import sk.vander.meals.data.api.model.AddOnCategories;
import sk.vander.meals.data.api.model.AddOns;
import sk.vander.meals.data.api.model.MealCategories;
import sk.vander.meals.data.api.model.Meals;

/**
 * Created by arashid on 21/06/16.
 */
public interface MenuService {
  @GET("meal") Observable<Meals> getMeals();
  @GET("meal/category") Observable<MealCategories> getMealCategories();
  @GET("addon") Observable<AddOns> getAddons();
  @GET("addon/category") Observable<AddOnCategories> getAddonCategories();
}

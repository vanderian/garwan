package sk.vander.garwan.data.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import sk.vander.garwan.data.model.Addon;
import sk.vander.garwan.data.model.AddonCategory;
import sk.vander.garwan.data.model.Meal;
import sk.vander.garwan.data.model.MealCategory;

/**
 * Created by arashid on 21/06/16.
 */
public interface MenuService {
  @GET("meal") Call<List<Meal>> getMeals();
  @GET("meal/category") Call<List<MealCategory>> getMealCategories();
  @GET("addon") Call<List<Addon>> getAddons();
  @GET("addon/category") Call<List<AddonCategory>> getAddonCategories();
}

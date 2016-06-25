package sk.vander.meals.data.api.model;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 24/06/16.
 */
@AutoParcelGson
public abstract class MealCategory {
  public abstract String id();
  public abstract String name();
  @Nullable public abstract List<Meal> meals();
  public abstract Packaging packaging();
  public abstract String description();
  public abstract int displaySeq();
}

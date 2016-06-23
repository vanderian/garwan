package sk.vander.garwan.data;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.orm.SugarRecord;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import sk.vander.garwan.data.model.AddOn;
import sk.vander.garwan.data.model.AddOnCategories;
import sk.vander.garwan.data.model.AddOnCategory;
import sk.vander.garwan.data.model.AddOns;
import sk.vander.garwan.data.model.Meal;
import sk.vander.garwan.data.model.MealCategories;
import sk.vander.garwan.data.model.MealCategory;
import sk.vander.garwan.data.model.Meals;
import sk.vander.garwan.data.service.MenuService;
import sk.vander.garwan.ui.model.GroupItem;

/**
 * Created by arashid on 21/06/16.
 */
@Singleton
public class MenuProvider {

  private final MenuService menuService;
  private final BehaviorSubject<Boolean> refresh = BehaviorSubject.create(Boolean.TRUE);

  @Inject public MenuProvider(MenuService menuService) {
    this.menuService = menuService;

//    fetchMenuData().filter(Boolean::booleanValue)
//        .subscribe(refresh::onNext, Throwable::printStackTrace);
  }

  @RxLogObservable
  public Observable<Boolean> fetchMenuData() {
    return menuService.getMealCategories()
        .map(MealCategories::getList)
        .doOnNext(SugarRecord::saveInTx)
        .flatMap(x -> menuService.getAddonCategories())
        .map(AddOnCategories::getList)
        .doOnNext(SugarRecord::saveInTx)
        .flatMap(x -> menuService.getMeals())
        .map(Meals::getList)
        .doOnNext(l -> Observable.from(l).subscribe(Meal::initSave))
        .doOnNext(SugarRecord::saveInTx)
        .flatMap(x -> menuService.getAddons())
        .map(AddOns::getList)
        .doOnNext(l -> Observable.from(l).subscribe(AddOn::initSave))
        .doOnNext(SugarRecord::saveInTx)
        .subscribeOn(Schedulers.io())
        .map(x -> true)
        .doOnError(Throwable::printStackTrace)
        .doOnNext(refresh::onNext)
        .onErrorResumeNext(Observable.just(false));
  }

  public Observable<List<GroupItem>> getMeals() {
    return refresh.map(x -> SugarRecord.listAll(MealCategory.class))
        .flatMap(l -> Observable.from(l)
            .map(mc -> GroupItem.create(l.indexOf(mc), mc, SugarRecord.find(Meal.class, "cat_id = ?", mc.getStrId())))
            .toList());
  }

  public Observable<List<AddOnCategory>> getAddons(String ids) {
    final List<String> idsList = Arrays.asList(ids.split(","));
    return refresh.map(x -> SugarRecord.listAll(AddOnCategory.class))
        .flatMap(l -> Observable.from(l)
            .doOnNext(ac -> ac.setAddOnList(getFilteredAddons(ac.getStrId(), idsList)))
            .filter(ac -> !ac.getAddOnList().isEmpty())
            .toList());
  }

  private List<AddOn> getFilteredAddons(String id, List<String> ids) {
    return Observable.from(SugarRecord.find(AddOn.class, "cat_id = ?", id))
        .filter(addOn -> ids.contains(addOn.getStrId()))
        .toList()
        .toBlocking()
        .firstOrDefault(Collections.emptyList());
  }
}

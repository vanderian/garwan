package sk.vander.garwan.data;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.orm.SugarRecord;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import sk.vander.garwan.data.api.service.MenuService;
import sk.vander.garwan.data.database.model.AddOnCategoryDao;
import sk.vander.garwan.data.database.model.AddOnDao;
import sk.vander.garwan.data.database.model.MealCategoryDao;
import sk.vander.garwan.data.database.model.MealDao;
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
  }

  @RxLogObservable
  public Observable<Boolean> fetchMenuData() {
    return Observable.concat(
        menuService.getMealCategories().flatMap(r -> Observable.from(r.list()).map(MealCategoryDao::new)),
        menuService.getAddonCategories().flatMap(r -> Observable.from(r.list()).map(AddOnCategoryDao::new)),
        menuService.getMeals().flatMap(r -> Observable.from(r.list()).map(MealDao::new))
            .flatMap(dao -> Observable.concat(Observable.just(dao.getServingSizeDao()), Observable.just(dao))),
        menuService.getAddons().flatMap(r -> Observable.from(r.list()).map(AddOnDao::new))
            .flatMap(dao -> Observable.concat(Observable.just(dao.getServingSizeDao()), Observable.just(dao))))
        .toList()
        .doOnNext(SugarRecord::saveInTx)
        .subscribeOn(Schedulers.io())
        .map(x -> true)
        .doOnError(Throwable::printStackTrace)
        .doOnNext(refresh::onNext)
        .onErrorResumeNext(Observable.just(false));
  }

  // consider .sample() instead
  public Observable<List<GroupItem>> getMeals() {
    return refresh.map(x -> SugarRecord.listAll(MealCategoryDao.class))
        .flatMap(l -> Observable.from(l)
            .map(mc -> GroupItem.create(mc, SugarRecord.find(MealDao.class, "cat_uid = ?", mc.getUid())))
            .toList());
  }

/*
  public Observable<List<AddOnCategoryDao>> getAddons(String ids) {
    final List<String> idsList = Arrays.asList(ids.split(","));
    return refresh.map(x -> SugarRecord.listAll(AddOnCategoryDao.class))
        .flatMap(l -> Observable.from(l)
            .doOnNext(ac -> ac.setAddOnDaoList(getFilteredAddons(ac.getStrId(), idsList)))
            .filter(ac -> !ac.getAddOnDaoList().isEmpty())
            .toList());
  }

  private List<AddOnDao> getFilteredAddons(String id, List<String> ids) {
    return Observable.from(SugarRecord.find(AddOnDao.class, "cat_id = ?", id))
        .filter(addOn -> ids.contains(addOn.getStrId()))
        .toList()
        .toBlocking()
        .firstOrDefault(Collections.emptyList());
  }
*/
}

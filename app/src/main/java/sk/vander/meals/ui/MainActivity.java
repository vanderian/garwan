package sk.vander.meals.ui;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import sk.vander.meals.App;
import sk.vander.meals.navigation.activity.ActivityScreen;
import sk.vander.meals.navigation.activity.ActivityScreenSwitcher;
import sk.vander.meals.ui.adapter.ExpandableAdapter;
import sk.vander.meals.ui.model.DetailItem;
import sk.vander.meals.ui.model.GroupItem;

public class MainActivity extends ListActivity {
  @Inject ActivityScreenSwitcher screenSwitcher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getComponent(this).inject(this);
    adapter.setSelectionMode(ExpandableAdapter.SelectionMode.SINGLE);
  }

  @Override protected void onResume() {
    super.onResume();
    subscription.add(adapter.onItemClicked()
        .map(DetailItem::addOnIds)
        .filter(s -> !TextUtils.isEmpty(s))
        .doOnNext(x -> screenSwitcher.open(ActivityScreen.create(DetailActivity.class)))
        .subscribe(menuProvider.getSelectedIds()::onNext, Throwable::printStackTrace));
  }

  @Override public Observable<List<GroupItem>> getItemsObservable() {
    return menuProvider.getMeals();
  }
}
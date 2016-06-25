package sk.vander.meals.ui;

import android.os.Bundle;

import java.util.List;

import rx.Observable;
import sk.vander.meals.App;
import sk.vander.meals.ui.model.GroupItem;

public class DetailActivity extends ListActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getComponent(this).inject(this);
  }

  @Override public Observable<List<GroupItem>> getItemsObservable() {
    return menuProvider.getAddons();
  }
}
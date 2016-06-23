package sk.vander.garwan.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;
import sk.vander.garwan.App;
import sk.vander.garwan.R;
import sk.vander.garwan.data.MenuProvider;
import sk.vander.garwan.misc.SwipeRefreshObservable;
import sk.vander.garwan.ui.adapter.ExpandableAdapter;
import sk.vander.garwan.ui.model.DetailItem;
import sk.vander.garwan.ui.model.GroupItem;
import sk.vander.garwan.ui.model.ListSource;

public class MainActivity extends AppCompatActivity {
  private final CompositeSubscription subscription = new CompositeSubscription();
  private final ListSource source = new ListSource();
  private final ExpandableAdapter<GroupItem, DetailItem> adapter = new ExpandableAdapter<>(source);

  @Inject MenuProvider menuProvider;

  @Bind(R.id.list) RecyclerView recyclerView;
  @Bind(R.id.refresh) SwipeRefreshLayout swipeRefreshLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    App.getComponent(this).inject(this);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    recyclerView.setAdapter(adapter);

//    menuProvider.fetchMenuData().filter(Boolean::booleanValue)
//        .subscribe(b -> {}, Throwable::printStackTrace);
//
//    menuProvider.getMeals().subscribe(meals -> Timber.d(meals.toString()));
//    menuProvider.getAddons("5531184695ea165d5f62f809,5531184695ea165d5f62f828").subscribe(a -> Timber.d(a.toString()));


  }

  @Override protected void onResume() {
    super.onResume();
    subscription.add(menuProvider.getMeals().subscribe(source::setGroupItems));
    subscription.add(SwipeRefreshObservable.create(swipeRefreshLayout)
        .concatMap(x -> menuProvider.fetchMenuData())
        .subscribe());
  }

  @Override protected void onPause() {
    super.onPause();
    subscription.clear();
  }
}
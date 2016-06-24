package sk.vander.garwan.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import sk.vander.garwan.App;
import sk.vander.garwan.R;
import sk.vander.garwan.data.MenuProvider;
import sk.vander.garwan.misc.SwipeRefreshObservable;
import sk.vander.garwan.ui.adapter.ExpandableAdapter;
import sk.vander.garwan.ui.model.DetailItem;
import sk.vander.garwan.ui.model.GroupItem;
import sk.vander.garwan.ui.model.ListSource;
import sk.vander.garwan.ui.view.GroupItemView;

public class DetailActivity extends AppCompatActivity {
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
    setContentView(R.layout.activity_list);
    ButterKnife.bind(this);

    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int pos = parent.getChildAdapterPosition(view);
        Observable.just(view)
            .ofType(GroupItemView.class)
            .subscribe(groupItemView ->  groupItemView.showDivider(pos != 0));
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();
    subscription.add(menuProvider.getMeals()
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(source::setGroupItems)
        .doOnNext(x -> adapter.rebuildPositionTranslator())
        .doOnNext(x -> adapter.notifyDataSetChanged())
        .subscribe());
    subscription.add(SwipeRefreshObservable.create(swipeRefreshLayout)
        .concatMap(x -> menuProvider.fetchMenuData())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(x -> swipeRefreshLayout.setRefreshing(false)));
  }

  @Override protected void onPause() {
    super.onPause();
    subscription.clear();
  }
}
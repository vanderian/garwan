package sk.vander.garwan.ui;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import sk.vander.garwan.R;
import sk.vander.garwan.data.MenuProvider;
import sk.vander.garwan.misc.SwipeRefreshObservable;
import sk.vander.garwan.ui.adapter.ExpandableAdapter;
import sk.vander.garwan.ui.model.DetailItem;
import sk.vander.garwan.ui.model.GroupItem;
import sk.vander.garwan.ui.model.ListSource;
import sk.vander.garwan.ui.view.GroupItemView;

public abstract class ListActivity extends AppCompatActivity {
  protected final CompositeSubscription subscription = new CompositeSubscription();
  protected final ListSource source = new ListSource();
  protected final ExpandableAdapter<GroupItem, DetailItem> adapter = new ExpandableAdapter<>(source);

  @Inject MenuProvider menuProvider;

  @Bind(R.id.list) RecyclerView recyclerView;
  @Bind(R.id.refresh) SwipeRefreshLayout swipeRefreshLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_list);
    ButterKnife.bind(this);

    if (savedInstanceState != null) {
      final ExpandableAdapter.SavedState state = savedInstanceState.getParcelable(ExpandableAdapter.SAVE_STATE);
      adapter.restoreState(state, false, true);
    }

    recyclerView.setAdapter(adapter);
    recyclerView.setHasFixedSize(true);
    recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int pos = parent.getChildAdapterPosition(view);
        Observable.just(view)
            .ofType(GroupItemView.class)
            .subscribe(groupItemView -> groupItemView.showDivider(pos != 0));
      }
    });
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    outState.putParcelable(ExpandableAdapter.SAVE_STATE, adapter.getSavedState());
    super.onSaveInstanceState(outState);
  }

  @Override protected void onResume() {
    super.onResume();
    subscription.add(getItemsObservable()
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

  public abstract Observable<List<GroupItem>> getItemsObservable();
}
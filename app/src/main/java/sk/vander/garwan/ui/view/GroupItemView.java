package sk.vander.garwan.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import sk.vander.garwan.R;
import sk.vander.garwan.ui.adapter.BindableView;
import sk.vander.garwan.ui.model.GroupItem;

/**
 * Created by arashid on 23/06/16.
 */
public class GroupItemView extends RelativeLayout implements BindableView<GroupItem> {
  @Bind(R.id.name) TextView name;
  @Bind(R.id.divider) View divider;

  public GroupItemView(Context context) {
    super(context);
  }

  public GroupItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public GroupItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @Override public void onSelected(boolean selected) {

  }

  @Override public void bindTo(GroupItem item) {
    name.setText(item.name());
  }

  @Override public View getView() {
    return this;
  }

  @Override public Observable<Object> getObjectObservable() {
    return null;
  }

  public void showDivider(boolean show) {
    divider.setVisibility(show ? VISIBLE : GONE);
  }
}

package sk.vander.garwan.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import rx.Observable;
import sk.vander.garwan.ui.adapter.BindableView;
import sk.vander.garwan.ui.model.GroupItem;

/**
 * Created by arashid on 23/06/16.
 */
public class GroupItemView extends TextView implements BindableView<GroupItem> {

  public GroupItemView(Context context) {
    super(context);
  }

  public GroupItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public GroupItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override public void onSelected(boolean selected) {

  }

  @Override public void bindTo(GroupItem item) {
    setText(item.name());
  }

  @Override public View getView() {
    return this;
  }

  @Override public Observable<Object> getObjectObservable() {
    return null;
  }
}

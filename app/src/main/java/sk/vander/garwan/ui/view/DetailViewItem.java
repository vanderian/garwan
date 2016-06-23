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
import sk.vander.garwan.ui.model.DetailItem;

/**
 * Created by arashid on 23/06/16.
 */
public class DetailViewItem extends RelativeLayout implements BindableView<DetailItem> {
  @Bind(R.id.name) TextView name;
  @Bind(R.id.price) TextView price;
  @Bind(R.id.size) TextView size;

  public DetailViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    ButterKnife.bind(this);
  }

  @Override public void onSelected(boolean selected) {

  }

  @Override public void bindTo(DetailItem item) {
    name.setText(item.name());
    price.setText(item.price());
    size.setText(item.size());
  }

  @Override public View getView() {
    return this;
  }

  @Override public Observable<Object> getObjectObservable() {
    return null;
  }
}

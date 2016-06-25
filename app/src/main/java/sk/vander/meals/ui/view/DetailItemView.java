package sk.vander.meals.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import sk.vander.meals.R;
import sk.vander.meals.ui.adapter.BindableView;
import sk.vander.meals.ui.model.DetailItem;

/**
 * Created by arashid on 23/06/16.
 */
public class DetailItemView extends RelativeLayout implements BindableView<DetailItem> {
  @Bind(R.id.name) TextView name;
  @Bind(R.id.price) TextView price;
  @Bind(R.id.size) TextView size;

  public DetailItemView(Context context) {
    super(context);
  }

  public DetailItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public DetailItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @Override public void onSelected(boolean selected) {

  }

  @Override public void bindTo(DetailItem item) {
    name.setText(item.name());
    price.setText(NumberFormat.getCurrencyInstance(new Locale("sk", "SK")).format(item.price()));
    size.setText(item.size());
  }

  @Override public View getView() {
    return this;
  }

  @Override public Observable<Object> getObjectObservable() {
    return null;
  }
}

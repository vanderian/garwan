package sk.vander.garwan.ui.adapter;

import android.view.View;

import rx.Observable;

/**
 * Created by vander on 5/15/15.
 */
public interface BindableView<T> {
    void onSelected(boolean selected);
    void bindTo(T item);
    View getView();
    Observable<Object> getObjectObservable();
}

package sk.vander.garwan.misc;

import android.support.v4.widget.SwipeRefreshLayout;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by ttuo on 27/01/15.
 */
public class SwipeRefreshObservable {
  static public Observable<Object> create(SwipeRefreshLayout refreshLayout) {
    return Observable.create(new OnSubscribe(refreshLayout));
  }

  static class OnSubscribe implements SwipeRefreshLayout.OnRefreshListener, Observable.OnSubscribe<Object> {
    final private Subject<Object, Object> subject = PublishSubject.create();

    private OnSubscribe(SwipeRefreshLayout refreshLayout) {
      refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void call(Subscriber<? super Object> subscriber) {
      subject.subscribe(subscriber);
    }

    @Override public void onRefresh() {
      subject.onNext(new Object());
    }
  }
}

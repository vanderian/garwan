package sk.vander.meals.navigation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import sk.vander.meals.navigation.Screen;


public abstract class ActivityScreen implements Screen {

  protected List<Pair<View, String>> pairs = new ArrayList<>();

  @NonNull
  protected Intent intent(Context context) {
    final Intent intent = new Intent();
    if (activityClass() != null) {
      intent.setClass(context, activityClass());
    }
    configureIntent(intent);
    return intent;
  }

  protected Bundle activityOptions(Activity activity) {
    final List<Pair<View, String>> transitionViews = new ArrayList<>(pairs);
//    if (transitionViews.isEmpty()) {
//      return null;
//    }
    pairs = null;

    final View decor = activity.getWindow().getDecorView();
    final View statusBar = decor.findViewById(android.R.id.statusBarBackground);
    final View navBar = decor.findViewById(android.R.id.navigationBarBackground);

    if (statusBar != null) {
      transitionViews.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
    }
    if (navBar != null) {
      transitionViews.add(Pair.create(navBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
    }

    return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionViews.toArray(new Pair[transitionViews.size()])).toBundle();
  }

  protected abstract void configureIntent(@NonNull Intent intent);

  protected abstract Class<? extends Activity> activityClass();

  public static void setTransitionView(View view, String name) {
    ViewCompat.setTransitionName(view, name);
  }

  public static ActivityScreen create(Class<? extends Activity> clazz) {
    return new ActivityScreen() {
      @Override protected void configureIntent(@NonNull Intent intent) {

      }

      @Override protected Class<? extends Activity> activityClass() {
        return clazz;
      }
    };
  }
}

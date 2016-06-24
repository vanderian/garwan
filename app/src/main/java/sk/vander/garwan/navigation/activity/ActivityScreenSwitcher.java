package sk.vander.garwan.navigation.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.security.InvalidParameterException;

import sk.vander.garwan.ActivityConnector;
import sk.vander.garwan.navigation.Screen;
import sk.vander.garwan.navigation.ScreenSwitcher;
import timber.log.Timber;

public class ActivityScreenSwitcher extends ActivityConnector<Activity> implements ScreenSwitcher {

  @Override
  public void open(Screen screen) {
    open(screen, false);
  }

  @Override public void open(Screen screen, boolean finish) {
    final Activity activity = getAttachedObject();
    if (activity == null) {
      return;
    }
    if (screen instanceof ActivityScreen) {
      if (finish) activity.finish();
      ActivityScreen activityScreen = ((ActivityScreen) screen);
      Intent intent = activityScreen.intent(activity);

      if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
        ActivityCompat.startActivity(activity, intent, activityScreen.activityOptions(activity));
      } else {
        final String err = "No activity found to handle intent: " + intent.toString();
        // FIXME: 7/8/15 debug
        Toast.makeText(activity, err, Toast.LENGTH_SHORT).show();
        Timber.e(err);
      }
    } else {
      throw new InvalidParameterException("Only ActivityScreen objects allowed");
    }
  }

  @Override
  public void goBack() {
    final Activity activity = getAttachedObject();
    if (activity != null) {
      activity.onBackPressed();
    }
  }

  public Activity getCurrentActivity() {
    return getAttachedObject();
  }
}

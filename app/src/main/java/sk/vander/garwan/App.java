package sk.vander.garwan;

import android.content.Context;

import com.orm.SugarApp;

import timber.log.Timber;

/**
 * Created by arashid on 21/06/16.
 */
public class App extends SugarApp {
  private AppComponent appComponent;

  @Override public void onCreate() {
    super.onCreate();

    Timber.plant(new Timber.DebugTree());

    appComponent = DaggerAppComponent.builder()
//        .appModule(new AppModule(this))
        .build();
  }

  public static AppComponent getComponent(Context context) {
    return ((App) context.getApplicationContext()).appComponent;
  }
}

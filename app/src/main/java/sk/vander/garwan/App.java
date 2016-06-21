package sk.vander.garwan;

import android.app.Application;
import android.content.Context;

import com.orm.SugarApp;

/**
 * Created by arashid on 21/06/16.
 */
public class App extends SugarApp {
  private AppComponent appComponent;

  @Override public void onCreate() {
    super.onCreate();
    appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .build();
  }

  public static AppComponent getComponent(Context context) {
    return ((App) context.getApplicationContext()).appComponent;
  }
}

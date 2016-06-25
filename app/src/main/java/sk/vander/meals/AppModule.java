package sk.vander.meals;

import android.app.Activity;
import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sk.vander.meals.navigation.activity.ActivityScreenSwitcher;

/**
 * Created by arashid on 21/06/16.
 */
@Module
public class AppModule {

  Application application;

  public AppModule(Application application) {
    this.application = application;
  }

  @Provides @Singleton Application providesApplication() {
    return application;
  }

  @Provides @Singleton ActivityScreenSwitcher provideActivityScreenSwitcher() {
    return new ActivityScreenSwitcher();
  }

  @Provides @Singleton
  ActivityHierarchyServer provideActivityScreenSwitcherServer(final ActivityScreenSwitcher screenSwitcher) {
    return new ActivityHierarchyServer.Empty() {
      @Override
      public void onActivityStarted(Activity activity) {
        screenSwitcher.attach(activity);
      }

      @Override
      public void onActivityStopped(Activity activity) {
        screenSwitcher.detach();
      }
    };
  }
}
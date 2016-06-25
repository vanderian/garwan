package sk.vander.meals;

import javax.inject.Singleton;

import dagger.Component;
import sk.vander.meals.data.DataModule;

/**
 * Created by arashid on 21/06/16.
 */
@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent extends AppGraph {

  final class Initializer {
    static AppComponent init(App app) {
      return DaggerAppComponent.builder()
          .appModule(new AppModule(app))
          .build();
    }

    private Initializer() {
    } // No instances.
  }
}

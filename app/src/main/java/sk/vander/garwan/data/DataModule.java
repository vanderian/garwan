package sk.vander.garwan.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import sk.vander.garwan.data.api.ApiModule;
import sk.vander.garwan.data.api.service.MenuService;

/**
 * Created by arashid on 21/06/16.
 */
@Module(includes = ApiModule.class)
public class DataModule {

  @Singleton @Provides MenuService providesMenuService(Retrofit retrofit) {
    return retrofit.create(MenuService.class);
  }
}

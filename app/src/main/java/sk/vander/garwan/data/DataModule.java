package sk.vander.garwan.data;

import android.widget.ResourceCursorTreeAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import sk.vander.garwan.data.service.MenuService;

/**
 * Created by arashid on 21/06/16.
 */
@Module
public class DataModule {
  private static final String ENDPOINT = "http://papagaj-breweria.herokuapp.com/api/v1/menu/54ca39f401731406200082df/";

  @Singleton @Provides Retrofit provideRetrofit() {
    return new Retrofit.Builder()
        .baseUrl(ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }

  @Singleton @Provides MenuService providesMenuService(Retrofit retrofit) {
    return retrofit.create(MenuService.class);
  }
}

package sk.vander.garwan.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import auto.parcelgson.gson.AutoParcelGsonTypeAdapterFactory;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by arashid on 24/06/16.
 */
@Module
public class ApiModule {
  private static final String ENDPOINT = "http://papagaj-breweria.herokuapp.com/api/v1/menu/54ca39f401731406200082df/";

  @Singleton @Provides Gson provideGson() {
    return new GsonBuilder().registerTypeAdapterFactory(new AutoParcelGsonTypeAdapterFactory()).create();
  }

  @Singleton @Provides Retrofit provideRetrofit(Gson gson) {
    return new Retrofit.Builder()
        .baseUrl(ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }
}

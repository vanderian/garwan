package sk.vander.garwan;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by arashid on 21/06/16.
 */
@Module
public class AppModule {

    Application application;

    public AppModule(Application application) {
        application = application;
    }

    @Provides @Singleton Application providesApplication() {
        return application;
    }
}
package sk.vander.garwan;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import sk.vander.garwan.data.DataModule;
import sk.vander.garwan.ui.MainActivity;

/**
 * Created by arashid on 21/06/16.
 */
@Singleton @Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
}

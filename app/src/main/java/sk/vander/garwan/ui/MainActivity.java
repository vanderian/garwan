package sk.vander.garwan.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import sk.vander.garwan.App;
import sk.vander.garwan.R;
import sk.vander.garwan.data.MenuProvider;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
  @Inject MenuProvider menuProvider;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    App.getComponent(this).inject(this);
    setContentView(R.layout.activity_main);

    menuProvider.fetchMenuData().filter(Boolean::booleanValue)
        .subscribe(b -> {}, Throwable::printStackTrace);

    menuProvider.getMeals().subscribe(meals -> Timber.d(meals.toString()));
    menuProvider.getAddons("5531184695ea165d5f62f809,5531184695ea165d5f62f828").subscribe(a -> Timber.d(a.toString()));
  }
}
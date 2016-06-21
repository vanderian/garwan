package sk.vander.garwan.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sk.vander.garwan.App;
import sk.vander.garwan.R;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    App.getComponent(this).inject(this);
    setContentView(R.layout.activity_main);
  }
}
package sk.vander.meals;

import sk.vander.meals.data.MenuProvider;
import sk.vander.meals.navigation.activity.ActivityScreenSwitcher;
import sk.vander.meals.ui.DetailActivity;
import sk.vander.meals.ui.MainActivity;

/**
 * Created by arashid on 24/06/16.
 */
public interface AppGraph {
  void inject(App app);
  void inject(MainActivity activity);
  void inject(DetailActivity activity);

  MenuProvider menuProvider();
  ActivityHierarchyServer activityHierarchyServer();
  ActivityScreenSwitcher activityScreenSwitcher();
}

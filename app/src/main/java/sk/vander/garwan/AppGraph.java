package sk.vander.garwan;

import sk.vander.garwan.data.MenuProvider;
import sk.vander.garwan.navigation.activity.ActivityScreenSwitcher;
import sk.vander.garwan.ui.DetailActivity;
import sk.vander.garwan.ui.MainActivity;

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

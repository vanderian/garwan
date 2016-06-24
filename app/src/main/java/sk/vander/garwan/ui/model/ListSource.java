package sk.vander.garwan.ui.model;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import sk.vander.garwan.R;
import sk.vander.garwan.ui.adapter.ExpandableSource;

/**
 * Created by arashid on 23/06/16.
 */
public class ListSource implements ExpandableSource<GroupItem, DetailItem> {
  private List<GroupItem> groupItems = Collections.emptyList();

  public boolean setGroupItems(@NonNull List<GroupItem> groupItems) {
    if (!groupItems.equals(this.groupItems)) {
      this.groupItems = groupItems;
      return true;
    }
    return false;
  }

  @Override public GroupItem getGroupItem(int groupPosition) {
    return groupItems.get(groupPosition);
  }

  @Override public DetailItem getChildItem(int groupPosition, int childPosition) {
    return getGroupItem(groupPosition).items().get(childPosition);
  }

  @Override public int getGroupLayoutRes(int viewType) {
    return R.layout.view_item_group;
  }

  @Override public int getChildLayoutRes(int viewType) {
    return R.layout.view_item_detail;
  }

  @Override public int getGroupCount() {
    return groupItems.size();
  }

  @Override public int getChildCount(int groupPosition) {
    return getGroupItem(groupPosition).items().size();
  }

  @Override public long getGroupId(int groupPosition) {
    return getGroupItem(groupPosition).getId();
  }

  @Override public long getChildId(int groupPosition, int childPosition) {
    return getChildItem(groupPosition, childPosition).getId();
  }

  @Override public int getGroupItemViewType(int groupPosition) {
    return 0;
  }

  @Override public int getChildItemViewType(int groupPosition, int childPosition) {
    return 0;
  }
}

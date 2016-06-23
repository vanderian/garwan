package sk.vander.garwan.ui.model;

import java.util.Collections;
import java.util.List;

import sk.vander.garwan.ui.adapter.ExpandableSource;

/**
 * Created by arashid on 23/06/16.
 */
public class ListSource implements ExpandableSource<ListGroupItem, ListDetailItem> {
  private List<ListGroupItem> groupItems = Collections.emptyList();

  public void setGroupItems(List<ListGroupItem> groupItems) {
    this.groupItems = groupItems;
  }

  @Override public ListGroupItem getGroupItem(int groupPosition) {
    return groupItems.get(groupPosition);
  }

  @Override public ListDetailItem getChildItem(int groupPosition, int childPosition) {
    return getGroupItem(groupPosition).items().get(childPosition);
  }

  @Override public int getGroupLayoutRes(int viewType) {
    return 0;
  }

  @Override public int getChildLayoutRes(int viewType) {
    return 0;
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

package sk.vander.garwan.ui.adapter;

import android.support.annotation.LayoutRes;

/**
 * Created by vander on 5/15/15.
 */
public interface ExpandableSource<G, CH> {
    G getGroupItem(int groupPosition);

    CH getChildItem(int groupPosition, int childPosition);

    @LayoutRes int getGroupLayoutRes(int viewType);

    @LayoutRes int getChildLayoutRes(int viewType);

    int getGroupCount();

    int getChildCount(int groupPosition);

    long getGroupId(int groupPosition);

    long getChildId(int groupPosition, int childPosition);

    int getGroupItemViewType(int groupPosition);

    int getChildItemViewType(int groupPosition, int childPosition);

//    boolean onHookGroupExpand(int groupPosition, boolean fromUser);

//    boolean onHookGroupCollapse(int groupPosition, boolean fromUser);

//    boolean onCheckCanExpandOrCollapseGroup(GVH holder, int groupPosition, int x, int y, boolean expand);
}

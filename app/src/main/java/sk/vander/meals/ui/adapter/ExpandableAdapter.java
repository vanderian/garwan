package sk.vander.meals.ui.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.internal.Preconditions;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import auto.parcelgson.AutoParcelGson;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by vander on 5/15/15.
 */
public class ExpandableAdapter<G extends ExpandGroupItem, CH> extends RecyclerView.Adapter<ExpandableAdapter.BaseHolder> {
  private static final int VIEW_TYPE_FLAG_IS_GROUP = ExpandableHelper.VIEW_TYPE_FLAG_IS_GROUP;
  private static final int STATE_FLAG_INITIAL_VALUE = -1;

  public static final String SAVE_STATE = "save_adapter_state";
  /**
   * State flag for the {@link ExpandableViewHolder#setExpandStateFlags(int)} and {@link ExpandableViewHolder#getExpandStateFlags()}
   * methods.
   * Indicates that this ViewHolder is associated to group item.
   */
  @SuppressWarnings("PointlessBitwiseExpression")
  public static final int STATE_FLAG_IS_GROUP = (1 << 0);

  /**
   * State flag for the {@link ExpandableViewHolder#setExpandStateFlags(int)} and {@link ExpandableViewHolder#getExpandStateFlags()}
   * methods.
   * Indicates that this ViewHolder is associated to child item.
   */
  public static final int STATE_FLAG_IS_CHILD = (1 << 1);

  /**
   * State flag for the {@link ExpandableViewHolder#setExpandStateFlags(int)} and {@link ExpandableViewHolder#getExpandStateFlags()}
   * methods.
   * Indicates that this is a group item.
   */
  public static final int STATE_FLAG_IS_EXPANDED = (1 << 2);

  /**
   * State flag for the {@link ExpandableViewHolder#setExpandStateFlags(int)} and {@link ExpandableViewHolder#getExpandStateFlags()}
   * methods.
   * If this flag is set, some other flags are changed and require to apply.
   */
  public static final int STATE_FLAG_IS_UPDATED = (1 << 31);

  private final PublishSubject<G> onGroupClicked = PublishSubject.create();
  private final PublishSubject<ObjectEvent> onGroupViewClicked = PublishSubject.create();
  private final PublishSubject<CH> onItemClicked = PublishSubject.create();
  private final PublishSubject<ObjectEvent> onItemViewClicked = PublishSubject.create();
  private final PublishSubject<OnExpandEvent> onExpandEvent = PublishSubject.create();

  private ExpandableSource<G, CH> source;
  private SparseArray<SparseBooleanArray> selectedItems;
  private ExpandablePositionTranslator positionTranslator;
  private SelectionMode selectionMode = SelectionMode.NONE;
  private ExpandMode expandMode = ExpandMode.SINGLE;

  public ExpandableAdapter(ExpandableSource<G, CH> items) {
    source = items;
    selectedItems = new SparseArray<>();
    positionTranslator = new ExpandablePositionTranslator();
    positionTranslator.build(source);
  }

  public void setSelectionMode(SelectionMode mode) {
    selectionMode = mode;
  }

  public void setExpandMode(ExpandMode mode) {
    expandMode = mode;
  }

  @SuppressWarnings("unchecked")
  @Override public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (source == null) {
      return null;
    }

    final int maskedViewType = (viewType & (~VIEW_TYPE_FLAG_IS_GROUP));

    final boolean isGroup = (viewType & VIEW_TYPE_FLAG_IS_GROUP) != 0;

    final View inflatedView = LayoutInflater.from(parent.getContext()).inflate(
        isGroup ? source.getGroupLayoutRes(maskedViewType)
            : source.getChildLayoutRes(maskedViewType),
        parent, false
    );

    final BaseHolder holder = isGroup
        ? new GroupViewHolder<>((BindableView<G>) inflatedView)
        : new ChildViewHolder<>((BindableView<CH>) inflatedView);

    if (holder instanceof ExpandableViewHolder) {
      final ExpandableViewHolder viewHolder = (ExpandableViewHolder) holder;
      viewHolder.setExpandStateFlags(STATE_FLAG_INITIAL_VALUE);
    }
    return holder;
  }

  @SuppressWarnings("unchecked")
  @Override public void onBindViewHolder(BaseHolder holder, int position) {
    if (source == null) {
      return;
    }

    final Pair<Integer, Integer> pos = getGroupChildPos(position);
//        final int viewType = (holder.getItemViewType() & (~VIEW_TYPE_FLAG_IS_GROUP));

    // update flags
    int flags = 0;

    if (pos.second == RecyclerView.NO_POSITION) {
      flags |= STATE_FLAG_IS_GROUP;
    } else {
      flags |= STATE_FLAG_IS_CHILD;
    }

    if (positionTranslator.isGroupExpanded(pos.first)) {
      flags |= STATE_FLAG_IS_EXPANDED;
    }

    safeUpdateExpandStateFlags(holder, flags);

    if (pos.second == RecyclerView.NO_POSITION) {
      Preconditions.checkArgument(holder instanceof GroupViewHolder, "invalid view holder");
      final G item = source.getGroupItem(pos.first);
      item.isExpanded = (flags & STATE_FLAG_IS_EXPANDED) != 0;
      item.position = pos.first;
      ((GroupViewHolder<G>) holder).bindTo(item);

    } else {
      Preconditions.checkArgument(holder instanceof ChildViewHolder, "invalid view holder");
      ((ChildViewHolder<CH>) holder).bindTo(source.getChildItem(pos.first, pos.second), isSelected(pos.first, pos.second));
    }
  }

  @Override public int getItemCount() {
    return positionTranslator.getItemCount();
  }

  @Override public long getItemId(int position) {
    if (source == null) {
      return RecyclerView.NO_ID;
    }

    final long expandablePosition = positionTranslator.getExpandablePosition(position);
    final int groupPosition = ExpandableHelper.getPackedPositionGroup(expandablePosition);
    final int childPosition = ExpandableHelper.getPackedPositionChild(expandablePosition);

    if (childPosition == RecyclerView.NO_POSITION) {
      final long groupId = source.getGroupId(groupPosition);
      return ExpandableHelper.getCombinedGroupId(groupId);
    } else {
      final long groupId = source.getGroupId(groupPosition);
      final long childId = source.getChildId(groupPosition, childPosition);
      //TODO how to to this with string translateId
//           purposeError
      return ExpandableHelper.getCombinedChildId(groupId, childId);
    }
  }

  @Override public int getItemViewType(int position) {
    if (source == null) {
      return 0;
    }

    final long expandablePosition = positionTranslator.getExpandablePosition(position);
    final int groupPosition = ExpandableHelper.getPackedPositionGroup(expandablePosition);
    final int childPosition = ExpandableHelper.getPackedPositionChild(expandablePosition);

    final boolean noChilds = childPosition == RecyclerView.NO_POSITION;
    final int type = noChilds
        ? source.getGroupItemViewType(groupPosition)
        : source.getChildItemViewType(groupPosition, childPosition);

    if ((type & VIEW_TYPE_FLAG_IS_GROUP) != 0) {
      throw new IllegalStateException("Illegal view type (type = " + Integer.toHexString(type) + ")");
    }

    return noChilds ? (type | VIEW_TYPE_FLAG_IS_GROUP) : type;
  }

  @SuppressWarnings("unchecked")
  @Override public void onViewAttachedToWindow(BaseHolder holder) {
    holder.subscriptions = new CompositeSubscription();
    if (holder instanceof GroupViewHolder) {
      final GroupViewHolder<G> viewHolder = (GroupViewHolder) holder;
      holder.subscriptions.add(RxView.clicks(viewHolder.itemView.getView())
          .map(ev -> viewHolder.getAdapterPosition())
          .doOnNext(this::onGroupItem)
          .map(ev -> viewHolder.item)
          .subscribe(onGroupClicked));
      if (viewHolder.itemView.getObjectObservable() != null) {
        holder.subscriptions.add(viewHolder.itemView.getObjectObservable()
            .map(o -> new ObjectEvent(viewHolder, o))
            .subscribe(onGroupViewClicked));
      }
    }
    if (holder instanceof ChildViewHolder) {
      final ChildViewHolder<CH> viewHolder = (ChildViewHolder) holder;
      holder.subscriptions.add(RxView.clicks(viewHolder.itemView.getView())
          .filter(ev -> selectionMode != SelectionMode.NONE)
          .map(ev -> getGroupChildPos(holder.getAdapterPosition()))
          .doOnNext(pos -> {
            if (selectionMode == SelectionMode.SINGLE) {
              toggleSelection(getSelectedItem());
            }
          })
          .doOnNext(this::toggleSelection)
          .map(x -> viewHolder.item)
          .subscribe(onItemClicked));

      if (viewHolder.itemView.getObjectObservable() != null) {
        holder.subscriptions.add(viewHolder.itemView.getObjectObservable()
            .debounce(600, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .map(o -> new ObjectEvent(viewHolder, o))
            .subscribe(onItemViewClicked));
      }
    }
  }

  @Override public void onViewDetachedFromWindow(BaseHolder holder) {
    if (holder.subscriptions.hasSubscriptions() && !holder.subscriptions.isUnsubscribed()) {
      holder.subscriptions.unsubscribe();
      holder.subscriptions = null;
    }
  }

  private boolean isSelected(int groupPosition, int childPosition) {
    return selectedItems.get(groupPosition, new SparseBooleanArray()).get(childPosition, false);
  }

  public void toggleSelection(Pair<Integer, Integer> pos) {
    if (pos.second != RecyclerView.NO_POSITION) {
      final SparseBooleanArray groupSelected = selectedItems.get(pos.first, new SparseBooleanArray());
      if (groupSelected.get(pos.second, false)) {
        groupSelected.delete(pos.second);
        if (groupSelected.size() == 0) {
          selectedItems.delete(pos.first);
        }
      } else {
        if (groupSelected.size() == 0) {
          selectedItems.put(pos.first, groupSelected);
        }
        groupSelected.put(pos.second, true);
      }
      notifyChildItemChanged(pos.first, pos.second);
    }
  }

  public void clearSelections() {
    selectedItems.clear();
    notifyDataSetChanged();
  }

  public void clearSelection(int group, int child) {
    final SparseBooleanArray array = selectedItems.get(group, new SparseBooleanArray());
    array.delete(child);
    selectedItems.put(group, array);
  }

  public int getSelectedItemCount() {
    int count = 0;
    for (int i = 0; i < selectedItems.size(); i++) {
      count += selectedItems.valueAt(i).size();
    }

    return count;
  }

  public int getSelectedItemCount(int groupPosition) {
    return selectedItems.get(groupPosition, new SparseBooleanArray()).size();
  }

  public List<Pair<Integer, Integer>> getSelectedItems() {
    List<Pair<Integer, Integer>> items = new ArrayList<>(getSelectedItemCount());
    for (int i = 0; i < selectedItems.size(); i++) {
      for (int j = 0; j < selectedItems.valueAt(i).size(); j++) {
        if (selectedItems.valueAt(i).valueAt(j)) {
          items.add(new Pair<>(selectedItems.keyAt(i), selectedItems.valueAt(i).keyAt(j)));
        }
      }
    }
    return items;
  }

  public List<Integer> getSelectedItems(int groupPosition) {
    List<Integer> items = new ArrayList<>(getSelectedItemCount(groupPosition));
    final SparseBooleanArray arr = selectedItems.get(groupPosition, new SparseBooleanArray());
    for (int i = 0; i < arr.size(); i++) {
      items.add(arr.keyAt(i));
    }
    return items;
  }

  public void setSelectedItems(int groupPosition, List<Integer> selection) {
    final SparseBooleanArray arr = new SparseBooleanArray();
    for (int i : selection) {
      arr.put(i, true);
    }
    selectedItems.put(groupPosition, arr);
  }

  public Pair<Integer, Integer> getSelectedItem() {
    for (int i = 0; i < selectedItems.size(); i++) {
      final SparseBooleanArray array = selectedItems.valueAt(i);
      final int idx = array.indexOfValue(true);
      if (idx >= 0) {
        return new Pair<>(i, idx);
      }
    }
    return new Pair<>(RecyclerView.NO_POSITION, RecyclerView.NO_POSITION);
  }

  private Pair<Integer, Integer> getGroupChildPos(int pos) {
    final long expandablePosition = positionTranslator.getExpandablePosition(pos);
    final int groupPosition = ExpandableHelper.getPackedPositionGroup(expandablePosition);
    final int childPosition = ExpandableHelper.getPackedPositionChild(expandablePosition);
    return new Pair<>(groupPosition, childPosition);
  }

  public void rebuildPositionTranslator() {
    if (positionTranslator != null) {
      int[] savedState = positionTranslator.getSavedStateArray();
      positionTranslator.build(source);

      // NOTE: do not call hook routines and listener methods
      positionTranslator.restoreExpandedGroupItems(savedState, null, null);
    }
  }

  private void onGroupItem(int pos) {
    final long expandablePosition = positionTranslator.getExpandablePosition(pos);
    final int groupPosition = ExpandableHelper.getPackedPositionGroup(expandablePosition);
    final boolean expand = !(positionTranslator.isGroupExpanded(groupPosition));

    if (ExpandMode.SINGLE.equals(expandMode)) {
      Observable.range(0, source.getGroupCount())
          .filter(positionTranslator::isGroupExpanded)
          .subscribe(p -> collapseGroup(p, false));
    }

    if (expand) {
      expandGroup(groupPosition, true);
    } else {
      collapseGroup(groupPosition, true);
    }
  }

  public boolean collapseGroup(int groupPosition, boolean fromUser) {
    if (!positionTranslator.isGroupExpanded(groupPosition)) {
      return false;
    }

    // call hook method
//        if (!source.onHookGroupCollapse(groupPosition, fromUser)) {
//            return false;
//        }

    if (positionTranslator.collapseGroup(groupPosition)) {
      final long packedPosition = ExpandableHelper.getPackedPositionForGroup(groupPosition);
      final int flatPosition = positionTranslator.getFlatPosition(packedPosition);
      final int childCount = positionTranslator.getChildCount(groupPosition);

      notifyItemRangeRemoved(flatPosition + 1, childCount);
    }

    onExpandEvent.onNext(OnExpandEvent.create(ExpandEvent.COLLAPSE, groupPosition, fromUser));

    final long packedPosition = ExpandableHelper.getPackedPositionForGroup(groupPosition);
    final int flatPosition = positionTranslator.getFlatPosition(packedPosition);
    notifyItemChanged(flatPosition);

    return true;
  }

  public boolean expandGroup(int groupPosition, boolean fromUser) {
    if (positionTranslator.isGroupExpanded(groupPosition)) {
      return false;
    }

    // call hook method
//        if (!source.onHookGroupExpand(groupPosition, fromUser)) {
//            return false;
//        }

    if (positionTranslator.expandGroup(groupPosition)) {
      final long packedPosition = ExpandableHelper.getPackedPositionForGroup(groupPosition);
      final int flatPosition = positionTranslator.getFlatPosition(packedPosition);
      final int childCount = positionTranslator.getChildCount(groupPosition);

      notifyItemRangeInserted(flatPosition + 1, childCount);
    }

    onExpandEvent.onNext(OnExpandEvent.create(ExpandEvent.EXPAND, groupPosition, fromUser));

    final long packedPosition = ExpandableHelper.getPackedPositionForGroup(groupPosition);
    final int flatPosition = positionTranslator.getFlatPosition(packedPosition);
    notifyItemChanged(flatPosition);

    return true;
  }

  public boolean isGroupExpanded(int groupPosition) {
    return positionTranslator.isGroupExpanded(groupPosition);
  }

  long getExpandablePosition(int flatPosition) {
    return positionTranslator.getExpandablePosition(flatPosition);
  }

  int getFlatPosition(long packedPosition) {
    return positionTranslator.getFlatPosition(packedPosition);
  }

  int[] getExpandedItemsSavedStateArray() {
    if (positionTranslator != null) {
      return positionTranslator.getSavedStateArray();
    } else {
      return null;
    }
  }

  public void notifyGroupItemChanged(int groupPosition) {
    final long packedPosition = ExpandableHelper.getPackedPositionForGroup(groupPosition);
    final int flatPosition = positionTranslator.getFlatPosition(packedPosition);

    if (flatPosition != RecyclerView.NO_POSITION) {
      notifyItemChanged(flatPosition);
    }
  }

  void notifyGroupAndChildrenItemsChanged(int groupPosition) {
    final long packedPosition = ExpandableHelper.getPackedPositionForGroup(groupPosition);
    final int flatPosition = positionTranslator.getFlatPosition(packedPosition);
    final int visibleChildCount = positionTranslator.getVisibleChildCount(groupPosition);

    if (flatPosition != RecyclerView.NO_POSITION) {
      notifyItemRangeChanged(flatPosition, 1 + visibleChildCount);
    }
  }

  void notifyChildrenOfGroupItemChanged(int groupPosition) {
    final int visibleChildCount = positionTranslator.getVisibleChildCount(groupPosition);

    // notify if the group is expanded
    if (visibleChildCount > 0) {
      final long packedPosition = ExpandableHelper.getPackedPositionForChild(groupPosition, 0);
      final int flatPosition = positionTranslator.getFlatPosition(packedPosition);

      if (flatPosition != RecyclerView.NO_POSITION) {
        notifyItemRangeChanged(flatPosition, visibleChildCount);
      }
    }
  }

  void notifyChildItemChanged(int groupPosition, int childPosition) {
    notifyChildItemRangeChanged(groupPosition, childPosition, 1);
  }

  void notifyChildItemRangeChanged(int groupPosition, int childPositionStart, int itemCount) {
    final int visibleChildCount = positionTranslator.getVisibleChildCount(groupPosition);

    // notify if the group is expanded
    if ((visibleChildCount > 0) && (childPositionStart < visibleChildCount)) {
      final long packedPosition = ExpandableHelper.getPackedPositionForChild(groupPosition, 0);
      final int flatPosition = positionTranslator.getFlatPosition(packedPosition);

      if (flatPosition != RecyclerView.NO_POSITION) {
        final int startPosition = flatPosition + childPositionStart;
        final int count = Math.min(itemCount, (visibleChildCount - childPositionStart));

        notifyItemRangeChanged(startPosition, count);
      }
    }
  }

  private static void safeUpdateExpandStateFlags(RecyclerView.ViewHolder holder, int flags) {
    if (!(holder instanceof ExpandableViewHolder)) {
      return;
    }

    final ExpandableViewHolder holder2 = (ExpandableViewHolder) holder;

    final int curFlags = holder2.getExpandStateFlags();
    final int mask = ~STATE_FLAG_IS_UPDATED;

    // append UPDATED flag
    if ((curFlags == STATE_FLAG_INITIAL_VALUE) || (((curFlags ^ flags) & mask) != 0)) {
      flags |= STATE_FLAG_IS_UPDATED;
    }

    holder2.setExpandStateFlags(flags);
  }

  public void setSource(ExpandableSource<G, CH> items) {
    this.source = items;
    notifyDataSetChanged();
  }

  public Observable<CH> onItemClicked() {
    return onItemClicked;
  }

  public Observable<ObjectEvent> onItemEvent() {
    return onItemViewClicked;
  }

  public Observable<G> onGroupClicked() {
    return onGroupClicked;
  }

  public Observable<ObjectEvent> onGroupEvent() {
    return onGroupViewClicked;
  }

  public Observable<OnExpandEvent> onExpandEvent() {
    return onExpandEvent;
  }


    /* saved state */

  /**
   * Gets saved state object in order to restore the internal state.
   * <p>
   * Call this method in Activity/Fragment's onSavedInstance() and save to the bundle.
   *
   * @return The Parcelable object which stores information need to restore the internal states.
   */

  public Parcelable getSavedState() {
    return new SavedState(getExpandedItemsSavedStateArray());
  }

  public void restoreState(SavedState state, boolean callHook, boolean callListeners) {
    positionTranslator.restoreExpandedGroupItems(
        state.adapterSavedState,
        (callHook ? source : null),
        (callListeners ? onExpandEvent : null));
//                (callListeners ? mOnGroupExpandListener : null),
//                (callListeners ? mOnGroupCollapseListener : null));
  }

  public static class SavedState implements Parcelable {
    final int[] adapterSavedState;

    public SavedState(int[] adapterSavedState) {
      this.adapterSavedState = adapterSavedState;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeIntArray(this.adapterSavedState);
    }

    private SavedState(Parcel in) {
      this.adapterSavedState = in.createIntArray();
    }

    public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
      public SavedState createFromParcel(Parcel source) {
        return new SavedState(source);
      }

      public SavedState[] newArray(int size) {
        return new SavedState[size];
      }
    };
  }

  public static class ChildViewHolder<T> extends BaseHolder {
    private final BindableView<T> itemView;
    protected T item;
//        private int pos;

    public ChildViewHolder(BindableView<T> itemView) {
      super(itemView.getView());
      this.itemView = itemView;
    }

    public void bindTo(T item, boolean selected) {
      this.item = item;
//            this.pos = pos;
      itemView.onSelected(selected);
      itemView.bindTo(item);
    }
  }

  public static class GroupViewHolder<T extends ExpandGroupItem> extends BaseHolder implements ExpandableViewHolder {
    private int expandStateFlags;

    public final BindableView<T> itemView;
    protected T item;
//        private int pos;

    public GroupViewHolder(BindableView<T> itemView) {
      super(itemView.getView());
      this.itemView = itemView;
    }

    public void bindTo(T item) {
      this.item = item;
//            this.pos = pos;
      itemView.bindTo(item);
    }

    @Override public void setExpandStateFlags(int flags) {
      expandStateFlags = flags;
    }

    @Override public int getExpandStateFlags() {
      return expandStateFlags;
    }
  }

  abstract static class BaseHolder extends RecyclerView.ViewHolder {
    private CompositeSubscription subscriptions;
    View itemView;

    public BaseHolder(View itemView) {
      super(itemView);
      this.itemView = itemView;
    }
  }

  public class ObjectEvent {
    private final BaseHolder viewHolder;
    private final Object object;

    public ObjectEvent(BaseHolder viewHolder, Object object) {

      this.viewHolder = viewHolder;
      this.object = object;
    }

    public BaseHolder getViewHolder() {
      return viewHolder;
    }

    public Object getObject() {
      return object;
    }

    public boolean typeOf(Class<?> clazz) {
      return clazz.isInstance(object);
    }
  }

  @AutoParcelGson
  public static abstract class OnExpandEvent {
    public abstract ExpandEvent expandEvent();

    public abstract int groupPosition();

    public abstract boolean fromUser();

    public static OnExpandEvent create(ExpandEvent event, int position, boolean fromUser) {
      return new AutoParcelGson_ExpandableAdapter_OnExpandEvent(event, position, fromUser);
    }
  }

  public enum ExpandEvent {
    EXPAND,
    COLLAPSE
  }

  public enum SelectionMode {
    NONE,
    SINGLE,
    MULTIPLE
  }

  public enum ExpandMode {
    SINGLE,
    MULTIPLE
  }
}

/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package bulat.diet.helper_sport.adapter;

import java.text.DecimalFormat;

import android.preference.PreferenceManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.common.data.AbstractExpandableDataProvider;
import bulat.diet.helper_sport.common.data.ExampleExpandableDataProvider;
import bulat.diet.helper_sport.common.data.ExampleExpandableDataProvider.DayTimeGroupData;
import bulat.diet.helper_sport.common.utils.DrawableUtils;
import bulat.diet.helper_sport.common.utils.ViewUtils;
import bulat.diet.helper_sport.common.widget.ExpandableItemIndicator;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableDraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableSwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;

public class ExpandableDraggableSwipeableExampleAdapter
        extends AbstractExpandableItemAdapter<ExpandableDraggableSwipeableExampleAdapter.DayTimeViewHolder, ExpandableDraggableSwipeableExampleAdapter.TodayDishItemViewHolder>
        implements ExpandableDraggableItemAdapter<ExpandableDraggableSwipeableExampleAdapter.DayTimeViewHolder, ExpandableDraggableSwipeableExampleAdapter.TodayDishItemViewHolder>,
        ExpandableSwipeableItemAdapter<ExpandableDraggableSwipeableExampleAdapter.DayTimeViewHolder, ExpandableDraggableSwipeableExampleAdapter.TodayDishItemViewHolder> {
    private static final String TAG = "MyEDSItemAdapter";

    // NOTE: Make accessible with short name
    private interface Expandable extends ExpandableItemConstants {
    }

    private interface Draggable extends DraggableItemConstants {
    }

    private interface Swipeable extends SwipeableItemConstants {
    }

    private final RecyclerViewExpandableItemManager mExpandableItemManager;

    public ExampleExpandableDataProvider getProvider() {
        return mProvider;
    }

    public void setProvider(ExampleExpandableDataProvider mProvider) {
        this.mProvider = mProvider;
    }

    private ExampleExpandableDataProvider mProvider;
    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;
    private View.OnClickListener mSwipeableViewContainerOnClickListener;

    public interface EventListener {
        void onGroupItemButtonClick(String groupId);

        void onGroupItemRemoved(int groupPosition);

        void onChildItemRemoved(int groupPosition, int childPosition);

        void onGroupItemPinned(int groupPosition);

        void onChildItemPinned(int groupPosition, int childPosition);

        void onItemViewClicked(View v, boolean pinned);
    }

    public static abstract class DishItemViewHolder extends AbstractDraggableSwipeableItemViewHolder implements ExpandableItemViewHolder {

        public FrameLayout mContainer;
        public FrameLayout mMainContainer;
        public View mDragHandle;
        public TextView mNameTextView;

        // mCarbon it is also amount of additional weight for activity (integer)
        public TextView mCarbon;

        // mCarbon it is also amount of additional weight for activity (decimal)
        public TextView mAbsProtein;

        // mFat it is also amount of repeat for activity
        public TextView mFat;
        public TextView mProtein;
        public TextView mApproaches;
        public TextView mCalory;
        public TextView mTime;
        public TextView mDishWeight;
        public LinearLayout mLinearLayoutFCP;

        public LinearLayout mDdishWeightLayout;

        private int mExpandStateFlags;

        public DishItemViewHolder(View v) {
            super(v);
            mMainContainer = (FrameLayout) v.findViewById(R.id.main_container);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mNameTextView = (TextView) v.findViewById(R.id.textViewDishName);
            mCarbon = (TextView) v.findViewById(R.id.textViewCarbon);
            mFat = (TextView) v.findViewById(R.id.textViewFat);
            mAbsProtein = (TextView) v.findViewById(R.id.textViewAdditionalWeight);
            mApproaches = (TextView) v.findViewById(R.id.textViewApproaches);
            mProtein = (TextView) v.findViewById(R.id.textViewProtein);
            mCalory = (TextView) v.findViewById(R.id.textViewDishCalorie);
            mDishWeight = (TextView) v.findViewById(R.id.textViewDishWeight);
            mTime = (TextView) v.findViewById(R.id.textViewTime);

            mLinearLayoutFCP = (LinearLayout) v.findViewById(R.id.linearLayoutFCP);
            mDdishWeightLayout = (LinearLayout) v.findViewById(R.id.dishWeightLayout);
        }

        @Override
        public int getExpandStateFlags() {
            return mExpandStateFlags;
        }

        @Override
        public void setExpandStateFlags(int flag) {
            mExpandStateFlags = flag;
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }
    }

    public static class DayTimeViewHolder extends DishItemViewHolder {
        public ImageButton addDishButton;
        public ExpandableItemIndicator mIndicator;
        public TextView carbonSubTotal;
        public TextView fatSubTotal;
        public TextView proteinSubTotal;
		public TextView calorySubTotal;
        public LinearLayout mGroupDishWeight;
        public LinearLayout mGroupDishCalorisity;

        public DayTimeViewHolder(View v) {
            super(v);
            mNameTextView = (TextView) v.findViewById(R.id.dishType);
            mIndicator = (ExpandableItemIndicator) v.findViewById(R.id.indicator);
            carbonSubTotal = (TextView) v.findViewById(R.id.textViewCarbon);
            fatSubTotal = (TextView) v.findViewById(R.id.textViewFat);
            proteinSubTotal = (TextView) v.findViewById(R.id.textViewProtein);
            calorySubTotal = (TextView) v.findViewById(R.id.textViewCalory);
            addDishButton = (ImageButton) v.findViewById(R.id.addDishButton);
            mGroupDishWeight = (LinearLayout) v.findViewById(R.id.group_dish_weight);
            mGroupDishCalorisity = (LinearLayout) v.findViewById(R.id.group_dish_caloricity);

        }
    }

    public static class TodayDishItemViewHolder extends DishItemViewHolder {
        public TodayDishItemViewHolder(View v) {
            super(v);
        }
    }

    public ExpandableDraggableSwipeableExampleAdapter(
            RecyclerViewExpandableItemManager expandableItemManager,
            ExampleExpandableDataProvider dataProvider) {
        mExpandableItemManager = expandableItemManager;
        mProvider = dataProvider;
        mItemViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClick(v);
            }
        };
        mSwipeableViewContainerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeableViewContainerClick(v);
            }
        };

        // ExpandableItemAdapter, ExpandableDraggableItemAdapter and ExpandableSwipeableItemAdapter
        // require stable ID, and also have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    private void onItemViewClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(v, true);  // true --- pinned
        }
    }

    private void onSwipeableViewContainerClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), false);  // false --- not pinned
        }
    }

    @Override
    public int getGroupCount() {
        return mProvider.getGroupCount();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mProvider.getChildCount(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mProvider.getGroupItem(groupPosition).getGroupId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mProvider.getChildItem(groupPosition, childPosition).getChildId();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChild2ItemViewType(int groupPosition, int childPosition) {
        return 2;
    }

    @Override
    public DayTimeViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_group_item, parent, false);
        return new DayTimeViewHolder(v);
    }

    @Override
    public TodayDishItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v;
        if (viewType != 2) {
            v = inflater.inflate(R.layout.list_child_item, parent, false);
        } else {
            v = inflater.inflate(R.layout.list_child_sport_item, parent, false);
        }

        return new TodayDishItemViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(final DayTimeViewHolder holder, int groupPosition, int viewType) {
        // group item
        final DayTimeGroupData item = mProvider.getGroupItem(groupPosition);

        // set listeners
        holder.itemView.setOnClickListener(mItemViewOnClickListener);
        DecimalFormat df = new DecimalFormat("###.#");
        // set text
        holder.mNameTextView.setText(item.getText());
        holder.carbonSubTotal.setText(df.format(item.getCarbon()));
        holder.fatSubTotal.setText(df.format( item.getFat()));
        holder.proteinSubTotal.setText(df.format( item.getProtein()));
        holder.calorySubTotal.setText("" + item.getCalory());
        holder.mDishWeight.setText("" + item.getWeight());
        holder.addDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventListener.onGroupItemButtonClick(holder.mNameTextView.getText().toString());
            }
        });

        if (!TextUtils.isEmpty(holder.mNameTextView.getText()) && holder.mNameTextView.getText().toString().equals(holder.mNameTextView.getContext().getString(R.string.water_name)))  {
            holder.mLinearLayoutFCP.setVisibility(View.GONE);
            holder.mGroupDishWeight.setVisibility(View.GONE);
            holder.mGroupDishCalorisity.setVisibility(View.GONE);
            holder.mGroupDishWeight.setVisibility(View.VISIBLE);
        } else if (item.getGroupId() != getGroupCount()-1) {
            holder.mLinearLayoutFCP.setVisibility(View.VISIBLE);
            holder.mGroupDishWeight.setVisibility(View.VISIBLE);
        } else {
            holder.mLinearLayoutFCP.setVisibility(View.GONE);
            holder.mGroupDishWeight.setVisibility(View.GONE);
        }

        // set background resource (target view ID: container)
        final int dragState = holder.getDragStateFlags();
        final int expandState = holder.getExpandStateFlags();
        final int swipeState = holder.getSwipeStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0) ||
                ((expandState & Expandable.STATE_FLAG_IS_UPDATED) != 0) ||
                ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;
            boolean isExpanded;
            boolean animateIndicator = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_group_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.getForeground());
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_group_item_dragging_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_group_item_swiping_active_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_group_item_swiping_state;
            } else if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                bgResId = R.drawable.bg_group_item_expanded_state;
            } else {
                bgResId = R.drawable.bg_group_item_normal_state;
            }

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                isExpanded = true;
            } else {
                isExpanded = false;
            }

            holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
        }

        // set swiping properties
        holder.setSwipeItemHorizontalSlideAmount(
                item.isPinned() ? Swipeable.OUTSIDE_OF_THE_WINDOW_LEFT : 0);
    }

    @Override
    public void onBindChildViewHolder(TodayDishItemViewHolder holder, int groupPosition, int childPosition, int viewType) {
        // child item
        final ExampleExpandableDataProvider.DishItemData item = (ExampleExpandableDataProvider.DishItemData) mProvider.getChildItem(groupPosition, childPosition);

        // set listeners
        // (if the item is *pinned*, click event comes to the itemView)
        holder.mMainContainer.setOnClickListener(mItemViewOnClickListener);
        holder.itemView.setOnClickListener(mItemViewOnClickListener);
        // (if the item is *not pinned*, click event comes to the mContainer)
        holder.mContainer.setOnClickListener(mSwipeableViewContainerOnClickListener);

        // set text
        holder.mNameTextView.setText(item.getText());
        holder.mCalory.setText("" + item.getDishInfo().getCaloricity());
        if  (holder.mAbsProtein != null) {
            //if activity params
            holder.mFat.setText("" + (int)(item.getDishInfo().getFat() + 1));
            holder.mApproaches.setText("" + (int)(item.getDishInfo().getProtein() + 1));
        } else {
            //if dish params
            holder.mFat.setText("" + item.getDishInfo().getFat());
        }
        holder.mProtein.setText("" + item.getDishInfo().getProtein());
        holder.mCarbon.setText("" + item.getDishInfo().getCarbon());
        if  (holder.mAbsProtein != null) holder.mAbsProtein.setText("" + (item.getDishInfo().getCarbon() + item.getDishInfo().getAbsProtein()/10));

            holder.mDishWeight.setText("" + item.getDishInfo().getWeight());
        holder.mTime.setText("" + item.getDishInfo().getDateTimeHH() +  ":" + ((item.getDishInfo().getDateTimeMM() > 9) ? item.getDishInfo().getDateTimeMM() : "0" +item.getDishInfo().getDateTimeMM()) );

        if (holder.mDdishWeightLayout != null) {
            if (item.getDishInfo().getAbsCarbon() > 0) {
                holder.mLinearLayoutFCP.setVisibility(View.VISIBLE);
                holder.mDdishWeightLayout.setVisibility(View.GONE);
            } else {
                holder.mLinearLayoutFCP.setVisibility(View.GONE);
                holder.mDdishWeightLayout.setVisibility(View.VISIBLE);
            }
        }

        final int dragState = holder.getDragStateFlags();
        final int swipeState = holder.getSwipeStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0) ||
                ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.getForeground());
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_swiping_active_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_item_swiping_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }

        // set swiping properties
        holder.setSwipeItemHorizontalSlideAmount(
                item.isPinned() ? Swipeable.OUTSIDE_OF_THE_WINDOW_LEFT : 0);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(DayTimeViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // check the item is *not* pinned
        if (mProvider.getGroupItem(groupPosition).isPinned()) {
            // return false to raise View.OnClickListener#onClick() event
            return false;
        }

        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return !ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public boolean onCheckGroupCanStartDrag(DayTimeViewHolder holder, int groupPosition, int x, int y) {
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return  false;//ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public boolean onCheckChildCanStartDrag(TodayDishItemViewHolder holder, int groupPosition, int childPosition, int x, int y) {
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetGroupItemDraggableRange(DayTimeViewHolder holder, int groupPosition) {
        // no drag-sortable range specified
        return null;
    }

    @Override
    public ItemDraggableRange onGetChildItemDraggableRange(TodayDishItemViewHolder holder, int groupPosition, int childPosition) {
        // no drag-sortable range specified
        return null;
    }

    @Override
    public boolean onCheckGroupCanDrop(int draggingGroupPosition, int dropGroupPosition) {
        return true;
    }

    @Override
    public boolean onCheckChildCanDrop(int draggingGroupPosition, int draggingChildPosition, int dropGroupPosition, int dropChildPosition) {
        return true;
    }

    @Override
    public void onMoveGroupItem(int fromGroupPosition, int toGroupPosition) {
        mProvider.moveGroupItem(fromGroupPosition, toGroupPosition);
    }

    @Override
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        mProvider.moveChildItem(fromGroupPosition, fromChildPosition, toGroupPosition, toChildPosition);
    }

    @Override
    public int onGetGroupItemSwipeReactionType(DayTimeViewHolder holder, int groupPosition, int x, int y) {
        if (onCheckGroupCanStartDrag(holder, groupPosition, x, y)) {
            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
        }

        return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public int onGetChildItemSwipeReactionType(TodayDishItemViewHolder holder, int groupPosition, int childPosition, int x, int y) {
        if (onCheckChildCanStartDrag(holder, groupPosition, childPosition, x, y)) {
            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
        }

        return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public void onSetGroupItemSwipeBackground(DayTimeViewHolder holder, int groupPosition, int type) {
        int bgResId = 0;
        switch (type) {
            case Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_neutral;
                break;
            case Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_group_item_left;
                break;
            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_group_item_right;
                break;
        }

        holder.itemView.setBackgroundResource(bgResId);
    }

    @Override
    public void onSetChildItemSwipeBackground(TodayDishItemViewHolder holder, int groupPosition, int childPosition, int type) {
        int bgResId = 0;
        switch (type) {
            case Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_neutral;
                break;
            case Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_left;
                break;
            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_right;
                break;
        }

        holder.itemView.setBackgroundResource(bgResId);
    }

    @Override
    public SwipeResultAction onSwipeGroupItem(DayTimeViewHolder holder, int groupPosition, int result) {
        Log.d(TAG, "onSwipeGroupItem(groupPosition = " + groupPosition + ", result = " + result + ")");

        switch (result) {
            // swipe right
            case Swipeable.RESULT_SWIPED_RIGHT:
                if (mProvider.getGroupItem(groupPosition).isPinned()) {
                    // pinned --- back to default position
                    return new GroupUnpinResultAction(this, groupPosition);
                } else {
                    // not pinned --- remove
                    return new GroupSwipeRightResultAction(this, groupPosition);
                }
                // swipe left -- pin
            case Swipeable.RESULT_SWIPED_LEFT:
                return new GroupSwipeLeftResultAction(this, groupPosition);
            // other --- do nothing
            case Swipeable.RESULT_CANCELED:
            default:
                if (groupPosition != RecyclerView.NO_POSITION) {
                    return new GroupUnpinResultAction(this, groupPosition);
                } else {
                    return null;
                }
        }
    }

    @Override
    public SwipeResultAction onSwipeChildItem(TodayDishItemViewHolder holder, int groupPosition, int childPosition, int result) {
        Log.d(TAG, "onSwipeChildItem(groupPosition = " + groupPosition + ", childPosition = " + childPosition + ", result = " + result + ")");

        switch (result) {
            // swipe right
            case Swipeable.RESULT_SWIPED_RIGHT:
                if (mProvider.getChildItem(groupPosition, childPosition).isPinned()) {
                    // pinned --- back to default position
                    return new ChildUnpinResultAction(this, groupPosition, childPosition);
                } else {
                    // not pinned --- remove
                    return new ChildSwipeRightResultAction(this, groupPosition, childPosition);
                }
                // swipe left -- pin
            case Swipeable.RESULT_SWIPED_LEFT:
                return new ChildSwipeLeftResultAction(this, groupPosition, childPosition);
            // other --- do nothing
            case Swipeable.RESULT_CANCELED:
            default:
                if (groupPosition != RecyclerView.NO_POSITION) {
                    return new ChildUnpinResultAction(this, groupPosition, childPosition);
                } else {
                    return null;
                }
        }
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    private static class GroupSwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private ExpandableDraggableSwipeableExampleAdapter mAdapter;
        private final int mGroupPosition;
        private boolean mSetPinned;

        GroupSwipeLeftResultAction(ExpandableDraggableSwipeableExampleAdapter adapter, int groupPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            AbstractExpandableDataProvider.GroupData item =
                    mAdapter.mProvider.getGroupItem(mGroupPosition);

            if (!item.isPinned()) {
                item.setPinned(true);
                mAdapter.mExpandableItemManager.notifyGroupItemChanged(mGroupPosition);
                mSetPinned = true;
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mSetPinned && mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onGroupItemPinned(mGroupPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class GroupSwipeRightResultAction extends SwipeResultActionRemoveItem {
        private ExpandableDraggableSwipeableExampleAdapter mAdapter;
        private final int mGroupPosition;

        GroupSwipeRightResultAction(ExpandableDraggableSwipeableExampleAdapter adapter, int groupPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            mAdapter.mProvider.removeGroupItem(mGroupPosition);
            mAdapter.mExpandableItemManager.notifyGroupItemRemoved(mGroupPosition);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onGroupItemRemoved(mGroupPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class GroupUnpinResultAction extends SwipeResultActionDefault {
        private ExpandableDraggableSwipeableExampleAdapter mAdapter;
        private final int mGroupPosition;

        GroupUnpinResultAction(ExpandableDraggableSwipeableExampleAdapter adapter, int groupPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            AbstractExpandableDataProvider.GroupData item = mAdapter.mProvider.getGroupItem(mGroupPosition);
            if (item.isPinned()) {
                item.setPinned(false);
                mAdapter.mExpandableItemManager.notifyGroupItemChanged(mGroupPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }


    private static class ChildSwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private ExpandableDraggableSwipeableExampleAdapter mAdapter;
        private final int mGroupPosition;
        private final int mChildPosition;
        private boolean mSetPinned;

        ChildSwipeLeftResultAction(ExpandableDraggableSwipeableExampleAdapter adapter, int groupPosition, int childPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            AbstractExpandableDataProvider.ChildData item =
                    mAdapter.mProvider.getChildItem(mGroupPosition, mChildPosition);

            if (!item.isPinned()) {
                item.setPinned(true);
                mAdapter.mExpandableItemManager.notifyChildItemChanged(mGroupPosition, mChildPosition);
                mSetPinned = true;
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mSetPinned && mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onChildItemPinned(mGroupPosition, mChildPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class ChildSwipeRightResultAction extends SwipeResultActionRemoveItem {
        private ExpandableDraggableSwipeableExampleAdapter mAdapter;
        private final int mGroupPosition;
        private final int mChildPosition;

        ChildSwipeRightResultAction(ExpandableDraggableSwipeableExampleAdapter adapter, int groupPosition, int childPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            mAdapter.mProvider.removeChildItem(mGroupPosition, mChildPosition);
            mAdapter.mExpandableItemManager.notifyChildItemRemoved(mGroupPosition, mChildPosition);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onChildItemRemoved(mGroupPosition, mChildPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class ChildUnpinResultAction extends SwipeResultActionDefault {
        private ExpandableDraggableSwipeableExampleAdapter mAdapter;
        private final int mGroupPosition;
        private final int mChildPosition;

        ChildUnpinResultAction(ExpandableDraggableSwipeableExampleAdapter adapter, int groupPosition, int childPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

            AbstractExpandableDataProvider.ChildData item = mAdapter.mProvider.getChildItem(mGroupPosition, mChildPosition);
            if (item.isPinned()) {
                item.setPinned(false);
                mAdapter.mExpandableItemManager.notifyChildItemChanged(mGroupPosition, mChildPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }
}

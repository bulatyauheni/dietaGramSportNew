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

package bulat.diet.helper_sport.common.data;


import android.content.Context;
import android.support.v4.util.Pair;
import bulat.diet.helper_sport.db.NotificationDishHelper;
import bulat.diet.helper_sport.db.TodayDishHelper;
import bulat.diet.helper_sport.item.NotificationDish;
import bulat.diet.helper_sport.item.TodayDish;
import bulat.diet.helper_sport.utils.SaveUtils;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ExampleExpandableDataProvider extends AbstractExpandableDataProvider {
    public List<Pair<DayTimeGroupData, List<DishItemData>>> getData() {
        return mData;
    }

    public void setData(List<Pair<DayTimeGroupData, List<DishItemData>>> mData) {
        this.mData = mData;
    }

    private List<Pair<DayTimeGroupData, List<DishItemData>>> mData;

    // for undo group item
    private Pair<DayTimeGroupData, List<DishItemData>> mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    // for undo child item
    private DishItemData mLastRemovedChild;
    private long mLastRemovedChildParentGroupId = -1;
    private int mLastRemovedChildPosition = -1;

    private Context mContext;
	private String curentDateandTime;

    public ExampleExpandableDataProvider(Context context, String date) {
        mContext = context;
        mData = new LinkedList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMMM", new Locale(
				SaveUtils.getLang(context)));
        if (date == null) {
			curentDateandTime = sdf.format(new Date());
		} else {
			curentDateandTime = date;
		}
        ArrayList<TodayDish> baseData = TodayDishHelper.getArrayDishesByDate(context, curentDateandTime);
        initDayTymes(baseData);       
    }

    private void initDayTymes(ArrayList<TodayDish> baseData) {
    	ArrayList<NotificationDish> nots = NotificationDishHelper.getEnabledNotificationsList(mContext);			
		int i = 0;
		for (NotificationDish notif : nots) {	
            //noinspection UnnecessaryLocalVariable
            final long groupId = i;
            final String groupText = notif.getName();
            final DayTimeGroupData group = new DayTimeGroupData(groupId, groupText);
            final List<DishItemData> children = new ArrayList<>();
            float tempFat = 0;
    		float tempCarbon = 0;
    		float tempProtein = 0;
    		int tempCalory = 0;
            for (TodayDish dish:baseData) {
                final long childId = Long.parseLong(dish.getId());
                final String childText = dish.getName();
                if (dish.getDayTyme().equals(notif.getId())){
                	children.add(new DishItemData(childId, childText, dish));
                	tempCarbon = tempCarbon + dish.getCarbon();
                	tempFat = tempFat + dish.getFat();
                	tempProtein = tempProtein + dish.getProtein();
                	tempCalory = tempCalory + dish.getCaloricity();
                }
            }
            group.setFat(tempFat);
            group.setCarbon(tempCarbon);
            group.setProtein(tempProtein);
            group.setCalory(tempCalory);           
            mData.add(new Pair<DayTimeGroupData, List<DishItemData>>(group, children));
            i++;
        }

	}

	@Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).second.size();
    }

    @Override
    public DayTimeGroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        return mData.get(groupPosition).first;
    }

    @Override
    public ChildData getChildItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        final List<DishItemData> children = mData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children.get(childPosition);
    }

    @Override
    public void moveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }

        final Pair<DayTimeGroupData, List<DishItemData>> item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    @Override
    public void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }

        final Pair<DayTimeGroupData, List<DishItemData>> fromGroup = mData.get(fromGroupPosition);
        final Pair<DayTimeGroupData, List<DishItemData>> toGroup = mData.get(toGroupPosition);

        final DishItemData item = (DishItemData) fromGroup.second.remove(fromChildPosition);

        if (toGroupPosition != fromGroupPosition) {
            // assign a new ID
            final long newId = ((DayTimeGroupData) toGroup.first).generateNewChildId();
            item.setChildId(newId);
        }

        toGroup.second.add(toChildPosition, item);
    }

    @Override
    public void removeGroupItem(int groupPosition) {
        mLastRemovedGroup = mData.remove(groupPosition);
        mLastRemovedGroupPosition = groupPosition;

        mLastRemovedChild = null;
        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
    }

    @Override
    public void removeChildItem(int groupPosition, int childPosition) {
        mLastRemovedChild = mData.get(groupPosition).second.remove(childPosition);
        mLastRemovedChildParentGroupId = mData.get(groupPosition).first.getGroupId();
        mLastRemovedChildPosition = childPosition;

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;
    }


    @Override
    public long undoLastRemoval() {
        if (mLastRemovedGroup != null) {
            return undoGroupRemoval();
        } else if (mLastRemovedChild != null) {
            return undoChildRemoval();
        } else {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }
    }

    private long undoGroupRemoval() {
        int insertedPosition;
        if (mLastRemovedGroupPosition >= 0 && mLastRemovedGroupPosition < mData.size()) {
            insertedPosition = mLastRemovedGroupPosition;
        } else {
            insertedPosition = mData.size();
        }

        mData.add(insertedPosition, mLastRemovedGroup);

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;

        return RecyclerViewExpandableItemManager.getPackedPositionForGroup(insertedPosition);
    }

    private long undoChildRemoval() {
        Pair<DayTimeGroupData, List<DishItemData>> group = null;
        int groupPosition = -1;

        // find the group
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).first.getGroupId() == mLastRemovedChildParentGroupId) {
                group = mData.get(i);
                groupPosition = i;
                break;
            }
        }

        if (group == null) {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }

        int insertedPosition;
        if (mLastRemovedChildPosition >= 0 && mLastRemovedChildPosition < group.second.size()) {
            insertedPosition = mLastRemovedChildPosition;
        } else {
            insertedPosition = group.second.size();
        }

        group.second.add(insertedPosition, mLastRemovedChild);

        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
        mLastRemovedChild = null;

        return RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, insertedPosition);
    }

    public static final class DayTimeGroupData extends GroupData {

        private final long mId;
        private final String mText;
        private boolean mPinned;
        private long mNextChildId;

        private float carbon;
        private float fat;
        private float protein;
        private int calory;

        private int weight;

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getCalory() {
			return calory;
		}

		public void setCalory(int calory) {
			this.calory = calory;
		}

		public DayTimeGroupData(long id, String text) {
            mId = id;
            mText = text;
            mNextChildId = 0;
        }

        @Override
        public long getGroupId() {
            return mId;
        }

        @Override
        public boolean isSectionHeader() {
            return false;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinnedToSwipeLeft) {
            mPinned = pinnedToSwipeLeft;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public long generateNewChildId() {
            final long id = mNextChildId;
            mNextChildId += 1;
            return id;
        }

        public float getCarbon() {
			return carbon;
		}

		public void setCarbon(float carbon) {
			this.carbon = carbon;
		}

		public float getFat() {
			return fat;
		}

		public void setFat(float fat) {
			this.fat = fat;
		}

		public float getProtein() {
			return protein;
		}

		public void setProtein(float protein) {
			this.protein = protein;
		}
    }

    public static final class DishItemData extends ChildData {

        private long mId;
        private String mText;
        private boolean mPinned;



        private TodayDish mDishInfo;

        public DishItemData(long id, String text, TodayDish dish) {
            mId = id;
            mText = text;
            mDishInfo = dish;
        }

        @Override
        public long getChildId() {
            return mId;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public void setChildId(long id) {
            this.mId = id;
        }

        public TodayDish getDishInfo() {
            return mDishInfo;
        }

        public void setDishInfo(TodayDish dishInfo) {
            this.mDishInfo = dishInfo;
        }

    }
}

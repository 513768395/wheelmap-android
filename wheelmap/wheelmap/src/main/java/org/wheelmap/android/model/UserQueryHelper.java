/*
 * #%L
 * Wheelmap - App
 * %%
 * Copyright (C) 2011 - 2012 Michal Harakal - Michael Kroez - Sozialhelden e.V.
 * %%
 * Wheelmap App based on the Wheelmap Service by Sozialhelden e.V.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS-IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.wheelmap.android.model;

import org.holoeverywhere.widget.Toast;
import org.wheelmap.android.manager.SupportManager;
import org.wheelmap.android.manager.SupportManager.WheelchairAttributes;
import org.wheelmap.android.model.Support.CategoriesContent;
import org.wheelmap.android.model.Wheelmap.POIs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.akquinet.android.androlog.Log;
import de.greenrobot.event.EventBus;

public class UserQueryHelper {

    private static final String TAG = UserQueryHelper.class.getSimpleName();

    public static UserQueryHelper INSTANCE;

    private Context mContext;

    private String mCategoriesQuery;

    private String mWheelchairQuery;

    private static String mQuery;

    private EventBus mBus;


    public static class UserQueryUpdateEvent {

        public final String query;

        public UserQueryUpdateEvent(String query) {
            this.query = query;
        }
    }

    private UserQueryHelper(Context context) {
        mContext = context;
        mBus = EventBus.getDefault();

        initCategoriesQuery();
        initWheelstateQuery();
        update(true);
    }

    public static void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UserQueryHelper(context);
        }
    }

    private void update(boolean post) {
        Log.d(TAG, "update query = " + mQuery);
        mQuery = concatenateWhere(mCategoriesQuery, mWheelchairQuery);
        Log.d(TAG, "update: posting UserQueryUpdateEvent on bus");
        mBus.postSticky(new UserQueryUpdateEvent(mQuery));
    }

    public static String getUserQuery() {
        return mQuery;
    }

    private OnSharedPreferenceChangeListener prefsListener
            = new OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs,
                String key) {
            boolean oneKeyFound = false;
            Map<WheelchairState, WheelchairAttributes> wsAttributes = SupportManager.wsAttributes;
            for (Map.Entry<WheelchairState, WheelchairAttributes> item : wsAttributes
                    .entrySet()) {
                if (item.getValue().prefsKey.equals(key)) {
                    oneKeyFound = true;
                }
            }

            if (oneKeyFound == false) {
                return;
            }

            calcWheelchairStateQuery(prefs);
            update(true);
        }
    };

    private void initWheelstateQuery() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        prefs.registerOnSharedPreferenceChangeListener(prefsListener);
        calcWheelchairStateQuery(prefs);
    }

    private List<WheelchairState> getWheelchairStateFromPrefs(final SharedPreferences prefs) {
        ArrayList<WheelchairState> list = new ArrayList<WheelchairState>();

        Map<WheelchairState, WheelchairAttributes> wsAttributes = SupportManager.wsAttributes;
        for (Map.Entry<WheelchairState, WheelchairAttributes> entry : wsAttributes
                .entrySet()) {
            boolean prefState = prefs.getBoolean(entry.getValue().prefsKey, true);
            if (prefState) {
                list.add(entry.getKey());
            }
        }

        return list;
    }

    private void calcWheelchairStateQuery(final SharedPreferences prefs) {
        Log.d(TAG, "calcWheelchairStateQuery starting");

        List<WheelchairState> list = getWheelchairStateFromPrefs(prefs);

        StringBuilder wheelchair = new StringBuilder("");

        for (WheelchairState state : list) {
            if (wheelchair.length() > 0) {
                wheelchair.append(" OR wheelchair=");
            } else {
                wheelchair.append(" wheelchair=");
            }
            wheelchair.append(state.getId());
        }

        if (wheelchair.toString().length() == 0) {
            for (WheelchairState state : WheelchairState.values()) {
                if (wheelchair.length() > 0) {
                    wheelchair.append(" AND NOT wheelchair=");
                } else {
                    wheelchair.append(" NOT wheelchair=");
                }
                wheelchair.append(state.getId());
            }
        }

        Log.d(TAG, "query result = " + wheelchair.toString());
        mWheelchairQuery = wheelchair.toString();
    }

    private void initCategoriesQuery() {
        Log.d(TAG, "initCategoriesQuery: starting");
        calcCategoriesQuery();
        mContext.getContentResolver().registerContentObserver(
                CategoriesContent.CONTENT_URI, false, categoriesObserver);

    }

    private ContentObserver categoriesObserver = new ContentObserver(
            new Handler()) {

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.d(TAG, "categoriesObserver onChanged fired");
            calcCategoriesQuery();
            update(true);
        }

    };

    private void calcCategoriesQuery() {

        Cursor cursor = mContext.getContentResolver().query(
                CategoriesContent.CONTENT_URI, null, null, null, null);
        if (cursor == null) {
            return;
        }

        StringBuilder categories = new StringBuilder("");

        int selectedCount = 0;
        if (cursor.moveToFirst()) {
            do {
                int id = CategoriesContent.getCategoryId(cursor);
                if (CategoriesContent.getSelected(cursor)) {
                    selectedCount++;
                    if (categories.length() > 0) {
                        categories.append(" OR ").append(POIs.CATEGORY_ID)
                                .append("=");
                    } else {
                        categories.append(POIs.CATEGORY_ID).append("=");
                    }
                    categories.append(Integer.valueOf(id));

                }

            } while (cursor.moveToNext());
        }
        if (selectedCount == 0) {
            if (cursor.moveToFirst()) {
                do {
                    int id = CategoriesContent.getCategoryId(cursor);
                    if (categories.length() > 0) {
                        categories.append(" AND NOT category_id=");
                    } else {
                        categories.append(" NOT category_id=");
                    }

                    categories.append(Integer.valueOf(id));

                } while (cursor.moveToNext());
            }

        }

        cursor.close();
        Log.d(TAG, "calcCategoriesQuery: result = " + categories.toString());
        mCategoriesQuery = categories.toString();
    }

    private static String concatenateWhere(String a, String b) {
        if (TextUtils.isEmpty(a)) {
            return b;
        }
        if (TextUtils.isEmpty(b)) {
            return a;
        }

        return "(" + a + ") AND (" + b + ")";
    }
}

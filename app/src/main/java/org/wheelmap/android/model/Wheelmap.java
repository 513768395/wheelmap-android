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

import org.wheelmap.android.online.R;

import android.content.ContentResolver;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.provider.BaseColumns;

public class Wheelmap {

    public static String AUTHORITY;


    // This class cannot be instantiated
    private Wheelmap() {
    }

    /**
     * Columns from the Places table that other columns join into themselves.
     */
    public static interface POIsColumns {

        /**
         * The name of the place <p/> Type: TEXT </P>
         */
        public static final String WM_ID = "wm_id";
        public static final String NAME = "name";

        public static final String CATEGORY_ID = "category_id";
        public static final String CATEGORY_IDENTIFIER = "category_identifier";
        public static final String NODETYPE_ID = "nodetype_id";
        public static final String NODETYPE_IDENTIFIER = "nodetype_identifier";
        public static final String ICON = "icon";

        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";

        public static final String STREET = "street";
        public static final String HOUSE_NUM = "house_num";
        public static final String POSTCODE = "postcode";
        public static final String CITY = "city";

        public static final String PHONE = "phone";
        public static final String WEBSITE = "website";

        public static final String WHEELCHAIR = "wheelchair";
        public static final String WHEELCHAIR_TOILET = "wheelchair_toilet";
        public static final String DESCRIPTION = "description";

        // auxiliry pre calculated sin and cos values of lat/lon (in radians)
        public static final String SIN_LAT_RAD = "sin_lat_rad";
        public static final String COS_LAT_RAD = "cos_lat_rad";
        public static final String SIN_LON_RAD = "sin_lon_rad";
        public static final String COS_LON_RAD = "cos_lon_rad";

        public static final String DISTANCE_ACOS = "distance_acos";

        public static final String TAG = "tag";
        public static final int TAG_RETRIEVED = 0x0;
        public static final int TAG_COPY = 0x1;
        public static final int TAG_TMP = 0x2;

        public static final String STATE = "state";
        public static final int STATE_UNCHANGED = 0x0;
        public static final int STATE_CHANGED = 0x1;

        public static final String DIRTY = "dirty";
        public static final int CLEAN = 0x0;
        public static final int DIRTY_STATE = 0x1;
        public static final int DIRTY_ALL = 0x2;

        public static final String STORE_TIMESTAMP = "store_timestamp";

        public static final String PHOTO_ID = "id";
        public static final String TAKEN_ON = "taken_on";
        public static final String TYPE = "type";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
        public static final String URL = "url";

    }

    public static final class POIs implements BaseColumns, POIsColumns {

        static Uri CONTENT_URI_BASE;

        public static Uri CONTENT_URI_ALL;

        public static Uri CONTENT_URI_RETRIEVED;

        public static Uri CONTENT_URI_COPY;

        public static Uri CONTENT_URI_TMP;


        /**
         * The content:// style URL for this table
         */
        static final String PATH_ALL = "all";

        static final String PATH_RETRIEVED = "retrieved";

        static final String PATH_COPY = "copy";

        static final String PATH_TMP = "tmp";

        // This class cannot be instantiated
        private POIs() {
        }

        public static void init(Context context) {
            AUTHORITY = context.getString(R.string.wheelmapprovider);

            CONTENT_URI_BASE = Uri
                    .parse(ContentResolver.SCHEME_CONTENT + "://" + AUTHORITY);
            CONTENT_URI_ALL = CONTENT_URI_BASE.buildUpon()
                    .appendPath(PATH_ALL).build();
            CONTENT_URI_RETRIEVED = CONTENT_URI_BASE
                    .buildUpon().appendPath(PATH_RETRIEVED).build();
            CONTENT_URI_COPY = CONTENT_URI_BASE.buildUpon()
                    .appendPath(PATH_COPY).build();
            CONTENT_URI_TMP = CONTENT_URI_BASE.buildUpon()
                    .appendPath(PATH_TMP).build();
        }

        public static final String PARAMETER_LONGITUDE = "longitude";

        public static final String PARAMETER_LATITUDE = "latitude";

        public static final String PARAMETER_SORTED = "sorted";

        public static final String PARAMETER_NONOTIFY = "no_notify";

        public static Uri createUriSorted(final Location location) {
            Uri.Builder builder = CONTENT_URI_RETRIEVED.buildUpon();
            if (location != null) {
                builder.appendQueryParameter(PARAMETER_LATITUDE,
                        Double.toString(location.getLatitude()))
                        .appendQueryParameter(PARAMETER_LONGITUDE,
                                Double.toString(location.getLongitude()))
                        .appendQueryParameter(PARAMETER_SORTED,
                                String.valueOf(true));
            }
            return builder.build();
        }

        public static Uri createNoNotify(final Uri uri) {
            return uri.buildUpon()
                    .appendQueryParameter(PARAMETER_NONOTIFY, "true").build();
        }

        /**
         * The MIME type providing a directory of notes.
         */
        public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.wheelmap.pois";

        /**
         * The MIME type sub-directory of a single note.
         */
        public static final String CONTENT_TYPE_ITEM
                = "vnd.android.cursor.item/vnd.wheelmap.poi_id";

        /**
         * The default sort order for this table - categories
         */
        public static final String DEFAULT_SORT_ORDER = NAME + " DESC";

        public static final String POI_ID = _ID;

        /**
         * The columns we are interested in from the database
         */
        public static final String[] PROJECTION = new String[]{_ID, WM_ID,
                NAME, LATITUDE, LONGITUDE, STREET, HOUSE_NUM, POSTCODE, CITY,
                PHONE, WEBSITE, WHEELCHAIR, WHEELCHAIR_TOILET, DESCRIPTION, CATEGORY_ID, CATEGORY_IDENTIFIER,          /* IMPORTANT */
                NODETYPE_ID, NODETYPE_IDENTIFIER, ICON, TAG, STATE, DIRTY, STORE_TIMESTAMP};
    }
}

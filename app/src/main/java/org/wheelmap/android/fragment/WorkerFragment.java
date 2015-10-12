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
package org.wheelmap.android.fragment;

import android.database.Cursor;
import android.os.Bundle;

public interface WorkerFragment {

    public static final int LIST_CURSOR = 0;
    public static final int MAP_CURSOR = 1;

    public void registerDisplayFragment(DisplayFragment fragment);

    public void unregisterDisplayFragment(DisplayFragment fragment);

    public void requestUpdate(Bundle bundle);

    public void requestSearch(Bundle bundle);

    public Cursor getCursor(int id);

    public boolean isRefreshing();

    public boolean isSearchMode();

    public void setSearchMode(boolean isSearchMode);

}

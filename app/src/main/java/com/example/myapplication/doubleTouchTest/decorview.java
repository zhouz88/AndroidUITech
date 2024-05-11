package com.example.myapplication.doubleTouchTest;

//        setcontentview 会先installdecor 最终布局如下
//        int layoutResource;
//                int features = getLocalFeatures();
//                // System.out.println("Features: 0x" + Integer.toHexString(features));
//                if ((features & ((1 << FEATURE_LEFT_ICON) | (1 << FEATURE_RIGHT_ICON))) != 0) {
//                if (mIsFloating) {
//                TypedValue res = new TypedValue();
//                getContext().getTheme().resolveAttribute(
//                R.attr.dialogTitleIconsDecorLayout, res, true);
//                layoutResource = res.resourceId;
//                } else {
//                layoutResource = R.layout.screen_title_icons;
//                }
//                // XXX Remove this once action bar supports these features.
//                removeFeature(FEATURE_ACTION_BAR);
//                // System.out.println("Title Icons!");
//                } else if ((features & ((1 << FEATURE_PROGRESS) | (1 << FEATURE_INDETERMINATE_PROGRESS))) != 0
//                && (features & (1 << FEATURE_ACTION_BAR)) == 0) {
//                // Special case for a window with only a progress bar (and title).
//                // XXX Need to have a no-title version of embedded windows.
//                layoutResource = R.layout.screen_progress;
//                // System.out.println("Progress!");
//                } else if ((features & (1 << FEATURE_CUSTOM_TITLE)) != 0) {
//                // Special case for a window with a custom title.
//                // If the window is floating, we need a dialog layout
//                if (mIsFloating) {
//                TypedValue res = new TypedValue();
//                getContext().getTheme().resolveAttribute(
//                R.attr.dialogCustomTitleDecorLayout, res, true);
//                layoutResource = res.resourceId;
//                } else {
//                layoutResource = R.layout.screen_custom_title;
//                }
//                // XXX Remove this once action bar supports these features.
//                removeFeature(FEATURE_ACTION_BAR);
//                } else if ((features & (1 << FEATURE_NO_TITLE)) == 0) {
//                // If no other features and not embedded, only need a title.
//                // If the window is floating, we need a dialog layout
//                if (mIsFloating) {
//                TypedValue res = new TypedValue();
//                getContext().getTheme().resolveAttribute(
//                R.attr.dialogTitleDecorLayout, res, true);
//                layoutResource = res.resourceId;
//                } else if ((features & (1 << FEATURE_ACTION_BAR)) != 0) {
//                layoutResource = a.getResourceId(
//                R.styleable.Window_windowActionBarFullscreenDecorLayout,
//                R.layout.screen_action_bar);
//                } else {
//                layoutResource = R.layout.screen_title;
//                }
//                // System.out.println("Title!");
//                } else if ((features & (1 << FEATURE_ACTION_MODE_OVERLAY)) != 0) {
//                layoutResource = R.layout.screen_simple_overlay_action_mode;
//                } else {
//                // Embedded, so no decoration is needed.
//                layoutResource = R.layout.screen_simple;
//                // System.out.println("Simple!");
//                }

//<?xml version="1.0" encoding="utf-8"?>
//<!--
//        /* //device/apps/common/assets/res/layout/screen_full.xml
//         **
//         ** Copyright 2006, The Android Open Source Project
//         **
//         ** Licensed under the Apache License, Version 2.0 (the "License");
//         ** you may not use this file except in compliance with the License.
//         ** You may obtain a copy of the License at
//         **
//         **     http://www.apache.org/licenses/LICENSE-2.0
//         **
//         ** Unless required by applicable law or agreed to in writing, software
//         ** distributed under the License is distributed on an "AS IS" BASIS,
//         ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//         ** See the License for the specific language governing permissions and
//         ** limitations under the License.
//         */
//
//        This is the basic layout for a screen, with all of its features enabled.
//        -->
//
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        android:fitsSystemWindows="true"
//        android:orientation="vertical"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        >
//<!-- Popout bar for action modes -->
//<ViewStub android:id="@+id/action_mode_bar_stub"
//        android:inflatedId="@+id/action_mode_bar"
//        android:layout="@layout/action_mode_bar"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:theme="?attr/actionBarTheme" />
//
//<RelativeLayout android:id="@android:id/title_container"
//        style="?android:attr/windowTitleBackgroundStyle"
//        android:layout_width="match_parent"
//        android:layout_height="?android:attr/windowTitleSize"
//        >
//<ProgressBar android:id="@+android:id/progress_circular"
//        style="?android:attr/progressBarStyleSmallTitle"
//        android:layout_width="wrap_content"
//        android:layout_height="wrap_content"
//        android:layout_marginStart="5dip"
//        android:layout_alignParentEnd="true"
//        android:layout_centerVertical="true"
//        android:visibility="gone"
//        android:max="10000"
//        />
//<ProgressBar android:id="@+android:id/progress_horizontal"
//        style="?android:attr/progressBarStyleHorizontal"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:layout_marginStart="2dip"
//        android:layout_alignParentStart="true"
//        android:layout_toStartOf="@android:id/progress_circular"
//        android:layout_centerVertical="true"
//        android:visibility="gone"
//        android:max="10000"
//        />
//<TextView android:id="@android:id/title"
//        style="?android:attr/windowTitleStyle"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        android:layout_alignParentStart="true"
//        android:layout_toStartOf="@android:id/progress_circular"
//        android:background="@null"
//        android:fadingEdge="horizontal"
//        android:gravity="center_vertical"
//        android:scrollHorizontally="true"
//        />
//</RelativeLayout>
//<FrameLayout android:id="@android:id/content"
//        android:layout_width="match_parent"
//        android:layout_height="0dip"
//        android:layout_weight="1"
//        android:foregroundGravity="fill_horizontal|top"
//        android:foreground="?android:attr/windowContentOverlay"
//        />
//</LinearLayout>

//screen_prgoress
//<?xml version="1.0" encoding="utf-8"?>
//<!--
//        /* //device/apps/common/assets/res/layout/screen_full.xml
//         **
//         ** Copyright 2006, The Android Open Source Project
//         **
//         ** Licensed under the Apache License, Version 2.0 (the "License");
//         ** you may not use this file except in compliance with the License.
//         ** You may obtain a copy of the License at
//         **
//         **     http://www.apache.org/licenses/LICENSE-2.0
//         **
//         ** Unless required by applicable law or agreed to in writing, software
//         ** distributed under the License is distributed on an "AS IS" BASIS,
//         ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//         ** See the License for the specific language governing permissions and
//         ** limitations under the License.
//         */
//
//        This is the basic layout for a screen, with all of its features enabled.
//        -->
//
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        android:fitsSystemWindows="true"
//        android:orientation="vertical"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        >
//<!-- Popout bar for action modes -->
//<ViewStub android:id="@+id/action_mode_bar_stub"
//        android:inflatedId="@+id/action_mode_bar"
//        android:layout="@layout/action_mode_bar"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:theme="?attr/actionBarTheme" />
//
//<RelativeLayout android:id="@android:id/title_container"
//        style="?android:attr/windowTitleBackgroundStyle"
//        android:layout_width="match_parent"
//        android:layout_height="?android:attr/windowTitleSize"
//        >
//<ProgressBar android:id="@+android:id/progress_circular"
//        style="?android:attr/progressBarStyleSmallTitle"
//        android:layout_width="wrap_content"
//        android:layout_height="wrap_content"
//        android:layout_marginStart="5dip"
//        android:layout_alignParentEnd="true"
//        android:layout_centerVertical="true"
//        android:visibility="gone"
//        android:max="10000"
//        />
//<ProgressBar android:id="@+android:id/progress_horizontal"
//        style="?android:attr/progressBarStyleHorizontal"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:layout_marginStart="2dip"
//        android:layout_alignParentStart="true"
//        android:layout_toStartOf="@android:id/progress_circular"
//        android:layout_centerVertical="true"
//        android:visibility="gone"
//        android:max="10000"
//        />
//<TextView android:id="@android:id/title"
//        style="?android:attr/windowTitleStyle"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        android:layout_alignParentStart="true"
//        android:layout_toStartOf="@android:id/progress_circular"
//        android:background="@null"
//        android:fadingEdge="horizontal"
//        android:gravity="center_vertical"
//        android:scrollHorizontally="true"
//        />
//</RelativeLayout>
//<FrameLayout android:id="@android:id/content"
//        android:layout_width="match_parent"
//        android:layout_height="0dip"
//        android:layout_weight="1"
//        android:foregroundGravity="fill_horizontal|top"
//        android:foreground="?android:attr/windowContentOverlay"
//        />
//</LinearLayout>

//feature_custom_title
//<?xml version="1.0" encoding="utf-8"?>
//<!-- Copyright (C) 2006 The Android Open Source Project
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.
//        -->
//
//<!--
//        This is a custom layout for a screen.
//        -->
//
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        android:orientation="vertical"
//        android:fitsSystemWindows="true">
//<!-- Popout bar for action modes -->
//<ViewStub android:id="@+id/action_mode_bar_stub"
//        android:inflatedId="@+id/action_mode_bar"
//        android:layout="@layout/action_mode_bar"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:theme="?attr/actionBarTheme" />
//
//<FrameLayout android:id="@android:id/title_container"
//        android:layout_width="match_parent"
//        android:layout_height="?android:attr/windowTitleSize"
//        android:transitionName="android:title"
//        style="?android:attr/windowTitleBackgroundStyle">
//</FrameLayout>
//<FrameLayout android:id="@android:id/content"
//        android:layout_width="match_parent"
//        android:layout_height="0dip"
//        android:layout_weight="1"
//        android:foregroundGravity="fill_horizontal|top"
//        android:foreground="?android:attr/windowContentOverlay" />
//</LinearLayout>

//screen_action_bar
//<?xml version="1.0" encoding="utf-8"?>
//<!-- Copyright (C) 2010 The Android Open Source Project
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.
//        -->
//
//<!--
//        This is an optimized layout for a screen with the Action Bar enabled.
//        -->
//
//<com.android.internal.widget.ActionBarOverlayLayout
//        xmlns:android="http://schemas.android.com/apk/res/android"
//        android:id="@+id/decor_content_parent"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        android:splitMotionEvents="false"
//        android:theme="?attr/actionBarTheme">
//<FrameLayout android:id="@android:id/content"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent" />
//<com.android.internal.widget.ActionBarContainer
//        android:id="@+id/action_bar_container"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:layout_alignParentTop="true"
//        style="?attr/actionBarStyle"
//        android:transitionName="android:action_bar"
//        android:touchscreenBlocksFocus="true"
//        android:keyboardNavigationCluster="true"
//        android:gravity="top">
//<com.android.internal.widget.ActionBarView
//        android:id="@+id/action_bar"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        style="?attr/actionBarStyle" />
//<com.android.internal.widget.ActionBarContextView
//        android:id="@+id/action_context_bar"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:visibility="gone"
//        style="?attr/actionModeStyle" />
//</com.android.internal.widget.ActionBarContainer>
//<com.android.internal.widget.ActionBarContainer android:id="@+id/split_action_bar"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        style="?attr/actionBarSplitStyle"
//        android:visibility="gone"
//        android:touchscreenBlocksFocus="true"
//        android:keyboardNavigationCluster="true"
//        android:gravity="center"/>
//</com.android.internal.widget.ActionBarOverlayLayout>

//screen_title
//<?xml version="1.0" encoding="utf-8"?>
//<!-- Copyright (C) 2006 The Android Open Source Project
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.
//        -->
//
//<!--
//        This is an optimized layout for a screen, with the minimum set of features
//        enabled.
//        -->
//
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        android:orientation="vertical"
//        android:fitsSystemWindows="true">
//<!-- Popout bar for action modes -->
//<ViewStub android:id="@+id/action_mode_bar_stub"
//        android:inflatedId="@+id/action_mode_bar"
//        android:layout="@layout/action_mode_bar"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:theme="?attr/actionBarTheme" />
//<FrameLayout
//        android:layout_width="match_parent"
//                android:layout_height="?android:attr/windowTitleSize"
//                style="?android:attr/windowTitleBackgroundStyle">
//<TextView android:id="@android:id/title"
//        style="?android:attr/windowTitleStyle"
//        android:background="@null"
//        android:fadingEdge="horizontal"
//        android:gravity="center_vertical"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent" />
//</FrameLayout>
//<FrameLayout android:id="@android:id/content"
//        android:layout_width="match_parent"
//        android:layout_height="0dip"
//        android:layout_weight="1"
//        android:foregroundGravity="fill_horizontal|top"
//        android:foreground="?android:attr/windowContentOverlay" />
//</LinearLayout>

// screen_simple
//<?xml version="1.0" encoding="utf-8"?>
//<!--
//        /* //device/apps/common/assets/res/layout/screen_simple.xml
//         **
//         ** Copyright 2006, The Android Open Source Project
//         **
//         ** Licensed under the Apache License, Version 2.0 (the "License");
//         ** you may not use this file except in compliance with the License.
//         ** You may obtain a copy of the License at
//         **
//         **     http://www.apache.org/licenses/LICENSE-2.0
//         **
//         ** Unless required by applicable law or agreed to in writing, software
//         ** distributed under the License is distributed on an "AS IS" BASIS,
//         ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//         ** See the License for the specific language governing permissions and
//         ** limitations under the License.
//         */
//
//        This is an optimized layout for a screen, with the minimum set of features
//        enabled.
//        -->
//
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        android:fitsSystemWindows="true"
//        android:orientation="vertical">
//<ViewStub android:id="@+id/action_mode_bar_stub"
//        android:inflatedId="@+id/action_mode_bar"
//        android:layout="@layout/action_mode_bar"
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:theme="?attr/actionBarTheme" />
//<FrameLayout
//         android:id="@android:id/content"
//                 android:layout_width="match_parent"
//                 android:layout_height="match_parent"
//                 android:foregroundInsidePadding="false"
//                 android:foregroundGravity="fill_horizontal|top"
//                 android:foreground="?android:attr/windowContentOverlay" />
//</LinearLayout>







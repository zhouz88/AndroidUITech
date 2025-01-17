/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myapplication.sourcecode;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentContainer;
import android.app.FragmentManagerImpl;
import android.app.LoaderManager;
import android.app.LoaderManagerImpl;
import android.compat.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Integration points with the Fragment host.
 * <p>
 * Fragments may be hosted by any object; such as an {@link android.app.Activity}. In order to
 * host fragments, implement {@link FragmentHostCallback}, overriding the methods
 * applicable to the host.
 *
 * @deprecated Use the <a href="{@docRoot}tools/extras/support-library.html">Support Library</a>
 *      {@link androidx.fragment.app.FragmentHostCallback}
 */
@Deprecated
public abstract class FragmentHostCallback<E> extends FragmentContainer {
    private final android.app.Activity mActivity;
    final Context mContext;
    private final Handler mHandler;
    final int mWindowAnimations;
    final FragmentManagerImpl mFragmentManager = new FragmentManagerImpl();
    /** The loader managers for individual fragments [i.e. Fragment#getLoaderManager()] */
    private ArrayMap<String, LoaderManager> mAllLoaderManagers;
    /** Whether or not fragment loaders should retain their state */
    private boolean mRetainLoaders;
    /** The loader manager for the fragment host [i.e. Activity#getLoaderManager()] */
    private LoaderManagerImpl mLoaderManager;
    private boolean mCheckedForLoaderManager;
    /** Whether or not the fragment host loader manager was started */
    @UnsupportedAppUsage
    private boolean mLoadersStarted;

    public FragmentHostCallback(Context context, Handler handler, int windowAnimations) {
        this((context instanceof android.app.Activity) ? (android.app.Activity)context : null, context,
                chooseHandler(context, handler), windowAnimations);
    }

    FragmentHostCallback(android.app.Activity activity) {
        this(activity, activity /*context*/, activity.mHandler, 0 /*windowAnimations*/);
    }

    FragmentHostCallback(android.app.Activity activity, Context context, Handler handler,
                         int windowAnimations) {
        mActivity = activity;
        mContext = context;
        mHandler = handler;
        mWindowAnimations = windowAnimations;
    }

    /**
     * Used internally in {@link #FragmentHostCallback(Context, Handler, int)} to choose
     * the Activity's handler or the provided handler.
     */
    private static Handler chooseHandler(Context context, Handler handler) {
        if (handler == null && context instanceof android.app.Activity) {
            android.app.Activity activity = (android.app.Activity) context;
            return activity.mHandler;
        } else {
            return handler;
        }
    }

    /**
     * Print internal state into the given stream.
     *
     * @param prefix Desired prefix to prepend at each line of output.
     * @param fd The raw file descriptor that the dump is being sent to.
     * @param writer The PrintWriter to which you should dump your state. This will be closed
     *                  for you after you return.
     * @param args additional arguments to the dump request.
     */
    public void onDump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
    }

    /**
     * Return {@code true} if the fragment's state needs to be saved.
     */
    public boolean onShouldSaveFragmentState(android.app.Fragment fragment) {
        return true;
    }

    /**
     * Return a {@link LayoutInflater}.
     * See {@link android.app.Activity#getLayoutInflater()}.
     */
    public LayoutInflater onGetLayoutInflater() {
        return (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Return {@code true} if the FragmentManager's LayoutInflaterFactory should be used.
     */
    public boolean onUseFragmentManagerInflaterFactory() {
        return false;
    }

    /**
     * Return the object that's currently hosting the fragment. If a {@link android.app.Fragment}
     * is hosted by a {@link android.app.Activity}, the object returned here should be the same
     * object returned from {@link android.app.Fragment#getActivity()}.
     */
    @Nullable
    public abstract E onGetHost();

    /**
     * Invalidates the activity's options menu.
     * See {@link android.app.Activity#invalidateOptionsMenu()}
     */
    public void onInvalidateOptionsMenu() {
    }

    /**
     * Starts a new {@link android.app.Activity} from the given fragment.
     * See {@link android.app.Activity#startActivityForResult(Intent, int)}.
     */
    public void onStartActivityFromFragment(android.app.Fragment fragment, Intent intent, int requestCode,
                                            Bundle options) {
        if (requestCode != -1) {
            throw new IllegalStateException(
                    "Starting activity with a requestCode requires a FragmentActivity host");
        }
        mContext.startActivity(intent);
    }

    /**
     * @hide
     * Starts a new {@link android.app.Activity} from the given fragment.
     * See {@link android.app.Activity#startActivityForResult(Intent, int)}.
     */
    public void onStartActivityAsUserFromFragment(android.app.Fragment fragment, Intent intent, int requestCode,
                                                  Bundle options, UserHandle userHandle) {
        if (requestCode != -1) {
            throw new IllegalStateException(
                    "Starting activity with a requestCode requires a FragmentActivity host");
        }
        mContext.startActivityAsUser(intent, userHandle);
    }

    /**
     * Starts a new {@link IntentSender} from the given fragment.
     * See {@link android.app.Activity#startIntentSender(IntentSender, Intent, int, int, int, Bundle)}.
     */
    public void onStartIntentSenderFromFragment(android.app.Fragment fragment, IntentSender intent,
                                                int requestCode, @Nullable Intent fillInIntent, int flagsMask, int flagsValues,
                                                int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        if (requestCode != -1) {
            throw new IllegalStateException(
                    "Starting intent sender with a requestCode requires a FragmentActivity host");
        }
        mContext.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags,
                options);
    }

    /**
     * Requests permissions from the given fragment.
     * See {@link android.app.Activity#requestPermissions(String[], int)}
     */
    public void onRequestPermissionsFromFragment(@NonNull android.app.Fragment fragment,
            @NonNull String[] permissions, int requestCode) {
    }

    /**
     * Return {@code true} if there are window animations.
     */
    public boolean onHasWindowAnimations() {
        return true;
    }

    /**
     * Return the window animations.
     */
    public int onGetWindowAnimations() {
        return mWindowAnimations;
    }

    /**
     * Called when a {@link android.app.Fragment} is being attached to this host, immediately
     * after the call to its {@link android.app.Fragment#onAttach(Context)} method and before
     * {@link android.app.Fragment#onCreate(Bundle)}.
     */
    public void onAttachFragment(Fragment fragment) {
    }

    @Nullable
    @Override
    public <T extends View> T onFindViewById(int id) {
        return null;
    }

    @Override
    public boolean onHasView() {
        return true;
    }

    boolean getRetainLoaders() {
        return mRetainLoaders;
    }

    Activity getActivity() {
        return mActivity;
    }

    Context getContext() {
        return mContext;
    }

    Handler getHandler() {
        return mHandler;
    }

    FragmentManagerImpl getFragmentManagerImpl() {
        return mFragmentManager;
    }

    LoaderManagerImpl getLoaderManagerImpl() {
        if (mLoaderManager != null) {
            return mLoaderManager;
        }
        mCheckedForLoaderManager = true;
        mLoaderManager = getLoaderManager("(root)", mLoadersStarted, true /*create*/);
        return mLoaderManager;
    }

    void inactivateFragment(String who) {
        //Log.v(TAG, "invalidateSupportFragment: who=" + who);
        if (mAllLoaderManagers != null) {
            LoaderManagerImpl lm = (LoaderManagerImpl) mAllLoaderManagers.get(who);
            if (lm != null && !lm.mRetaining) {
                lm.doDestroy();
                mAllLoaderManagers.remove(who);
            }
        }
    }

    void doLoaderStart() {
        if (mLoadersStarted) {
            return;
        }
        mLoadersStarted = true;

        if (mLoaderManager != null) {
            mLoaderManager.doStart();
        } else if (!mCheckedForLoaderManager) {
            mLoaderManager = getLoaderManager("(root)", mLoadersStarted, false);
        }
        mCheckedForLoaderManager = true;
    }

    void doLoaderStop(boolean retain) {
        mRetainLoaders = retain;

        if (mLoaderManager == null) {
            return;
        }

        if (!mLoadersStarted) {
            return;
        }
        mLoadersStarted = false;

        if (retain) {
            mLoaderManager.doRetain();
        } else {
            mLoaderManager.doStop();
        }
    }

    void doLoaderRetain() {
        if (mLoaderManager == null) {
            return;
        }
        mLoaderManager.doRetain();
    }

    void doLoaderDestroy() {
        if (mLoaderManager == null) {
            return;
        }
        mLoaderManager.doDestroy();
    }

    void reportLoaderStart() {
        if (mAllLoaderManagers != null) {
            final int N = mAllLoaderManagers.size();
            LoaderManagerImpl loaders[] = new LoaderManagerImpl[N];
            for (int i=N-1; i>=0; i--) {
                loaders[i] = (LoaderManagerImpl) mAllLoaderManagers.valueAt(i);
            }
            for (int i=0; i<N; i++) {
                LoaderManagerImpl lm = loaders[i];
                lm.finishRetain();
                lm.doReportStart();
            }
        }
    }

    LoaderManagerImpl getLoaderManager(String who, boolean started, boolean create) {
        if (mAllLoaderManagers == null) {
            mAllLoaderManagers = new ArrayMap<String, LoaderManager>();
        }
        LoaderManagerImpl lm = (LoaderManagerImpl) mAllLoaderManagers.get(who);
        if (lm == null && create) {
            lm = new LoaderManagerImpl(who, this, started);
            mAllLoaderManagers.put(who, lm);
        } else if (started && lm != null && !lm.mStarted){
            lm.doStart();
        }
        return lm;
    }

    ArrayMap<String, LoaderManager> retainLoaderNonConfig() {
        boolean retainLoaders = false;
        if (mAllLoaderManagers != null) {
            // Restart any loader managers that were already stopped so that they
            // will be ready to retain
            final int N = mAllLoaderManagers.size();
            LoaderManagerImpl loaders[] = new LoaderManagerImpl[N];
            for (int i=N-1; i>=0; i--) {
                loaders[i] = (LoaderManagerImpl) mAllLoaderManagers.valueAt(i);
            }
            final boolean doRetainLoaders = getRetainLoaders();
            for (int i=0; i<N; i++) {
                LoaderManagerImpl lm = loaders[i];
                if (!lm.mRetaining && doRetainLoaders) {
                    if (!lm.mStarted) {
                        lm.doStart();
                    }
                    lm.doRetain();
                }
                if (lm.mRetaining) {
                    retainLoaders = true;
                } else {
                    lm.doDestroy();
                    mAllLoaderManagers.remove(lm.mWho);
                }
            }
        }

        if (retainLoaders) {
            return mAllLoaderManagers;
        }
        return null;
    }

    void restoreLoaderNonConfig(ArrayMap<String, LoaderManager> loaderManagers) {
        if (loaderManagers != null) {
            for (int i = 0, N = loaderManagers.size(); i < N; i++) {
                ((LoaderManagerImpl) loaderManagers.valueAt(i)).updateHostController(this);
            }
        }
        mAllLoaderManagers = loaderManagers;
    }

    void dumpLoaders(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.print(prefix); writer.print("mLoadersStarted=");
        writer.println(mLoadersStarted);
        if (mLoaderManager != null) {
            writer.print(prefix); writer.print("Loader Manager ");
            writer.print(Integer.toHexString(System.identityHashCode(mLoaderManager)));
            writer.println(":");
            mLoaderManager.dump(prefix + "  ", fd, writer, args);
        }
    }
}

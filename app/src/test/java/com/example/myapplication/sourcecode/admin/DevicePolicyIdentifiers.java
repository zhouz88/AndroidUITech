/*
 * Copyright (C) 2023 The Android Open Source Project
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

package com.example.myapplication.sourcecode.admin;

import android.annotation.NonNull;
import android.annotation.TestApi;
import android.app.admin.DevicePolicyManager;
import android.app.admin.PolicyUpdateReceiver;
import android.os.UserManager;

import java.util.Objects;

/**
 * Class containing identifiers for policy APIs in {@link android.app.admin.DevicePolicyManager}, for example they
 * will be passed in {@link android.app.admin.PolicyUpdateReceiver#onPolicySetResult} and
 * {@link PolicyUpdateReceiver#onPolicyChanged} to communicate updates of a certain policy back
 * to the admin.
 */
public final class DevicePolicyIdentifiers {

    private DevicePolicyIdentifiers() {}

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setAutoTimeZoneEnabled}.
     */
    public static final String AUTO_TIMEZONE_POLICY = "autoTimezone";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setPermissionGrantState}.
     */
    public static final String PERMISSION_GRANT_POLICY = "permissionGrant";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setLockTaskPackages}.
     */
    public static final String LOCK_TASK_POLICY = "lockTask";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setUserControlDisabledPackages}.
     */
    public static final String USER_CONTROL_DISABLED_PACKAGES_POLICY =
            "userControlDisabledPackages";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#addPersistentPreferredActivity}.
     */
    public static final String PERSISTENT_PREFERRED_ACTIVITY_POLICY =
            "persistentPreferredActivity";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setUninstallBlocked}.
     */
    public static final String PACKAGE_UNINSTALL_BLOCKED_POLICY = "packageUninstallBlocked";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setApplicationRestrictions}.
     */
    public static final String APPLICATION_RESTRICTIONS_POLICY = "applicationRestrictions";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setResetPasswordToken}.
     */
    public static final String RESET_PASSWORD_TOKEN_POLICY = "resetPasswordToken";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setAccountManagementDisabled}.
     */
    public static final String ACCOUNT_MANAGEMENT_DISABLED_POLICY = "accountManagementDisabled";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setApplicationHidden}.
     */
    public static final String APPLICATION_HIDDEN_POLICY = "applicationHidden";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setCameraDisabled}.
     */
    public static final String CAMERA_DISABLED_POLICY = "cameraDisabled";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setStatusBarDisabled}.
     */
    public static final String STATUS_BAR_DISABLED_POLICY = "statusBarDisabled";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setPackagesSuspended}.
     */
    public static final String PACKAGES_SUSPENDED_POLICY = "packagesSuspended";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setKeyguardDisabledFeatures}.
     */
    public static final String KEYGUARD_DISABLED_FEATURES_POLICY = "keyguardDisabledFeatures";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setAutoTimeEnabled}.
     */
    public static final String AUTO_TIME_POLICY = "autoTime";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setBackupServiceEnabled}.
     */
    public static final String BACKUP_SERVICE_POLICY = "backupService";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setPermittedInputMethods}.
     *
     * @hide
     */
    @TestApi
    public static final String PERMITTED_INPUT_METHODS_POLICY = "permittedInputMethods";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setPersonalAppsSuspended}.
     *
     * @hide
     */
    @TestApi
    public static final String PERSONAL_APPS_SUSPENDED_POLICY = "personalAppsSuspended";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setScreenCaptureDisabled}.
     *
     * @hide
     */
    @TestApi
    public static final String SCREEN_CAPTURE_DISABLED_POLICY = "screenCaptureDisabled";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#setTrustAgentConfiguration}.
     *
     * @hide
     */
    public static final String TRUST_AGENT_CONFIGURATION_POLICY = "trustAgentConfiguration";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#addCrossProfileIntentFilter}.
     *
     * @hide
     */
    public static final String CROSS_PROFILE_INTENT_FILTER_POLICY = "crossProfileIntentFilter";

    /**
     * String identifier for {@link android.app.admin.DevicePolicyManager#addCrossProfileWidgetProvider}.
     *
     * @hide
     */
    public static final String CROSS_PROFILE_WIDGET_PROVIDER_POLICY = "crossProfileWidgetProvider";

    /**
     * @hide
     */
    public static final String USER_RESTRICTION_PREFIX = "userRestriction_";

    /**
     * Returns a string identifier for the provided user restrictions, see
     * {@link DevicePolicyManager#addUserRestriction} and {@link UserManager} for the list of
     * available restrictions.
     */
    @NonNull
    public static String getIdentifierForUserRestriction(
            @UserManager.UserRestrictionKey @NonNull String restriction) {
        Objects.requireNonNull(restriction);
        return USER_RESTRICTION_PREFIX + restriction;
    }
}

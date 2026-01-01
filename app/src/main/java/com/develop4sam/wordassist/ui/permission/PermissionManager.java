package com.develop4sam.wordassist.ui.permission;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

public class PermissionManager {

    /* ---------------- Overlay ---------------- */

    public static boolean hasOverlayPermission(Context context) {
        return Settings.canDrawOverlays(context);
    }

    public static void requestOverlayPermission(Context context) {
        Intent intent = new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.getPackageName())
        );
        context.startActivity(intent);
    }

    /* ---------------- Accessibility ---------------- */

    public static boolean isAccessibilityEnabled(
            Context context,
            Class<? extends AccessibilityService> service
    ) {
        AccessibilityManager manager =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);

        if (manager == null) return false;

        List<AccessibilityServiceInfo> enabledServices =
                manager.getEnabledAccessibilityServiceList(
                        AccessibilityServiceInfo.FEEDBACK_ALL_MASK
                );

        for (AccessibilityServiceInfo info : enabledServices) {
            if (info.getResolveInfo() != null &&
                    info.getResolveInfo().serviceInfo != null) {

                String enabledService =
                        info.getResolveInfo().serviceInfo.packageName + "/" +
                                info.getResolveInfo().serviceInfo.name;

                String targetService =
                        context.getPackageName() + "/" + service.getName();

                if (enabledService.equals(targetService)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void openAccessibilitySettings(Context context) {
        context.startActivity(
                new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        );
    }
}

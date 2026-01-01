package com.develop4sam.wordassist.ui.permission;

import android.app.AlertDialog;
import android.content.Context;

public class PermissionDialog {

    public static void showOverlayExplanation(
            Context context,
            Runnable onProceed
    ) {
        new AlertDialog.Builder(context)
                .setTitle("Overlay Permission Required")
                .setMessage(
                        "WordAssist needs permission to display a floating widget over other apps.\n\n" +
                                "This allows you to see word meanings instantly without leaving your current screen."
                )
                .setPositiveButton("Continue", (d, w) -> onProceed.run())
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static void showAccessibilityExplanation(
            Context context,
            Runnable onProceed
    ) {
        new AlertDialog.Builder(context)
                .setTitle("Accessibility Permission Required")
                .setMessage(
                        "WordAssist needs permission to display a floating widget over other apps.\n\n" +
                                "This allows you to see word meanings instantly without leaving your current screen."
                )
                .setPositiveButton("Open Settings", (d, w) -> onProceed.run())
                .setNegativeButton("Cancel", null)
                .show();
    }
}

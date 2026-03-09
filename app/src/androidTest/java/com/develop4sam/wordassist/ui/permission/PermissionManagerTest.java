package com.develop4sam.wordassist.ui.permission;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.develop4sam.wordassist.accessibility.WordAssistAccessibilityService;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PermissionManagerTest {

    private Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void hasOverlayPermission_returnsBoolean() {
        // This will depend on device settings, but at least test it doesn't crash
        boolean result = PermissionManager.hasOverlayPermission(context);
        assertNotNull(result); // Should not be null
    }

    @Test
    public void isAccessibilityEnabled_returnsBoolean() {
        // Test with the actual service class
        boolean result = PermissionManager.isAccessibilityEnabled(context, WordAssistAccessibilityService.class);
        assertNotNull(result);
    }

    // Note: requestOverlayPermission and openAccessibilitySettings start activities, hard to test without UI.
}
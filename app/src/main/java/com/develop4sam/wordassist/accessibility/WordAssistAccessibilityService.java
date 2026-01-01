package com.develop4sam.wordassist.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.develop4sam.wordassist.overlay.service.OverlayService;
import com.develop4sam.wordassist.utils.Debouncer;

public class WordAssistAccessibilityService extends AccessibilityService {

    private static final String TAG = "WordAssistA11y";
    private String lastWord = "";

    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        // Critical events for text selection across apps
        info.eventTypes =
                AccessibilityEvent.TYPE_VIEW_SELECTED |                  // Best for selection
                        AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED |    // Fallback for EditText
                        AccessibilityEvent.TYPE_VIEW_LONG_CLICKED |              // Optional: detect long press
                        AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;         // Helps re-arm

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
                | AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
                | AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS; // Important!

        info.notificationTimeout = 100;

        setServiceInfo(info);
        Log.d(TAG, "Accessibility service connected and armed for selection");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null || event.getSource() == null) return;

        int eventType = event.getEventType();

        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                handlePossibleSelection(event);
                break;

            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                // Optional: some apps only trigger long click
                findAndProcessSelectedText(event.getSource());
                break;

            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                // Optional: helps refresh node tree
                break;
        }
    }

    private void handlePossibleSelection(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (source == null) return;

        // First try the classic way (for EditText)
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
            int start = event.getFromIndex();
            int end = event.getToIndex();
            if (start >= 0 && end > start && source.getText() != null
                    && end <= source.getText().length()) {
                String selected = source.getText().subSequence(start, end).toString().trim();
                if (isValidWord(selected)) {
                    triggerOverlay(selected);
                    return;
                }
            }
        }

        // Fallback: manually search for selected text in the node or parents
        findAndProcessSelectedText(source);
    }

    private void findAndProcessSelectedText(AccessibilityNodeInfo node) {
        if (node == null) return;

        if (!Debouncer.shouldProcess(500)) {
            node.recycle();
            return;
        }

        String selectedText = extractSelectedText(node);

        if (isValidWord(selectedText)) {
            triggerOverlay(selectedText);
        }

        node.recycle();
    }

    private String extractSelectedText(AccessibilityNodeInfo node) {
        // Check if node itself has selection
        if (node.isSelected() && node.getText() != null) {
            return node.getText().toString().trim();
        }

        // Some apps mark selection via textSelectionStart/End
        try {
            if (node.getTextSelectionStart() >= 0 && node.getTextSelectionEnd() > node.getTextSelectionStart()
                    && node.getText() != null) {
                int start = node.getTextSelectionStart();
                int end = node.getTextSelectionEnd();
                if (end <= node.getText().length()) {
                    return node.getText().subSequence(start, end).toString().trim();
                }
            }
        } catch (Exception ignored) {}

        // Traverse up parents (common in WebView, custom views)
        AccessibilityNodeInfo parent = node.getParent();
        while (parent != null) {
            if (parent.getText() != null && parent.getText().length() > 0) {
                CharSequence text = parent.getText();
                // Some apps expose selection via content description or other props
                if (text.length() < 100) { // Avoid huge blocks
                    String candidate = text.toString().trim();
                    if (isLikelyWord(candidate)) {
                        return candidate;
                    }
                }
            }
            AccessibilityNodeInfo temp = parent;
            parent = parent.getParent();
            temp.recycle();
        }

        return null;
    }

    private boolean isValidWord(String text) {
        if (TextUtils.isEmpty(text) || text.length() < 2 || text.length() > 50) return false;
        if (text.equalsIgnoreCase(lastWord)) return false;

        // Basic filter: contains at least one letter
        if (!text.matches(".*[a-zA-Z].*")) return false;

        lastWord = text;
        Log.d(TAG, "Valid selected word: " + text);
        return true;
    }

    private boolean isLikelyWord(String text) {
        return text.length() >= 2 && text.length() <= 30 && text.matches(".*[a-zA-Z].*");
    }

    private void triggerOverlay(String word) {
        Log.d(TAG, "Triggering overlay for: " + word);

        Intent intent = new Intent(this, OverlayService.class);
        intent.putExtra("text", word);
        startService(intent);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "Service interrupted");
    }
}
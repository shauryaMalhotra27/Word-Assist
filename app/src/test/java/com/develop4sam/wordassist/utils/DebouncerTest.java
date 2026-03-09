package com.develop4sam.wordassist.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class DebouncerTest {

    @Test
    public void shouldProcess_firstCall_returnsTrue() {
        // Reset static state if needed, but since it's static, hard to reset.
        // For simplicity, assume tests run in order.
        assertTrue(Debouncer.shouldProcess(1000));
    }

    @Test
    public void shouldProcess_withinDelay_returnsFalse() throws InterruptedException {
        Debouncer.shouldProcess(1000); // First call
        Thread.sleep(500); // Wait less than delay
        assertFalse(Debouncer.shouldProcess(1000));
    }

    @Test
    public void shouldProcess_afterDelay_returnsTrue() throws InterruptedException {
        Debouncer.shouldProcess(1000);
        Thread.sleep(1100); // Wait more than delay
        assertTrue(Debouncer.shouldProcess(1000));
    }
}
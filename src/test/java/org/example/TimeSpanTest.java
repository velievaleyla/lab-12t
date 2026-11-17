package org.example;

import org.example.exceptions.IllegalInputTimeSpanException;
import org.example.exceptions.IllegalScaleFactorException;
import org.example.exceptions.NegativeTimeSpanException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TimeSpanTest {

    @Test
    @DisplayName("Default constructor creates 0h 0m")
    void testDefaultConstructor() {
        TimeSpan span = new TimeSpan();
        assertEquals(0, span.getHours());
        assertEquals(0, span.getMinutes());
        assertEquals(0, span.getTotalMinutes());
        assertEquals(0.0, span.getTotalHours());
    }

    @Test
    @DisplayName("Single-argument constructor correctly converts minutes")
    void testSingleArgConstructor() {
        TimeSpan span = new TimeSpan(135);
        assertEquals(2, span.getHours());
        assertEquals(15, span.getMinutes());
        assertEquals(135, span.getTotalMinutes());
        assertEquals(2.25, span.getTotalHours());
    }

    @Test
    @DisplayName("Two-argument constructor sets hours and minutes")
    void testTwoArgConstructor() {
        TimeSpan span = new TimeSpan(3, 45);
        assertEquals(3, span.getHours());
        assertEquals(45, span.getMinutes());
    }

    @Test
    @DisplayName("Copy constructor duplicates span")
    void testCopyConstructor() {
        TimeSpan original = new TimeSpan(2, 30);
        TimeSpan copy = new TimeSpan(original);
        assertEquals(original, copy);
        assertEquals(original.hashCode(), copy.hashCode());
    }

    @Test
    @DisplayName("IllegalInputTimeSpanException on negative inputs in constructor")
    void testIllegalInputInConstructors() {
        assertThrows(IllegalInputTimeSpanException.class, () -> new TimeSpan(-5));
        assertThrows(IllegalInputTimeSpanException.class, () -> new TimeSpan(-1, 30));
        assertThrows(IllegalInputTimeSpanException.class, () -> new TimeSpan(1, -10));
        assertThrows(IllegalInputTimeSpanException.class, () -> new TimeSpan(1, 60));
        assertThrows(IllegalInputTimeSpanException.class, () -> new TimeSpan((TimeSpan) null));
    }

    @Test
    @DisplayName("add methods correctly add time")
    void testAddMethods() {
        TimeSpan span = new TimeSpan(1, 40);
        span.add(1, 30);
        assertEquals(3, span.getHours());
        assertEquals(10, span.getMinutes());

        span.add(50);
        assertEquals(4, span.getHours());
        assertEquals(0, span.getMinutes());

        span.add(new TimeSpan(1, 15));
        assertEquals(5, span.getHours());
        assertEquals(15, span.getMinutes());
    }

    @Test
    @DisplayName("add methods throw IllegalInputTimeSpanException on invalid input")
    void testAddInvalidInputs() {
        TimeSpan span = new TimeSpan(1, 0);
        assertThrows(IllegalInputTimeSpanException.class, () -> span.add(-1));
        assertThrows(IllegalInputTimeSpanException.class, () -> span.add(-1, 10));
        assertThrows(IllegalInputTimeSpanException.class, () -> span.add(1, 75));
        assertThrows(IllegalInputTimeSpanException.class, () -> span.add(null));
    }

    @Test
    @DisplayName("subtract methods correctly subtract time")
    void testSubtractMethods() {
        TimeSpan span = new TimeSpan(3, 30);
        span.subtract(1, 15);
        assertEquals(2, span.getHours());
        assertEquals(15, span.getMinutes());

        span.subtract(45);
        assertEquals(1, span.getHours());
        assertEquals(30, span.getMinutes());

        span.subtract(new TimeSpan(1, 0));
        assertEquals(0, span.getHours());
        assertEquals(30, span.getMinutes());
    }

    @Test
    @DisplayName("subtract throws NegativeTimeSpanException when result < 0")
    void testSubtractNegativeResult() {
        TimeSpan span = new TimeSpan(1, 0); // 60 min
        assertThrows(NegativeTimeSpanException.class, () -> span.subtract(1, 1)); // 61 min
        assertThrows(NegativeTimeSpanException.class, () -> span.subtract(65));
        assertThrows(NegativeTimeSpanException.class, () -> span.subtract(new TimeSpan(2, 0)));
    }

    @Test
    @DisplayName("subtract throws IllegalInputTimeSpanException on invalid input")
    void testSubtractInvalidInput() {
        TimeSpan span = new TimeSpan(2, 0);
        assertThrows(IllegalInputTimeSpanException.class, () -> span.subtract(-10));
        assertThrows(IllegalInputTimeSpanException.class, () -> span.subtract(-1, 10));
        assertThrows(IllegalInputTimeSpanException.class, () -> span.subtract(1, 70));
        assertThrows(IllegalInputTimeSpanException.class, () -> span.subtract(null));
    }

    @Test
    @DisplayName("scale multiplies TimeSpan correctly")
    void testScale() {
        TimeSpan span = new TimeSpan(1, 15);
        span.scale(2);
        assertEquals(2, span.getHours());
        assertEquals(30, span.getMinutes());
    }

    @Test
    @DisplayName("scale throws IllegalScaleFactorException on factor <= 0")
    void testScaleInvalidFactor() {
        TimeSpan span = new TimeSpan(1, 0);
        assertThrows(IllegalScaleFactorException.class, () -> span.scale(0));
        assertThrows(IllegalScaleFactorException.class, () -> span.scale(-2));
    }
}

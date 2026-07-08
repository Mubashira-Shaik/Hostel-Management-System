package com.hosteltest;

	import com.custom.FeeException;
	import org.junit.jupiter.api.DisplayName;
	import org.junit.jupiter.api.Test;
	import static org.junit.jupiter.api.Assertions.*;

	/**
	 * Tests for Fee business logic validation
	 * These tests run WITHOUT a database — they only test logic.
	 */
	public class FeeValidationTest {

	    // ── Helper: simulate the validation in FeeServiceImpl ─────────────────
	    private void validateAmount(double amount) {
	        if (amount <= 0) throw new FeeException("Fee amount must be greater than zero.");
	    }

	    @Test
	    @DisplayName("Positive amount should not throw exception")
	    void testValidAmount() {
	        // Should run without throwing any exception
	        assertDoesNotThrow(() -> validateAmount(15000.0));
	    }

	    @Test
	    @DisplayName("Zero amount should throw FeeException")
	    void testZeroAmount() {
	        FeeException ex = assertThrows(FeeException.class,
	            () -> validateAmount(0));
	        assertEquals("Fee amount must be greater than zero.", ex.getMessage());
	    }

	    @Test
	    @DisplayName("Negative amount should throw FeeException")
	    void testNegativeAmount() {
	        assertThrows(FeeException.class, () -> validateAmount(-500));
	    }
	}
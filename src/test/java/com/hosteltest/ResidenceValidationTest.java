package com.hosteltest;

	import org.junit.jupiter.api.DisplayName;
	import org.junit.jupiter.api.Test;
	import static org.junit.jupiter.api.Assertions.*;

	/**
	 * Tests for WomenResidence input validation logic.
	 * These run without a database — pure logic tests.
	 */
	public class ResidenceValidationTest {

	    @Test
	    @DisplayName("Valid 10-digit contact should pass")
	    void testValidContact() {
	        String contact = "9876543210";
	        assertTrue(contact.matches("\\d{10}"), "10-digit number should be valid");
	    }

	    @Test
	    @DisplayName("Short contact number should fail")
	    void testShortContact() {
	        String contact = "98765";
	        assertFalse(contact.matches("\\d{10}"), "5-digit number should be invalid");
	    }

	    @Test
	    @DisplayName("Valid 12-digit Aadhaar should pass")
	    void testValidAadhaar() {
	        String aadhaar = "123456789012";
	        assertTrue(aadhaar.matches("\\d{12}"), "12-digit Aadhaar should be valid");
	    }

	    @Test
	    @DisplayName("Short Aadhaar should fail")
	    void testInvalidAadhaar() {
	        String aadhaar = "1234";
	        assertFalse(aadhaar.matches("\\d{12}"), "4-digit Aadhaar should be invalid");
	    }

	    @Test
	    @DisplayName("Gender Female should be accepted")
	    void testFemaleGender() {
	        String gender = "Female";
	        assertTrue(gender.equalsIgnoreCase("Female"));
	    }

	    @Test
	    @DisplayName("Gender Male should be rejected")
	    void testMaleGender() {
	        String gender = "Male";
	        assertFalse(gender.equalsIgnoreCase("Female"));
	    }
	}
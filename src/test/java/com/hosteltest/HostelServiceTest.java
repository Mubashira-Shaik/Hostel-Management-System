package com.hosteltest;

	import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.hibernateUtil.HibernateUtil;
import com.hostelEntity.Hostel;

	/**
	 * Unit Tests for Hostel Entity — CRUD operations
	 *
	 * HOW TO RUN:
	 * Right-click this file → Run As → JUnit Test
	 *
	 * GREEN bar = all tests passed ✅
	 * RED bar   = at least one test failed ❌
	 */
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class HostelServiceTest {

	    static SessionFactory sf;
	    static int savedHostelId;  // stores ID from save test for use in later tests

	    // ── Runs ONCE before all tests — opens DB connection ──────────────────
	    @BeforeAll
	    static void setUp() {
	        sf = HibernateUtil.getSessionFactory();
	        System.out.println("=== Test DB connection opened ===");
	    }

	    // ── Runs ONCE after all tests — closes DB connection ──────────────────
	    @AfterAll
	    static void tearDown() {
	        HibernateUtil.shutdown();
	        System.out.println("=== Test DB connection closed ===");
	    }

	    // ── TEST 1: Save a Hostel ─────────────────────────────────────────────
	    @Test
	    @Order(1)
	    @DisplayName("Test 1: Save a new Hostel")
	    void testSaveHostel() {
	        Transaction tx = null;
	        try (Session session = sf.openSession()) {
	            tx = session.beginTransaction();

	            Hostel hostel = new Hostel();
	            hostel.setHostelName("Test Hostel JUnit");
	            hostel.setLocation("Test City");

	            session.persist(hostel);
	            tx.commit();

	            // hostelId must be auto-generated (greater than 0)
	            assertTrue(hostel.getHostelId() > 0,
	                "Hostel ID should be auto-generated and > 0");

	            savedHostelId = hostel.getHostelId();
	            System.out.println("Saved Hostel ID: " + savedHostelId);
	        }
	    }

	    // ── TEST 2: Read the Hostel back from DB ──────────────────────────────
	    @Test
	    @Order(2)
	    @DisplayName("Test 2: Read saved Hostel from DB")
	    void testGetHostel() {
	        try (Session session = sf.openSession()) {
	            Hostel hostel = session.find(Hostel.class, savedHostelId);

	            // Must not be null
	            assertNotNull(hostel, "Hostel should exist in DB");

	            // Name and location must match what we saved
	            assertEquals("Test Hostel JUnit", hostel.getHostelName());
	            assertEquals("Test City",         hostel.getLocation());

	            System.out.println("Read Hostel: " + hostel.getHostelName());
	        }
	    }

	    // ── TEST 3: Update the Hostel ─────────────────────────────────────────
	    @Test
	    @Order(3)
	    @DisplayName("Test 3: Update Hostel location")
	    void testUpdateHostel() {
	        Transaction tx = null;
	        try (Session session = sf.openSession()) {
	            tx = session.beginTransaction();

	            Hostel hostel = session.find(Hostel.class, savedHostelId);
	            hostel.setLocation("Updated City");
	            session.merge(hostel);
	            tx.commit();
	        }

	        // Re-read and confirm update was saved
	        try (Session session = sf.openSession()) {
	            Hostel updated = session.find(Hostel.class, savedHostelId);
	            assertEquals("Updated City", updated.getLocation(),
	                "Location should be updated to 'Updated City'");
	            System.out.println("Updated location: " + updated.getLocation());
	        }
	    }

	    // ── TEST 4: Delete the Hostel ─────────────────────────────────────────
	    @Test
	    @Order(4)
	    @DisplayName("Test 4: Delete Hostel")
	    void testDeleteHostel() {
	        Transaction tx = null;
	        try (Session session = sf.openSession()) {
	            tx = session.beginTransaction();
	            Hostel hostel = session.find(Hostel.class, savedHostelId);
	            session.remove(hostel);
	            tx.commit();
	        }

	        // Re-read and confirm it is gone
	        try (Session session = sf.openSession()) {
	            Hostel deleted = session.find(Hostel.class, savedHostelId);
	            assertNull(deleted, "Hostel should be null after deletion");
	            System.out.println("Hostel deleted confirmed.");
	        }
	    }

	    // ── TEST 5: Negative Test — find hostel that doesn't exist ────────────
	    @Test
	    @Order(5)
	    @DisplayName("Test 5: Lookup non-existent Hostel returns null")
	    void testGetNonExistentHostel() {
	        try (Session session = sf.openSession()) {
	            Hostel hostel = session.find(Hostel.class, 99999);
	            assertNull(hostel, "Should return null for non-existent ID");
	            System.out.println("Non-existent hostel correctly returned null.");
	        }
	    }
	}
package com.services;

import org.hibernate.SessionFactory;

/**
 * HostelService Interface
 * Defines the contract for all hostel management operations.
 *
 * FIXES APPLIED:
 * - Original file had a COMMENTED-OUT duplicate of the same interface — removed
 */
public interface HostelService {

    void saveHostel(SessionFactory sessionFactory);

    void getHostel(SessionFactory sessionFactory);

    void getAllHostels(SessionFactory sessionFactory);

    void updateHostel(SessionFactory sessionFactory);

    void deleteHostel(SessionFactory sessionFactory);
}
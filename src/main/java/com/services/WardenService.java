package com.services;

import org.hibernate.SessionFactory;

/**
 * WardenService Interface
 * Defines the contract for all warden management operations.
 */
public interface WardenService {

    void saveWarden(SessionFactory sessionFactory);

    void getWarden(SessionFactory sessionFactory);

    void getAllWardens(SessionFactory sessionFactory);

    void updateWarden(SessionFactory sessionFactory);

    void deleteWarden(SessionFactory sessionFactory);
}
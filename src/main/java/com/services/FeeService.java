package com.services;

import org.hibernate.SessionFactory;

/**
 * FeeService Interface
 * Defines the contract for all fee management operations.
 */
public interface FeeService {

    void saveFee(SessionFactory sessionFactory);

    void getFee(SessionFactory sessionFactory);

    void getAllFees(SessionFactory sessionFactory);

    void updateFee(SessionFactory sessionFactory);

    void deleteFee(SessionFactory sessionFactory);
}
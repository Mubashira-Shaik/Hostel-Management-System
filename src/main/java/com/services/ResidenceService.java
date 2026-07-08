package com.services;

import org.hibernate.SessionFactory;

/**
 * WomenResidenceService Interface
 * Defines the contract for all women's residence management operations.
 *
 * FIXES APPLIED:
 * - Was 'WomensResidenceService'   renamed to 'WomenResidenceService' (consistent with entity name)
 */
public interface ResidenceService {

    void saveResidence(SessionFactory sessionFactory);

    void getResidence(SessionFactory sessionFactory);

    void getAllResidences(SessionFactory sessionFactory);

    void updateResidence(SessionFactory sessionFactory);

    void deleteResidence(SessionFactory sessionFactory);
}
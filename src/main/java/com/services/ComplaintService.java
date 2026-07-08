package com.services;

import org.hibernate.SessionFactory;

/**
 * ComplaintService Interface
 * Defines the contract for all complaint management operations.
 */
public interface ComplaintService {

    void saveComplaint(SessionFactory sessionFactory);

    void getComplaint(SessionFactory sessionFactory);

    void getAllComplaints(SessionFactory sessionFactory);

    void updateComplaint(SessionFactory sessionFactory);

    void deleteComplaint(SessionFactory sessionFactory);
}
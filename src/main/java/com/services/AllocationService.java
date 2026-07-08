package com.services;

import org.hibernate.SessionFactory;

public interface AllocationService {

    void saveAllocation(SessionFactory sf);
    void getAllocation(SessionFactory sf);
    void getAllAllocations(SessionFactory sf);
    void updateAllocation(SessionFactory sf);
    void deleteAllocation(SessionFactory sf);
}
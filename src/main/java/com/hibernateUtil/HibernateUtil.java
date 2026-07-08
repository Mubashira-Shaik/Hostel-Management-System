package com.hibernateUtil;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.hostelEntity.Allocation;
import com.hostelEntity.Complaint;
import com.hostelEntity.Fee;
import com.hostelEntity.Hostel;
import com.hostelEntity.Residence;
import com.hostelEntity.Room;
import com.hostelEntity.Warden;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration config = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Hostel.class)
                    .addAnnotatedClass(Room.class)
                    .addAnnotatedClass(Residence.class)
                    .addAnnotatedClass(Warden.class)
                    .addAnnotatedClass(Allocation.class)
                    .addAnnotatedClass(Complaint.class)
                    .addAnnotatedClass(Fee.class);

            sessionFactory = config.buildSessionFactory();

        } catch (Exception e) {
            System.err.println("Hibernate initialization failed: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
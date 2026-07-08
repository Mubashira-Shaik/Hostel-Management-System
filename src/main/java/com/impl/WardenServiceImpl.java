package com.impl;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.hostelEntity.Hostel;
import com.hostelEntity.Warden;
import com.services.WardenService;
 
public class WardenServiceImpl implements WardenService {
 
    private static final Scanner sc = new Scanner(System.in);
 
    @Override
    public void saveWarden(SessionFactory sf) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
 
            Warden warden = new Warden();
 
            System.out.print("Enter Warden ID: ");
            warden.setWardenId(Integer.parseInt(sc.nextLine().trim()));
 
            System.out.print("Enter Warden Name: ");
            warden.setName(sc.nextLine().trim());
 
            System.out.print("Enter Contact Number: ");
            warden.setContact(sc.nextLine().trim());
 
            List<Hostel> hostels = session.createQuery("FROM Hostel", Hostel.class).getResultList();
            if (hostels.isEmpty()) {
                System.out.println("No hostels found. Please add a hostel first.");
                return;
            }
 
            System.out.println("\n--- SELECT HOSTEL ---");
            System.out.printf("%-5s %-25s %-20s%n", "No.", "Hostel Name", "Location");
            System.out.println("--------------------------------------------------");
            for (int i = 0; i < hostels.size(); i++) {
                System.out.printf("%-5d %-25s %-20s%n",
                    (i + 1), hostels.get(i).getHostelName(), hostels.get(i).getLocation());
            }
 
            System.out.print("Choose Hostel (1-" + hostels.size() + "): ");
            int choice = Integer.parseInt(sc.nextLine().trim());
 
            if (choice < 1 || choice > hostels.size()) {
                System.out.println("Invalid hostel selection.");
                return;
            }
 
            Hostel selectedHostel = hostels.get(choice - 1);
            warden.setHostel(selectedHostel);   // CHANGED from warden.setHostelId(int)
 
            session.persist(warden);
            tx.commit();
            System.out.println("Warden Saved! ID: " + warden.getWardenId()
                + " | Hostel: " + selectedHostel.getHostelName());
 
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error saving warden: " + e.getMessage());
        }
    }
 
    @Override
    public void getWarden(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            System.out.print("Enter Warden ID: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            Warden w = session.find(Warden.class, id);
            if (w != null) {
                printWarden(w);
            } else {
                System.out.println("Warden not found for ID: " + id);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    @Override
    public void getAllWardens(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            List<Warden> list = session.createQuery("FROM Warden", Warden.class).getResultList();
            if (list.isEmpty()) {
                System.out.println("No wardens found.");
            } else {
                System.out.printf("%-10s %-25s %-15s %-20s%n",
                    "ID", "Name", "Contact", "Hostel");
                System.out.println("--------------------------------------------------------------");
                for (Warden w : list) {
                    String hostelName = (w.getHostel() != null) ? w.getHostel().getHostelName() : "N/A";
                    System.out.printf("%-10d %-25s %-15s %-20s%n",
                        w.getWardenId(), w.getName(), w.getContact(), hostelName);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    @Override
    public void updateWarden(SessionFactory sf) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            System.out.print("Enter Warden ID to Update: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            Warden warden = session.find(Warden.class, id);
            if (warden == null) {
                System.out.println("Warden not found.");
                return;
            }
            System.out.println("1. Update Name  2. Update Contact");
            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine().trim());
            switch (ch) {
                case 1:
                    System.out.print("Enter New Name: ");
                    warden.setName(sc.nextLine().trim());
                    break;
                case 2:
                    System.out.print("Enter New Contact: ");
                    warden.setContact(sc.nextLine().trim());
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            session.merge(warden);
            tx.commit();
            System.out.println("Warden Updated Successfully!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    @Override
    public void deleteWarden(SessionFactory sf) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            System.out.print("Enter Warden ID to Delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            Warden warden = session.find(Warden.class, id);
            if (warden != null) {
                session.remove(warden);
                tx.commit();
                System.out.println("Warden Deleted! ID: " + id);
            } else {
                System.out.println("Warden not found for ID: " + id);
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void printWarden(Warden w) {
        System.out.println("\n--- WARDEN DETAILS ---");
        System.out.println("Warden ID : " + w.getWardenId());
        System.out.println("Name      : " + w.getName());
        System.out.println("Contact   : " + w.getContact());
        System.out.println("Hostel    : " + (w.getHostel() != null ? w.getHostel().getHostelName() : "N/A"));
    }
}
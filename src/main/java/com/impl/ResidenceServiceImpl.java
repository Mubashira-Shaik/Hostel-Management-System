package com.impl;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.hostelEntity.Hostel;
import com.hostelEntity.Residence;
import com.hostelEntity.Room;
import com.services.ResidenceService;

public class ResidenceServiceImpl implements ResidenceService {

    private static final Scanner sc = new Scanner(System.in);

    @Override
    public void saveResidence(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            Residence wr = new Residence();

            System.out.print("Enter Name: ");
            wr.setName(sc.nextLine().trim());

            System.out.print("Enter Contact (10 digits): ");
            String contact = sc.nextLine().trim();

            if (!contact.matches("\\d{10}")) {
                System.out.println("Invalid contact. Must be 10 digits.");
                tx.rollback();
                return;
            }

            wr.setContact(contact);

            System.out.print("Enter Address: ");
            wr.setAddress(sc.nextLine().trim());

            System.out.print("Enter DOB (yyyy-MM-dd): ");
            wr.setDob(sc.nextLine().trim());

            System.out.print("Enter Age: ");
            wr.setAge(Integer.parseInt(sc.nextLine().trim()));

            System.out.print("Enter Aadhaar Number (12 digits): ");
            String aadhaar = sc.nextLine().trim();

            if (!aadhaar.matches("\\d{12}")) {
                System.out.println("Invalid Aadhaar. Must be exactly 12 digits.");
                tx.rollback();
                return;
            }

            wr.setAadhaar(aadhaar);

            System.out.print("Enter Hostel ID: ");
            int hostelId = Integer.parseInt(sc.nextLine().trim());

            Hostel hostel = session.find(Hostel.class, hostelId);

            if (hostel == null) {
                System.out.println("Hostel not found!");
                tx.rollback();
                return;
            }

            wr.setHostel(hostel);

            System.out.print("Enter Gender (Male/Female): ");
            String gender = sc.nextLine().trim();

            if (!gender.equalsIgnoreCase(hostel.getHostelType())) {
                System.out.println("This hostel is only for "
                        + hostel.getHostelType()
                        + " residents.");
                tx.rollback();
                return;
            }

            wr.setGender(gender);

            System.out.print("Enter Room ID: ");
            int roomId = Integer.parseInt(sc.nextLine().trim());

            Room room = session.find(Room.class, roomId);

            if (room == null) {
                System.out.println("Room not found!");
                tx.rollback();
                return;
            }

            wr.setRoom(room);

            session.persist(wr);
            tx.commit();

            System.out.println("\nResidence Saved Successfully!");
            System.out.println("ID       : " + wr.getId());
            System.out.println("Name     : " + wr.getName());
            System.out.println("Gender   : " + wr.getGender());
            System.out.println("Hostel   : " + hostel.getHostelName());
            System.out.println("Room     : " + room.getRoomNumber());

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error saving residence: " + e.getMessage());
        }
    }

    @Override
    public void getResidence(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            System.out.print("Enter Residence ID: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Residence wr = session.find(Residence.class, id);

            if (wr != null) {
                printResidence(wr);
            } else {
                System.out.println("Residence not found for ID: " + id);
            }

        } catch (Exception e) {
            System.out.println("Error getting residence: " + e.getMessage());
        }
    }

    @Override
    public void getAllResidences(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            List<Residence> list = session
                    .createQuery("FROM Residence", Residence.class)
                    .getResultList();

            if (list.isEmpty()) {
                System.out.println("No residence records found.");
                return;
            }

            System.out.println("\n--- ALL RESIDENTS ---");
            System.out.printf("%-8s %-20s %-10s %-12s %-5s %-15s %-20s %-12s%n",
                    "ID", "Name", "Gender", "Contact", "Age", "DOB", "Hostel", "Room");

            System.out.println("--------------------------------------------------------------------------------------");

            for (Residence wr : list) {
                String hostelName = wr.getHostel() != null
                        ? wr.getHostel().getHostelName()
                        : "N/A";

                String roomNo = wr.getRoom() != null
                        ? wr.getRoom().getRoomNumber()
                        : "N/A";

                System.out.printf("%-8d %-20s %-10s %-12s %-5d %-15s %-20s %-12s%n",
                        wr.getId(),
                        wr.getName(),
                        wr.getGender(),
                        wr.getContact(),
                        wr.getAge(),
                        wr.getDob(),
                        hostelName,
                        roomNo);
            }

        } catch (Exception e) {
            System.out.println("Error getting residences: " + e.getMessage());
        }
    }

    @Override
    public void updateResidence(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            System.out.print("Enter Residence ID to Update: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Residence wr = session.find(Residence.class, id);

            if (wr == null) {
                System.out.println("Residence not found for ID: " + id);
                tx.rollback();
                return;
            }

            System.out.println("\n--- WHAT TO UPDATE ---");
            System.out.println("1. Address");
            System.out.println("2. Contact");
            System.out.println("3. Age");
            System.out.println("4. Room");
            System.out.print("Choose: ");

            int ch = Integer.parseInt(sc.nextLine().trim());

            switch (ch) {
                case 1:
                    System.out.print("Enter New Address: ");
                    wr.setAddress(sc.nextLine().trim());
                    break;

                case 2:
                    System.out.print("Enter New Contact (10 digits): ");
                    String contact = sc.nextLine().trim();

                    if (!contact.matches("\\d{10}")) {
                        System.out.println("Invalid contact.");
                        tx.rollback();
                        return;
                    }

                    wr.setContact(contact);
                    break;

                case 3:
                    System.out.print("Enter New Age: ");
                    wr.setAge(Integer.parseInt(sc.nextLine().trim()));
                    break;

                case 4:
                    System.out.print("Enter New Room ID: ");
                    int roomId = Integer.parseInt(sc.nextLine().trim());

                    Room room = session.find(Room.class, roomId);

                    if (room == null) {
                        System.out.println("Room not found!");
                        tx.rollback();
                        return;
                    }

                    wr.setRoom(room);
                    break;

                default:
                    System.out.println("Invalid choice.");
                    tx.rollback();
                    return;
            }

            session.merge(wr);
            tx.commit();

            System.out.println("Residence Updated Successfully!");

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error updating residence: " + e.getMessage());
        }
    }

    @Override
    public void deleteResidence(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            System.out.print("Enter Residence ID to Delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Residence wr = session.find(Residence.class, id);

            if (wr == null) {
                System.out.println("Residence not found for ID: " + id);
                tx.rollback();
                return;
            }

            session.remove(wr);
            tx.commit();

            System.out.println("Residence Deleted! ID: " + id);

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error deleting residence: " + e.getMessage());
        }
    }

    private void printResidence(Residence wr) {
        System.out.println("\n--- RESIDENCE DETAILS ---");
        System.out.println("ID       : " + wr.getId());
        System.out.println("Name     : " + wr.getName());
        System.out.println("Gender   : " + wr.getGender());
        System.out.println("Contact  : " + wr.getContact());
        System.out.println("Address  : " + wr.getAddress());
        System.out.println("DOB      : " + wr.getDob());
        System.out.println("Age      : " + wr.getAge());
        System.out.println("Aadhaar  : " + wr.getAadhaar());
        System.out.println("Hostel   : " + (wr.getHostel() != null ? wr.getHostel().getHostelName() : "N/A"));
        System.out.println("Room     : " + (wr.getRoom() != null ? wr.getRoom().getRoomNumber() : "N/A"));
    }
}
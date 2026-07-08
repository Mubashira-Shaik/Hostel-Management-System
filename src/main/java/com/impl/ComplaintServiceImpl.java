package com.impl;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.hostelEntity.Complaint;
import com.hostelEntity.Hostel;
import com.hostelEntity.Residence;
import com.hostelEntity.Room;
import com.services.ComplaintService;

public class ComplaintServiceImpl implements ComplaintService {

    private static final Scanner sc = new Scanner(System.in);

    @Override
    public void saveComplaint(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            Complaint complaint = new Complaint();

            System.out.print("Enter Hostel ID: ");
            int hostelId = Integer.parseInt(sc.nextLine().trim());

            Hostel hostel = session.find(Hostel.class, hostelId);

            if (hostel == null) {
                System.out.println("Hostel not found!");
                tx.rollback();
                return;
            }

            complaint.setHostel(hostel);

            System.out.print("Enter Residence ID: ");
            int residenceId = Integer.parseInt(sc.nextLine().trim());

            Residence residence = session.find(Residence.class, residenceId);

            if (residence == null) {
                System.out.println("Resident not found!");
                tx.rollback();
                return;
            }

            complaint.setResidence(residence);
            complaint.setStudentName(residence.getName());

            System.out.print("Enter Room ID: ");
            int roomId = Integer.parseInt(sc.nextLine().trim());

            Room room = session.find(Room.class, roomId);

            if (room == null) {
                System.out.println("Room not found!");
                tx.rollback();
                return;
            }

            complaint.setRoom(room);

            System.out.print("Enter Description: ");
            complaint.setDescription(sc.nextLine().trim());

            System.out.println("1. Open");
            System.out.println("2. Resolved");
            System.out.print("Choose Status: ");

            int statusChoice = Integer.parseInt(sc.nextLine().trim());

            if (statusChoice == 1) {
                complaint.setStatus("Open");
            } else if (statusChoice == 2) {
                complaint.setStatus("Resolved");
            } else {
                System.out.println("Invalid status.");
                tx.rollback();
                return;
            }

            session.persist(complaint);
            tx.commit();

            System.out.println("Complaint Saved Successfully!");
            System.out.println("Complaint ID: " + complaint.getComplaintId());

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.out.println("Error saving complaint: " + e.getMessage());
        }
    }

    @Override
    public void getComplaint(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            System.out.print("Enter Complaint ID: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Complaint complaint = session.find(Complaint.class, id);

            if (complaint != null) {
                printComplaint(complaint);
            } else {
                System.out.println("Complaint not found.");
            }

        } catch (Exception e) {
            System.out.println("Error getting complaint: " + e.getMessage());
        }
    }

    @Override
    public void getAllComplaints(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            List<Complaint> list = session
                    .createQuery("FROM Complaint", Complaint.class)
                    .getResultList();

            if (list.isEmpty()) {
                System.out.println("No complaints found.");
                return;
            }

            System.out.println("\n--- ALL COMPLAINTS ---");

            System.out.printf("%-5s %-20s %-20s %-15s %-30s %-10s%n",
                    "ID", "Student", "Hostel", "Room", "Description", "Status");

            System.out.println("--------------------------------------------------------------------------------");

            for (Complaint c : list) {
                String studentName = c.getResidence() != null
                        ? c.getResidence().getName()
                        : c.getStudentName();

                String hostelName = c.getHostel() != null
                        ? c.getHostel().getHostelName()
                        : "N/A";

                String roomNo = c.getRoom() != null
                        ? c.getRoom().getRoomNumber()
                        : "N/A";

                System.out.printf("%-5d %-20s %-20s %-15s %-30s %-10s%n",
                        c.getComplaintId(),
                        studentName,
                        hostelName,
                        roomNo,
                        truncate(c.getDescription(), 28),
                        c.getStatus());
            }

        } catch (Exception e) {
            System.out.println("Error getting complaints: " + e.getMessage());
        }
    }

    @Override
    public void updateComplaint(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            System.out.print("Enter Complaint ID to Update: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Complaint complaint = session.find(Complaint.class, id);

            if (complaint == null) {
                System.out.println("Complaint not found.");
                tx.rollback();
                return;
            }

            System.out.println("1. Update Description");
            System.out.println("2. Update Status");
            System.out.print("Choose: ");

            int ch = Integer.parseInt(sc.nextLine().trim());

            switch (ch) {
                case 1:
                    System.out.print("Enter New Description: ");
                    complaint.setDescription(sc.nextLine().trim());
                    break;

                case 2:
                    System.out.println("1. Open");
                    System.out.println("2. Resolved");
                    System.out.print("Choose New Status: ");

                    int status = Integer.parseInt(sc.nextLine().trim());

                    if (status == 1) {
                        complaint.setStatus("Open");
                    } else if (status == 2) {
                        complaint.setStatus("Resolved");
                    } else {
                        System.out.println("Invalid status.");
                        tx.rollback();
                        return;
                    }
                    break;

                default:
                    System.out.println("Invalid choice.");
                    tx.rollback();
                    return;
            }

            session.merge(complaint);
            tx.commit();

            System.out.println("Complaint Updated Successfully!");

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.out.println("Error updating complaint: " + e.getMessage());
        }
    }

    @Override
    public void deleteComplaint(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            System.out.print("Enter Complaint ID to Delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Complaint complaint = session.find(Complaint.class, id);

            if (complaint == null) {
                System.out.println("Complaint not found.");
                tx.rollback();
                return;
            }

            session.remove(complaint);
            tx.commit();

            System.out.println("Complaint Deleted Successfully!");

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.out.println("Error deleting complaint: " + e.getMessage());
        }
    }

    private void printComplaint(Complaint c) {
        System.out.println("\n--- COMPLAINT DETAILS ---");
        System.out.println("Complaint ID : " + c.getComplaintId());
        System.out.println("Student      : " + (c.getResidence() != null ? c.getResidence().getName() : c.getStudentName()));
        System.out.println("Hostel       : " + (c.getHostel() != null ? c.getHostel().getHostelName() : "N/A"));
        System.out.println("Room         : " + (c.getRoom() != null ? c.getRoom().getRoomNumber() : "N/A"));
        System.out.println("Description  : " + c.getDescription());
        System.out.println("Status       : " + c.getStatus());
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max) + "..." : s;
    }
}
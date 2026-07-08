package com.impl;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.hostelEntity.Fee;
import com.hostelEntity.Hostel;
import com.hostelEntity.Residence;
import com.hostelEntity.Room;
import com.services.FeeService;

public class FeeServiceImpl implements FeeService {

    private static final Scanner sc = new Scanner(System.in);

    @Override
    public void saveFee(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            System.out.print("Enter Residence ID: ");
            int residenceId = Integer.parseInt(sc.nextLine().trim());

            Residence residence = session.find(Residence.class, residenceId);
            if (residence == null) {
                System.out.println("Resident not found!");
                tx.rollback();
                return;
            }

            System.out.print("Enter Room ID: ");
            int roomId = Integer.parseInt(sc.nextLine().trim());

            Room room = session.find(Room.class, roomId);
            if (room == null) {
                System.out.println("Room not found!");
                tx.rollback();
                return;
            }

            Hostel hostel = room.getHostel();

            double amount = room.getFixedFee();

            System.out.print("Enter Payment Date yyyy-MM-dd: ");
            String paymentDate = sc.nextLine().trim();

            System.out.println("1. Paid");
            System.out.println("2. Pending");
            System.out.print("Choose Status: ");
            int statusChoice = Integer.parseInt(sc.nextLine().trim());

            String status = statusChoice == 1 ? "Paid" : "Pending";

            Fee fee = new Fee();
            fee.setResidence(residence);
            fee.setRoom(room);
            fee.setHostel(hostel);
            fee.setAmount(amount);
            fee.setPaymentDate(paymentDate);
            fee.setStatus(status);

            session.persist(fee);
            tx.commit();

            System.out.println("Fee Saved Successfully! Fee ID: " + fee.getFeeId());

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void getFee(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            System.out.print("Enter Fee ID: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Fee fee = session.find(Fee.class, id);

            if (fee != null) {
                printFee(fee);
            } else {
                System.out.println("Fee not found!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void getAllFees(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            List<Fee> list = session.createQuery("FROM Fee", Fee.class).getResultList();

            if (list.isEmpty()) {
                System.out.println("No fee records found.");
                return;
            }

            System.out.printf("%-5s %-20s %-10s %-12s %-10s%n",
                    "ID", "Student", "Amount", "Date", "Status");

            for (Fee f : list) {
                System.out.printf("%-5d %-20s %-10.2f %-12s %-10s%n",
                        f.getFeeId(),
                        f.getResidence() != null ? f.getResidence().getName() : "N/A",
                        f.getAmount(),
                        f.getPaymentDate(),
                        f.getStatus());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void updateFee(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            System.out.print("Enter Fee ID: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Fee fee = session.find(Fee.class, id);

            if (fee == null) {
                System.out.println("Fee not found!");
                tx.rollback();
                return;
            }

            System.out.println("1. Update Status");
            System.out.println("2. Update Payment Date");
            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine().trim());

            if (ch == 1) {
                System.out.println("1. Paid");
                System.out.println("2. Pending");
                int status = Integer.parseInt(sc.nextLine().trim());
                fee.setStatus(status == 1 ? "Paid" : "Pending");
            } else if (ch == 2) {
                System.out.print("Enter New Payment Date: ");
                fee.setPaymentDate(sc.nextLine().trim());
            } else {
                System.out.println("Invalid choice!");
                tx.rollback();
                return;
            }

            session.merge(fee);
            tx.commit();

            System.out.println("Fee Updated Successfully!");

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFee(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            System.out.print("Enter Fee ID: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Fee fee = session.find(Fee.class, id);

            if (fee == null) {
                System.out.println("Fee not found!");
                tx.rollback();
                return;
            }

            session.remove(fee);
            tx.commit();

            System.out.println("Fee Deleted Successfully!");

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    private void printFee(Fee f) {
        System.out.println("Fee ID  : " + f.getFeeId());
        System.out.println("Student : " + (f.getResidence() != null ? f.getResidence().getName() : "N/A"));
        System.out.println("Hostel  : " + (f.getHostel() != null ? f.getHostel().getHostelName() : "N/A"));
        System.out.println("Room    : " + (f.getRoom() != null ? f.getRoom().getRoomNumber() : "N/A"));
        System.out.println("Amount  : " + f.getAmount());
        System.out.println("Date    : " + f.getPaymentDate());
        System.out.println("Status  : " + f.getStatus());
    }
}
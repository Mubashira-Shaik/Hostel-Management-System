package com.hostelMain;

import java.util.Scanner;

import org.hibernate.SessionFactory;

import com.hibernateUtil.HibernateUtil;
import com.impl.AllocationServiceImpl;
import com.impl.ComplaintServiceImpl;
import com.impl.FeeServiceImpl;
import com.impl.HostelServiceImpl;
import com.impl.ResidenceServiceImpl;
import com.impl.RoomServiceImpl;
import com.impl.WardenServiceImpl;
import com.services.AllocationService;
import com.services.ComplaintService;
import com.services.FeeService;
import com.services.HostelService;
import com.services.ResidenceService;
import com.services.RoomService;
import com.services.WardenService;

public class MainApp {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        SessionFactory sf = HibernateUtil.getSessionFactory();

        HostelService hostelService = new HostelServiceImpl();
        RoomService roomService = new RoomServiceImpl();
        ResidenceService residenceService = new ResidenceServiceImpl();
        WardenService wardenService = new WardenServiceImpl();
        AllocationService allocationService = new AllocationServiceImpl();
        ComplaintService complaintService = new ComplaintServiceImpl();
        FeeService feeService = new FeeServiceImpl();

        while (true) {

            System.out.println("\n======================================");
            System.out.println("  HOSTEL MANAGEMENT SYSTEM ");
            System.out.println("======================================");
            System.out.println("1. Hostel Management");
            System.out.println("2. Room Management");
            System.out.println("3. Residence Management");
            System.out.println("4. Warden Management");
            System.out.println("5. Allocation Management");
            System.out.println("6. Complaint Management");
            System.out.println("7. Fee Management");
            System.out.println("0. Exit");

            System.out.print("Enter Choice: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> hostelMenu(hostelService, sf);
                case 2 -> roomMenu(roomService, sf);
                case 3 -> residenceMenu(residenceService, sf);
                case 4 -> wardenMenu(wardenService, sf);
                case 5 -> allocationMenu(allocationService, sf);
                case 6 -> complaintMenu(complaintService, sf);
                case 7 -> feeMenu(feeService, sf);
                case 0 -> {
                    System.out.println("Thank You!");
                    sf.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid Choice!");
            }
        }
    }

    private static void hostelMenu(HostelService service, SessionFactory sf) {
        while (true) {
            System.out.println("\n===== HOSTEL MENU =====");
            System.out.println("1. Add Hostel");
            System.out.println("2. View Hostel");
            System.out.println("3. View All Hostels");
            System.out.println("4. Update Hostel");
            System.out.println("5. Delete Hostel");
            System.out.println("0. Back");

            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> service.saveHostel(sf);
                case 2 -> service.getHostel(sf);
                case 3 -> service.getAllHostels(sf);
                case 4 -> service.updateHostel(sf);
                case 5 -> service.deleteHostel(sf);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid Choice!");
            }
        }
    }

    private static void roomMenu(RoomService service, SessionFactory sf) {
        while (true) {
            System.out.println("\n===== ROOM MENU =====");
            System.out.println("1. Add Room");
            System.out.println("2. View Room");
            System.out.println("3. View All Rooms");
            System.out.println("4. Update Room");
            System.out.println("5. Delete Room");
            System.out.println("0. Back");

            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> service.saveRoom(sf);
                case 2 -> service.getRoom(sf);
                case 3 -> service.getAllRooms(sf);
                case 4 -> service.updateRoom(sf);
                case 5 -> service.deleteRoom(sf);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid Choice!");
            }
        }
    }

    private static void residenceMenu(ResidenceService service, SessionFactory sf) {
        while (true) {
            System.out.println("\n===== RESIDENCE MENU =====");
            System.out.println("1. Add Residence");
            System.out.println("2. View Residence");
            System.out.println("3. View All Residences");
            System.out.println("4. Update Residence");
            System.out.println("5. Delete Residence");
            System.out.println("0. Back");

            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> service.saveResidence(sf);
                case 2 -> service.getResidence(sf);
                case 3 -> service.getAllResidences(sf);
                case 4 -> service.updateResidence(sf);
                case 5 -> service.deleteResidence(sf);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid Choice!");
            }
        }
    }

    private static void wardenMenu(WardenService service, SessionFactory sf) {
        while (true) {
            System.out.println("\n===== WARDEN MENU =====");
            System.out.println("1. Add Warden");
            System.out.println("2. View Warden");
            System.out.println("3. View All Wardens");
            System.out.println("4. Update Warden");
            System.out.println("5. Delete Warden");
            System.out.println("0. Back");

            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> service.saveWarden(sf);
                case 2 -> service.getWarden(sf);
                case 3 -> service.getAllWardens(sf);
                case 4 -> service.updateWarden(sf);
                case 5 -> service.deleteWarden(sf);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid Choice!");
            }
        }
    }

    private static void allocationMenu(AllocationService service, SessionFactory sf) {
        while (true) {
            System.out.println("\n===== ALLOCATION MENU =====");
            System.out.println("1. Add Allocation");
            System.out.println("2. View Allocation");
            System.out.println("3. View All Allocations");
            System.out.println("4. Update Allocation");
            System.out.println("5. Delete Allocation");
            System.out.println("0. Back");

            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> service.saveAllocation(sf);
                case 2 -> service.getAllocation(sf);
                case 3 -> service.getAllAllocations(sf);
                case 4 -> service.updateAllocation(sf);
                case 5 -> service.deleteAllocation(sf);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid Choice!");
            }
        }
    }

    private static void complaintMenu(ComplaintService service, SessionFactory sf) {
        while (true) {
            System.out.println("\n===== COMPLAINT MENU =====");
            System.out.println("1. Add Complaint");
            System.out.println("2. View Complaint");
            System.out.println("3. View All Complaints");
            System.out.println("4. Update Complaint");
            System.out.println("5. Delete Complaint");
            System.out.println("0. Back");

            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> service.saveComplaint(sf);
                case 2 -> service.getComplaint(sf);
                case 3 -> service.getAllComplaints(sf);
                case 4 -> service.updateComplaint(sf);
                case 5 -> service.deleteComplaint(sf);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid Choice!");
            }
        }
    }

    private static void feeMenu(FeeService service, SessionFactory sf) {
        while (true) {
            System.out.println("\n===== FEE MENU =====");
            System.out.println("1. Add Fee");
            System.out.println("2. View Fee");
            System.out.println("3. View All Fees");
            System.out.println("4. Update Fee");
            System.out.println("5. Delete Fee");
            System.out.println("0. Back");

            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> service.saveFee(sf);
                case 2 -> service.getFee(sf);
                case 3 -> service.getAllFees(sf);
                case 4 -> service.updateFee(sf);
                case 5 -> service.deleteFee(sf);
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid Choice!");
            }
        }
    }
}
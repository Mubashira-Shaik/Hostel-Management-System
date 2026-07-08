// FILE 9: com/impl/AllocationServiceImpl.java
// ============================================================
// SERVICE LAYER CHANGES — AllocationServiceImpl:
//   - saveAllocation(): load WomenResidence, Room, Hostel objects from DB
//     then set them on Allocation instead of primitive IDs
//   - deleteAllocation(): get room via a.getRoom() instead of a.getRoomId()
//   - getRoom() helper: query uses room.hostel.hostelId instead of room.hostelId
//   - getCount() helper: query uses a.room.roomId instead of a.roomId
//   - printAllocation(): updated to use object references
// ============================================================
 
package com.impl;
 
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.hostelEntity.Allocation;
import com.hostelEntity.Hostel;
import com.hostelEntity.Residence;
import com.hostelEntity.Room;
import com.services.AllocationService;
 
public class AllocationServiceImpl implements AllocationService {
 
    private static final Scanner sc = new Scanner(System.in);
 
    // ================= CREATE =================
    @Override
    public void saveAllocation(SessionFactory sf) {
 
        Transaction tx = null;
 
        try (Session session = sf.openSession()) {
 
            System.out.print("Enter Student ID: ");
            int Id = Integer.parseInt(sc.nextLine());
 
            // Load full WomenResidence object from DB
            Residence wr = session.find(Residence.class, Id);
            if (wr == null) {
                System.out.println("Resident not found!");
                return;
            }
 
            System.out.print("Enter Room Number: ");
            String rn = sc.nextLine();
 
            System.out.print("Enter Hostel ID: ");
            int hid = Integer.parseInt(sc.nextLine());
 
            // Load full Hostel object
            Hostel hostel = session.find(Hostel.class, hid);
            if (hostel == null) {
                System.out.println("Hostel not found!");
                return;
            }
 
            // Load Room using room number + hostel ID (updated query)
            Room room = getRoomByNumberAndHostel(session, rn, hid);
            if (room == null) {
                System.out.println("Room not found!");
                return;
            }
 
            long count = getCount(session, room.getRoomId());
            if (count >= room.getCapacity()) {
                System.out.println("Room FULL!");
                return;
            }
 
            System.out.print("Enter Join Date (yyyy-MM-dd): ");
            Date join = Date.valueOf(sc.nextLine());
 
            System.out.print("Enter Leave Date (yyyy-MM-dd): ");
            Date leave = Date.valueOf(sc.nextLine());
 
            tx = session.beginTransaction();
 
            Allocation a = new Allocation();
            a.setResidence(wr);       // CHANGED from setStudentId()
            a.setRoom(room);               // CHANGED from setRoomId()
            a.setHostel(hostel);           // CHANGED from setHostelName()
            a.setDateOfJoin(join);
            a.setDateOfLeave(leave);
 
            session.persist(a);
 
            // Capacity check after persist
            count = getCount(session, room.getRoomId());
            if (count >= room.getCapacity()) {
                room.setStatus("occupied");
            } else {
                room.setStatus("available");
            }
            session.merge(room);
            tx.commit();
 
            System.out.println("Allocation Done! ID: " + a.getAllocationId());
 
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println(e.getMessage());
        }
    }
 
    // ================= VIEW BY ID =================
    @Override
    public void getAllocation(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            System.out.print("Enter Allocation ID: ");
            int id = Integer.parseInt(sc.nextLine());
            Allocation a = session.find(Allocation.class, id);
            if (a == null) {
                System.out.println("Not found!");
                return;
            }
            printAllocation(a);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
 
    // ================= VIEW ALL =================
    @Override
    public void getAllAllocations(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            List<Allocation> list = session
                    .createQuery("FROM Allocation", Allocation.class)
                    .getResultList();
            if (list.isEmpty()) {
                System.out.println("No Allocations Found!");
                return;
            }
            System.out.println("\n===== ALLOCATIONS =====");
            for (Allocation a : list) {
                printAllocation(a);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
 
    // ================= UPDATE =================
    @Override
    public void updateAllocation(SessionFactory sf) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            System.out.print("Enter Allocation ID: ");
            int id = Integer.parseInt(sc.nextLine());
            Allocation a = session.find(Allocation.class, id);
            if (a == null) {
                System.out.println("Not found!");
                return;
            }
            System.out.print("Enter New Leave Date (yyyy-MM-dd): ");
            Date leave = Date.valueOf(sc.nextLine());
            tx = session.beginTransaction();
            a.setDateOfLeave(leave);
            session.merge(a);
            tx.commit();
            System.out.println("Updated Successfully!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println(e.getMessage());
        }
    }
 
    // ================= DELETE =================
    @Override
    public void deleteAllocation(SessionFactory sf) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            System.out.print("Enter Allocation ID: ");
            int id = Integer.parseInt(sc.nextLine());
            Allocation a = session.find(Allocation.class, id);
            if (a == null) {
                System.out.println("Not found!");
                return;
            }
            tx = session.beginTransaction();
 
            Room room = a.getRoom();   // CHANGED from session.find(Room, a.getRoomId())
 
            session.remove(a);
 
            long count = getCount(session, room.getRoomId());
            if (count < room.getCapacity()) {
                room.setStatus("available");
            }
            session.merge(room);
            tx.commit();
            System.out.println("Deleted Successfully!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println(e.getMessage());
        }
    }
 
    // ================= HELPERS =================
 
    // CHANGED: query now uses room.hostel.hostelId (object path) not room.hostelId
    private Room getRoomByNumberAndHostel(Session s, String rn, int hid) {
        List<Room> list = s.createQuery(
                "FROM Room WHERE roomNumber = :rn AND hostel.hostelId = :hid", Room.class)
                .setParameter("rn", rn)
                .setParameter("hid", hid)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
 
    // CHANGED: query uses a.room.roomId (object path) not a.roomId
    private long getCount(Session s, int id) {
        return s.createQuery(
                "SELECT COUNT(a) FROM Allocation a WHERE a.room.roomId = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
    }
 
    private void printAllocation(Allocation a) {
        System.out.println("----------------------------");
        System.out.println("ID     : " + a.getAllocationId());
        System.out.println("Student: " + (a.getResidence() != null ? a.getResidence().getName() : "N/A"));
        System.out.println("Room   : " + (a.getRoom() != null ? a.getRoom().getRoomNumber() : "N/A"));
        System.out.println("Hostel : " + (a.getHostel() != null ? a.getHostel().getHostelName() : "N/A"));
        System.out.println("Join   : " + a.getDateOfJoin());
        System.out.println("Leave  : " + a.getDateOfLeave());
    }
}
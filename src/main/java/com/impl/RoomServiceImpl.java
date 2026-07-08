package com.impl;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.hostelEntity.Hostel;
import com.hostelEntity.Room;
import com.services.RoomService;

public class RoomServiceImpl implements RoomService {

    private static final Scanner sc = new Scanner(System.in);

    @Override
    public void saveRoom(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            Room room = new Room();

            System.out.print("Enter Room Number: ");
            room.setRoomNumber(sc.nextLine().trim());

            System.out.println("Select Room Type:");
            System.out.println("1. Single - Rs.8000");
            System.out.println("2. Double - Rs.6000");
            System.out.println("3. Triple - Rs.4500");
            System.out.print("Choose: ");

            int type = Integer.parseInt(sc.nextLine().trim());

            switch (type) {
                case 1:
                    room.setRoomType("Single");
                    room.setCapacity(1);
                    break;
                case 2:
                    room.setRoomType("Double");
                    room.setCapacity(2);
                    break;
                case 3:
                    room.setRoomType("Triple");
                    room.setCapacity(3);
                    break;
                default:
                    System.out.println("Invalid room type.");
                    tx.rollback();
                    return;
            }

            room.setStatus("available");

            System.out.print("Enter Hostel ID: ");
            int hostelId = Integer.parseInt(sc.nextLine().trim());

            Hostel hostel = session.find(Hostel.class, hostelId);

            if (hostel == null) {
                System.out.println("Hostel not found. Add hostel first.");
                tx.rollback();
                return;
            }

            Room existingRoom = session.createQuery(
                    "FROM Room r WHERE r.roomNumber = :roomNumber AND r.hostel.hostelId = :hostelId",
                    Room.class)
                    .setParameter("roomNumber", room.getRoomNumber())
                    .setParameter("hostelId", hostelId)
                    .uniqueResult();

            if (existingRoom != null) {
                System.out.println("Room Number '" + room.getRoomNumber()
                        + "' already exists in this hostel.");
                tx.rollback();
                return;
            }

            room.setHostel(hostel);

            session.persist(room);
            tx.commit();

            System.out.println("Room Added Successfully!");
            System.out.println("Room ID   : " + room.getRoomId());
            System.out.println("Room Type : " + room.getRoomType());
            System.out.println("Capacity  : " + room.getCapacity());
            System.out.println("Fixed Fee : Rs." + room.getFixedFee());

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error saving room: " + e.getMessage());
        }
    }

    @Override
    public void getRoom(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            System.out.print("Enter Room ID: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Room room = session.find(Room.class, id);

            if (room != null) {
                printRoom(room);
            } else {
                System.out.println("Room not found for ID: " + id);
            }

        } catch (Exception e) {
            System.out.println("Error getting room: " + e.getMessage());
        }
    }

    @Override
    public void getAllRooms(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            List<Room> list = session
                    .createQuery("FROM Room", Room.class)
                    .getResultList();

            if (list.isEmpty()) {
                System.out.println("No rooms found.");
                return;
            }

            System.out.println("\n--- ALL ROOMS ---");
            System.out.printf("%-8s %-15s %-12s %-10s %-12s %-20s %-10s%n",
                    "ID", "Room No", "Type", "Capacity", "Status", "Hostel", "Fee");

            System.out.println("--------------------------------------------------------------------------------");

            for (Room r : list) {
                String hostelName = r.getHostel() != null
                        ? r.getHostel().getHostelName()
                        : "N/A";

                System.out.printf("%-8d %-15s %-12s %-10d %-12s %-20s Rs.%-10.0f%n",
                        r.getRoomId(),
                        r.getRoomNumber(),
                        r.getRoomType(),
                        r.getCapacity(),
                        r.getStatus(),
                        hostelName,
                        r.getFixedFee());
            }

        } catch (Exception e) {
            System.out.println("Error getting rooms: " + e.getMessage());
        }
    }

    @Override
    public void updateRoom(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            System.out.print("Enter Room ID to Update: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Room room = session.find(Room.class, id);

            if (room == null) {
                System.out.println("Room not found.");
                tx.rollback();
                return;
            }

            System.out.println("1. Update Room Number");
            System.out.println("2. Update Room Type");
            System.out.println("3. Update Status");
            System.out.print("Choose: ");

            int ch = Integer.parseInt(sc.nextLine().trim());

            switch (ch) {
                case 1:
                    System.out.print("Enter New Room Number: ");
                    String newRoomNo = sc.nextLine().trim();

                    Room existing = session.createQuery(
                            "FROM Room r WHERE r.roomNumber = :roomNumber " +
                                    "AND r.hostel.hostelId = :hostelId " +
                                    "AND r.roomId <> :roomId",
                            Room.class)
                            .setParameter("roomNumber", newRoomNo)
                            .setParameter("hostelId", room.getHostel().getHostelId())
                            .setParameter("roomId", room.getRoomId())
                            .uniqueResult();

                    if (existing != null) {
                        System.out.println("Room Number '" + newRoomNo
                                + "' already exists in this hostel.");
                        tx.rollback();
                        return;
                    }

                    room.setRoomNumber(newRoomNo);
                    break;

                case 2:
                    System.out.println("1. Single - Rs.8000");
                    System.out.println("2. Double - Rs.6000");
                    System.out.println("3. Triple - Rs.4500");
                    System.out.print("Choose New Type: ");

                    int type = Integer.parseInt(sc.nextLine().trim());

                    if (type == 1) {
                        room.setRoomType("Single");
                        room.setCapacity(1);
                    } else if (type == 2) {
                        room.setRoomType("Double");
                        room.setCapacity(2);
                    } else if (type == 3) {
                        room.setRoomType("Triple");
                        room.setCapacity(3);
                    } else {
                        System.out.println("Invalid room type.");
                        tx.rollback();
                        return;
                    }
                    break;

                case 3:
                    System.out.println("1. available");
                    System.out.println("2. occupied");
                    System.out.print("Choose Status: ");

                    int status = Integer.parseInt(sc.nextLine().trim());

                    if (status == 1) {
                        room.setStatus("available");
                    } else if (status == 2) {
                        room.setStatus("occupied");
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

            session.merge(room);
            tx.commit();

            System.out.println("Room Updated Successfully!");
            System.out.println("Updated Fee: Rs." + room.getFixedFee());

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error updating room: " + e.getMessage());
        }
    }

    @Override
    public void deleteRoom(SessionFactory sf) {
        Transaction tx = null;

        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();

            System.out.print("Enter Room ID to Delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Room room = session.find(Room.class, id);

            if (room == null) {
                System.out.println("Room not found.");
                tx.rollback();
                return;
            }

            session.remove(room);
            tx.commit();

            System.out.println("Room Deleted Successfully!");

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error deleting room: " + e.getMessage());
        }
    }

    private void printRoom(Room r) {
        System.out.println("\n========== ROOM DETAILS ==========");
        System.out.println("Room ID      : " + r.getRoomId());
        System.out.println("Room Number  : " + r.getRoomNumber());
        System.out.println("Room Type    : " + r.getRoomType());
        System.out.println("Capacity     : " + r.getCapacity());
        System.out.println("Status       : " + r.getStatus());

        if (r.getHostel() != null) {
            System.out.println("Hostel ID    : " + r.getHostel().getHostelId());
            System.out.println("Hostel Name  : " + r.getHostel().getHostelName());
        } else {
            System.out.println("Hostel       : N/A");
        }

        System.out.println("Fixed Fee    : Rs." + r.getFixedFee());
        System.out.println("==================================");
    }
}
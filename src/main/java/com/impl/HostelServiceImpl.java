package com.impl;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.hostelEntity.Hostel;
import com.services.HostelService;

public class HostelServiceImpl implements HostelService {

    private static final Scanner sc = new Scanner(System.in);

    // ================= SAVE HOSTEL =================

    @Override
    public void saveHostel(SessionFactory sf) {

        Transaction tx = null;

        try (Session session = sf.openSession()) {

            Hostel hostel = new Hostel();

            // Hostel Name
            String hostelName;
            while (true) {
                System.out.print("Enter Hostel Name: ");
                hostelName = sc.nextLine().trim();

                if (hostelName.isEmpty()) {
                    System.out.println("❌ Hostel Name cannot be empty.");
                } else {
                    break;
                }
            }

            // Location
            String location;
            while (true) {
                System.out.print("Enter Location: ");
                location = sc.nextLine().trim();

                if (location.isEmpty()) {
                    System.out.println("❌ Location cannot be empty.");
                } else {
                    break;
                }
            }

            // Hostel Type
            String hostelType;
            while (true) {
                System.out.print("Enter Hostel Type (Male/Female): ");
                hostelType = sc.nextLine().trim();

                if (hostelType.equalsIgnoreCase("Male") ||
                    hostelType.equalsIgnoreCase("Female")) {
                    break;
                } else {
                    System.out.println("❌ Enter only Male or Female.");
                }
            }

            hostel.setHostelName(hostelName);
            hostel.setLocation(location);
            hostel.setHostelType(hostelType);

            tx = session.beginTransaction();

            session.persist(hostel);

            tx.commit();

            System.out.println("\n✅ Hostel Saved Successfully!");
            System.out.println("Generated Hostel ID : " + hostel.getHostelId());

        } catch (Exception e) {

            if (tx != null)
                tx.rollback();

            System.out.println("❌ Error Saving Hostel");
            e.printStackTrace();
        }
    }

    // ================= VIEW SINGLE HOSTEL =================

    @Override
    public void getHostel(SessionFactory sf) {

        try (Session session = sf.openSession()) {

            System.out.print("Enter Hostel ID : ");
            int id = Integer.parseInt(sc.nextLine().trim());

            Hostel hostel = session.find(Hostel.class, id);

            if (hostel != null) {

                printHostel(hostel);

            } else {

                System.out.println("❌ Hostel Not Found.");
            }

        } catch (NumberFormatException e) {

            System.out.println("❌ Invalid ID.");

        } catch (Exception e) {

            System.out.println("Error : " + e.getMessage());
        }
    }

    // ================= VIEW ALL HOSTELS =================

    @Override
    public void getAllHostels(SessionFactory sf) {

        try (Session session = sf.openSession()) {

            List<Hostel> list = session
                    .createQuery("from Hostel", Hostel.class)
                    .getResultList();

            if (list.isEmpty()) {

                System.out.println("No Hostels Available.");

                return;
            }

            System.out.println("\n==================== HOSTEL LIST ====================");

            System.out.printf("%-10s %-30s %-25s %-15s%n",
                    "ID",
                    "HOSTEL NAME",
                    "LOCATION",
                    "TYPE");

            System.out.println("---------------------------------------------------------------");

            for (Hostel h : list) {

                System.out.printf("%-10d %-30s %-25s %-15s%n",
                        h.getHostelId(),
                        h.getHostelName(),
                        h.getLocation(),
                        h.getHostelType());
            }

        } catch (Exception e) {

            System.out.println("Error : " + e.getMessage());
        }
    }

    // ================= UPDATE HOSTEL =================

    @Override
    public void updateHostel(SessionFactory sf) {

        Transaction tx = null;

        try (Session session = sf.openSession()) {

            System.out.print("Enter Hostel ID to Update : ");

            int id = Integer.parseInt(sc.nextLine().trim());

            Hostel hostel = session.find(Hostel.class, id);

            if (hostel == null) {

                System.out.println("❌ Hostel Not Found.");

                return;
            }

            tx = session.beginTransaction();

            System.out.print("Enter New Hostel Name (Press Enter to Skip): ");

            String name = sc.nextLine().trim();

            if (!name.isEmpty())
                hostel.setHostelName(name);

            System.out.print("Enter New Location (Press Enter to Skip): ");

            String location = sc.nextLine().trim();

            if (!location.isEmpty())
                hostel.setLocation(location);

            System.out.print("Enter New Hostel Type (Male/Female or Press Enter): ");

            String type = sc.nextLine().trim();

            if (!type.isEmpty()) {

                if (type.equalsIgnoreCase("Male") ||
                        type.equalsIgnoreCase("Female")) {

                    hostel.setHostelType(type);

                } else {

                    System.out.println("Invalid Hostel Type.");
                }
            }

            session.merge(hostel);

            tx.commit();

            System.out.println("✅ Hostel Updated Successfully.");

        } catch (NumberFormatException e) {

            System.out.println("Invalid Hostel ID.");

            if (tx != null)
                tx.rollback();

        } catch (Exception e) {

            if (tx != null)
                tx.rollback();

            System.out.println("Error : " + e.getMessage());
        }
    }

    // ================= DELETE HOSTEL =================

    @Override
    public void deleteHostel(SessionFactory sf) {

        Transaction tx = null;

        try (Session session = sf.openSession()) {

            System.out.print("Enter Hostel ID to Delete : ");

            int id = Integer.parseInt(sc.nextLine().trim());

            Hostel hostel = session.find(Hostel.class, id);

            if (hostel == null) {

                System.out.println("❌ Hostel Not Found.");

                return;
            }

            tx = session.beginTransaction();

            session.remove(hostel);

            tx.commit();

            System.out.println("✅ Hostel Deleted Successfully.");

        } catch (NumberFormatException e) {

            System.out.println("Invalid Hostel ID.");

        } catch (Exception e) {

            if (tx != null)
                tx.rollback();

            System.out.println("Error : " + e.getMessage());
        }
    }

    // ================= PRINT HOSTEL =================

    private void printHostel(Hostel h) {

        System.out.println("\n============== HOSTEL DETAILS ==============");

        System.out.println("Hostel ID    : " + h.getHostelId());
        System.out.println("Hostel Name  : " + h.getHostelName());
        System.out.println("Location     : " + h.getLocation());
        System.out.println("Hostel Type  : " + h.getHostelType());

        System.out.println("============================================");
    }
}
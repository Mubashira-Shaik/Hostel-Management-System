package com.hostelEntity;
 
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hostel")
public class Hostel {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hostel_id")
    private int hostelId;
 
    @Column(name = "hostel_name", nullable = false)
    private String hostelName;
 
    @Column(name = "location", nullable = false)
    private String location;
 
    // ── One Hostel → Many Rooms ──────────────────────────────
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude   // Prevents Lombok infinite recursion in toString()
    private List<Room> rooms;
 
    // ── One Hostel → Many Wardens ────────────────────────────
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Warden> wardens;
 
    // ── One Hostel → Many Residents ──────────────────────────
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Residence> residents;
 
    // ── One Hostel → Many Complaints ─────────────────────────
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Complaint> complaints;
 
    // ── One Hostel → Many Fee Records ────────────────────────
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Fee> fees;
 
    // ── One Hostel → Many Allocations ────────────────────────
    @OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Allocation> allocations;
    
    @Column(name = "hostel_type", nullable = false)
    private String hostelType; 
    // values: Male / Female
}
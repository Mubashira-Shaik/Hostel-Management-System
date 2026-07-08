package com.hostelEntity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "residence")
public class Residence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "contact", nullable = false, length = 15)
    private String contact;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "dob")
    private String dob;

    @Column(name = "age")
    private int age;

    @Column(name = "aadhaar", length = 12)
    private String aadhaar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_id", nullable = false)
    @ToString.Exclude
    private Hostel hostel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @ToString.Exclude
    private Room room;

    @OneToMany(mappedBy = "residence", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Fee> fees;

    @OneToMany(mappedBy = "residence", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Complaint> complaints;

    @OneToMany(mappedBy = "residence", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Allocation> allocations;
}
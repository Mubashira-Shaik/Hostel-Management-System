package com.hostelEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warden")
public class Warden {
 
    @Id
    @Column(name = "warden_id")
    private int wardenId;
 
    @Column(name = "name", nullable = false)
    private String name;
 
    @Column(name = "contact", nullable = false, length = 15)
    private String contact;
 
    // ── Many Wardens → One Hostel ────────────────────────────
    // REPLACES: private int hostelId  +  stub getHostel()/setHostel()
    // FK COLUMN: hostel_id in warden table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostel_id", nullable = false)
    @ToString.Exclude
    private Hostel hostel;
}
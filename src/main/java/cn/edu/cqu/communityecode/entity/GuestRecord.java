package cn.edu.cqu.communityecode.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "guest_record")
public class GuestRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "time")
    @CurrentTimestamp
    private LocalDateTime time;
    @Column(name = "entrance")
    private int entrance;
    @Column(name = "guest_id")
    private int guestId;
    @Column(name = "owner_id")
    private int ownerId;
}

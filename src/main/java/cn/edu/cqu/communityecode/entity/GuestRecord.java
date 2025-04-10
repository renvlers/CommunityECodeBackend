package cn.edu.cqu.communityecode.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "guest_record")
public class GuestRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "enter_time")
    private LocalDateTime enterTime;

    @Column(name = "leave_time")
    private LocalDateTime leaveTime;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "guest_phone")
    private String guestPhone;

    @Column(name = "entrance")
    private int entrance;

    @Column(name = "owner_id")
    private int ownerId;
}

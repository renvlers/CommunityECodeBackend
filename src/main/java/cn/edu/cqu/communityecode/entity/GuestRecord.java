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
    private Integer id;

    @Column(name = "enter_time")
    private LocalDateTime enterTime;

    @Column(name = "leave_time")
    private LocalDateTime leaveTime;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "guest_phone")
    private String guestPhone;

    @Column(name = "entrance")
    private Integer entrance;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "request_code")
    private String requestCode;

    @Column(name = "hash")
    private String hash;

    @Column(name = "status")
    private Integer status;
}

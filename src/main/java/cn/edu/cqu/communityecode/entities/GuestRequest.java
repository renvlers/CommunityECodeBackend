package cn.edu.cqu.communityecode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "guest_request")
public class GuestRequest {
    @Id
    @Column(name = "request_code")
    private String requestCode;

    @Column(name = "enter_time")
    private LocalDateTime enterTime;

    @Column(name = "leave_time")
    private LocalDateTime leaveTime;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "guest_phone")
    private String guestPhone;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "hash")
    private String hash;
}

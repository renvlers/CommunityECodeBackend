package cn.edu.cqu.communityecode.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "guest_request")
public class GuestRequest {
    @Id
    @Column(name = "request_code")
    private String requestCode;
    @Column(name = "time")
    @CurrentTimestamp
    private LocalDateTime time;
    @Column(name = "guest_id")
    private int guestId;
    @Column(name = "owner_id")
    private int ownerId;
    @Column(name = "hash")
    private String hash;
}

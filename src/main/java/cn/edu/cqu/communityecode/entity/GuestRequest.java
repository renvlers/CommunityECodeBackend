package cn.edu.cqu.communityecode.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class GuestRequest {
    @Id
    private String requestCode;
    private LocalDateTime time;
    private int guestId;
    private int ownerId;
    private String hash;
}

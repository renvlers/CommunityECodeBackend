package cn.edu.cqu.communityecode.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class GuestRecord {
    @Id
    private int id;
    private LocalDateTime time;
    private int entrance;
    private int guestId;
    private int ownerId;
}

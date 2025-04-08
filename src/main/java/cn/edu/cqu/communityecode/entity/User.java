package cn.edu.cqu.communityecode.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    private int uid;
    private String phone;
    private String username;
    private String password;
    private String avatar;
    private LocalDateTime registerDate;
    private int permissionId;
}

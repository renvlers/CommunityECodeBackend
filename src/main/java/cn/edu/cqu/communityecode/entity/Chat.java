package cn.edu.cqu.communityecode.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Chat {
    @Id
    private int id;
    private int from;
    private int to;
    private String message;
    private String type;
    private LocalDateTime time;
}

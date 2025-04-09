package cn.edu.cqu.communityecode.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "from")
    private int from;
    @Column(name = "to")
    private int to;
    @Column(name = "message")
    private String message;
    @Column(name = "type")
    private String type;
    @Column(name = "time")
    @CurrentTimestamp
    private LocalDateTime time;
}

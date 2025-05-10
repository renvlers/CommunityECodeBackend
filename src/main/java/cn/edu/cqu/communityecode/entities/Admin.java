package cn.edu.cqu.communityecode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @Column(name = "aid")
    private int aid;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;
}

package cn.edu.cqu.communityecode.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Permission {
    @Id
    private int id;
    private String name;
}

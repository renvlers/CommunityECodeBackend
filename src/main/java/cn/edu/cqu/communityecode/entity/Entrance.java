package cn.edu.cqu.communityecode.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "entrance")
public class Entrance {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;
}

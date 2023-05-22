package com.dss.springboot.backend.apirest.models.entity;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "regions")
public class Region implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

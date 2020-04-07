package com.micro.usuarios.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="foo")
public class Foo implements Serializable {

    private static final long serialVersionUID = -5291566604023416394L;

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

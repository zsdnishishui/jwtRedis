package com.zsd.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
@Data
public class User {

    @Id
    private Long id;

    private String username;

    private String password;

    private Integer age;
}

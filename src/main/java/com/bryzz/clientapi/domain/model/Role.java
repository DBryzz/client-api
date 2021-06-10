package com.bryzz.clientapi.domain.model;

import com.bryzz.clientapi.domain.constant.ERole;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(	name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 20)
    private ERole name;

    @Column(name = "role_description")
    private String description;

}

package com.bryzz.clientapi.domain.model;

import com.bryzz.clientapi.domain.constant.ERole;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(	name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 20)
    @NotBlank(message = "Role name cannot be empty")
    private ERole name;

    @Column(name = "role_description")
    @NotBlank(message = "Role description cannot be empty")
    private String description;

}

package com.bryzz.clientapi.domain.model;

import com.bryzz.clientapi.domain.constant.AppType;
import com.bryzz.clientapi.domain.constant.ERole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.query.criteria.internal.expression.function.CurrentTimestampFunction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "app_tbl"
        /*uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "user_email")
        }*/)
public class AppSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_id")
    private Long appId;

    @NotBlank
    @Column(name = "name")
    @Size(max = 20)
    private String appName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 20)
    @NotBlank(message = "Role name cannot be empty")
    private AppType appType;

    @NotBlank
    @Size(max = 50)
    @Column(name = "description")
    private String appDescription;

    @NotBlank
    @Column(name = "image_url")
    private String appCodeUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "app_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "app_id")
    )
    private List<User> users;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "app_deployer")
    private User deployer;


    @NotBlank
    private LocalDateTime createdDate;

    @NotBlank
    private LocalDateTime modifiedDate;



}

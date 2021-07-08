package com.bryzz.clientapi.domain.model;

import com.bryzz.clientapi.domain.constant.AppStatus;
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
    @Column(name = "app_type", length = 20)
    @NotBlank(message = "Type cannot be empty")
    private AppType appType;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_status", length = 20)
    @NotBlank(message = "Status cannot be empty")
    private AppStatus appStatus;

    @NotBlank
    @Size(max = 50)
    @Column(name = "description")
    private String appDescription;

    @NotBlank
    @Column(name = "image_url")
    private String appCodeUrl;

    

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "app_uploader") //uploader
    private User uploader;


    @NotBlank
    private LocalDateTime createdDate;

    @NotBlank
    private LocalDateTime modifiedDate;



}

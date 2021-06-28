package com.bryzz.clientapi.domain.model;


import com.bryzz.clientapi.domain.constant.AppStatus;
import com.bryzz.clientapi.domain.constant.AppType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "image_tbl")
public class DockerImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Long imgId;

    @NotBlank
    @Column(name = "name")
    @Size(max = 20)
    private String imgName;

    @Column(name = "img_tag", length = 20)
    @NotBlank(message = "Tag cannot be empty")
    private String imgTag;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "img_source")
    private AppSource imgSourceCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "img_status", length = 20)
    @NotBlank(message = "Status cannot be empty")
    private AppStatus imageStatus;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "img_deployer")
    private User deployer;

    @NotBlank
    private LocalDateTime createdDate;

    @NotBlank
    private LocalDateTime modifiedDate;


}

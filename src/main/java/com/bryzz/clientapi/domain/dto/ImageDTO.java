package com.bryzz.clientapi.domain.dto;

import com.bryzz.clientapi.domain.constant.AppStatus;
import com.bryzz.clientapi.domain.model.AppSource;
import com.bryzz.clientapi.domain.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ImageDTO {

    @NotBlank(message = "img_id cannot be empty")
    private Long imgId;

    @NotBlank
    @Size(max = 20)
    private String imgName;

    @NotBlank(message = "Tag cannot be empty")
    private String imgTag;

    @JsonIgnore
    @JoinTable(name = "img_source")
    private AppSource imgSourceCode;

    private List<User> users;

    private User deployer;


    @JsonIgnore
    private AppStatus imageStatus;


    @NotBlank
    private LocalDateTime createdDate;

    @NotBlank
    private LocalDateTime modifiedDate;

}

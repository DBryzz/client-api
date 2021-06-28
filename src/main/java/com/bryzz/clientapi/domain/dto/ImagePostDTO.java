package com.bryzz.clientapi.domain.dto;

import com.bryzz.clientapi.domain.constant.AppStatus;
import com.bryzz.clientapi.domain.constant.AppType;
import com.bryzz.clientapi.domain.model.AppSource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ImagePostDTO {

    @NotBlank
    @Size(max = 20)
    private String imgName;

    @NotBlank(message = "Tag cannot be empty")
    private String imgTag;

    @JsonIgnore
    @JoinTable(name = "img_source")
    private AppSource imgSourceCode;

    @JsonIgnore
    private AppStatus imageStatus;

}

package com.bryzz.clientapi.domain.dto;

import com.bryzz.clientapi.domain.constant.AppStatus;
import com.bryzz.clientapi.domain.constant.AppType;
import com.bryzz.clientapi.domain.model.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppSourceDTO {


    @NotBlank(message = "app_id cannot be empty")
    private Long appId;

    @NotBlank(message = "AppName cannot be empty")
    @Size(max = 20, message = "maximum of 20 characters")
    private String appName;

    @NotBlank(message = "AppType cannot be empty")
    private AppType appType;

    @NotBlank(message = "AppStatus cannot be empty")
    private AppStatus appStatus;

    @NotBlank(message = "AppType cannot be empty")
    @Size(max = 50, message = "maximum of 50 characters")
    private String appDescription;

    @NotBlank(message = "url cannot be empty")
    private String appCodeUrl;
    

    private User uploader;


    @NotBlank
    private LocalDateTime createdDate;

    @NotBlank
    private LocalDateTime modifiedDate;

}

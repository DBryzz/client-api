package com.bryzz.clientapi.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class UserProfileDTO {
    @Column(name = "first_name")
    @Size(min = 3, max = 10, message = "must be at least 3 characters and at most 10 characters")
//    @NotBlank(message = "FirstName cannot be empty")
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 3, max = 10, message = "must be at least 3 characters and at most 10 characters")
//    @NotBlank(message = "FirstName cannot be empty")
    private String lastName;

    @Column(name = "sex")
    @Size(min = 1, max = 1, message = "M or F")
//    @NotBlank(message = "FirstName cannot be empty")
    private String sex;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
//    @NotBlank(message = "yyyy-mm-dd")
    private Date birthday;
}

package com.bryzz.clientapi.domain.dto;

import com.bryzz.clientapi.domain.model.Role;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserPostDTO {

    @NotBlank(message = "Username cannot be empty - Add username")
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "Email cannot be empty - Add email address")
    @Size(max = 50)
    @Email(message = "Add a valid email address")
    private String email;

    @NotBlank(message = "Add a user role - USER or DEPLOYER")
    private Set<String> roles = new HashSet<>();

    @NotBlank(message = "Password cannot be empty - Add a password")
    @Size(max = 120)
    private String password;




}

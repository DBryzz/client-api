package com.bryzz.clientapi.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserLoginDTO {

    @NotBlank(message = "Username cannot be empty - Add username")
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "Password cannot be empty - Add a password")
    @Size(max = 120)
    private String password;

}

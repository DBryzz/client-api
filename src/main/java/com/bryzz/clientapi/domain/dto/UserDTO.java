package com.bryzz.clientapi.domain.dto;


import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String roles;

    private List<String> sRoles;
    private String accessToken;

}
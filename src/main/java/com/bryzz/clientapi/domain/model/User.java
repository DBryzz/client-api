package com.bryzz.clientapi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
/*@AllArgsConstructor
@NoArgsConstructor*/
@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    /*@JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long autoIncrement;*/

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "username")
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email(message = "Please enter a valid e-mail address")
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(max = 120)
    @Column(name = "passwd")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "first_name")
    @Size(min = 3, max = 10)
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 3, max = 10)
    private String lastName;

    @Column(name = "sex")
    private String sex;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(name = "dob")
    private Date birthday;

    /*@PrePersist
    private void prePersistUserId() {
        userId = Long.toString(getAutoIncrement());
    }*/

}

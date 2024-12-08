package com.alura.challenge.forohub.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity(name = "User")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String eMail;
    private String password;

    public User(DataRegisterUser dataRegisterUser) {
        this.username = dataRegisterUser.username();
        this.eMail = dataRegisterUser.eMail();
        this.password = dataRegisterUser.password();

    }
}

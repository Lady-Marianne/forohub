package com.alura.challenge.forohub.domain.user;

import com.alura.challenge.forohub.domain.answer.Answer;
import com.alura.challenge.forohub.domain.topic.Topic;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity(name = "User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "userId")
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    private String email;

    @Setter
    private String password;

    @Enumerated(EnumType.STRING) // Guardamos el rol como texto.
    @Column(nullable = false)
    protected Role role = Role.USER; // Valor por defecto es USER.

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Topic> topics = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    public User(DataRegisterUser dataRegisterUser) {
        this.username = dataRegisterUser.username();
        this.email = dataRegisterUser.email();
        this.password = dataRegisterUser.password();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "Id=" + userId +
//                ", Nombre='" + username + '\'' +
//                ", Correo electrónico='" + email + '\'' +
//                '}';
//    }

}

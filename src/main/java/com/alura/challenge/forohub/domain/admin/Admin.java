package com.alura.challenge.forohub.domain.admin;

import com.alura.challenge.forohub.domain.user.DataRegisterUser;
import com.alura.challenge.forohub.domain.user.Role;
import com.alura.challenge.forohub.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Table(name = "admins")
@Entity(name = "Admin")
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    public Admin(DataRegisterUser dataRegisterUser) {
        super(dataRegisterUser);
        this.role = Role.ADMIN; // Aseguramos que el rol sea ADMIN.
    }
}

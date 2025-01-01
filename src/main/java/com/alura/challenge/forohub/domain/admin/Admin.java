package com.alura.challenge.forohub.domain.admin;

import com.alura.challenge.forohub.domain.user.DataRegisterUser;
import com.alura.challenge.forohub.domain.user.User;
import com.alura.challenge.forohub.domain.user.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Table(name = "admins")
@Entity(name = "Admin")
@Getter
@RequiredArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "user_id") // Establece que 'user_id'
// en Admin es clave foránea para 'users'.
public class Admin extends User {

    public Admin(DataRegisterUser dataRegisterUser) {
        super(dataRegisterUser);
        this.role = Role.ADMIN; // Aseguramos que el rol sea ADMIN.
    }
}

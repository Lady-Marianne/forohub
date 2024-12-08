package com.alura.challenge.forohub.domain.user;

public record DataResponseUser(
        Long id,
        String username,
        String eMail
) {
    public DataResponseUser (User user) {
        this
                (
                        user.getId(),
                        user.getUsername(),
                        user.getEMail()

                );
    }

}

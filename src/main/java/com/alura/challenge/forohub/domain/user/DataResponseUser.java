package com.alura.challenge.forohub.domain.user;

public record DataResponseUser(
        Long id,
        String username,
        String email
) {
    public DataResponseUser (User user) {
        this
                (
                        user.getId(),
                        user.getUsername(),
                        user.getEmail()

                );
    }

}

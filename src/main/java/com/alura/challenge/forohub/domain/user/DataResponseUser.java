package com.alura.challenge.forohub.domain.user;

public record DataResponseUser(
        Long userId,
        String username,
        String email
) {
    public DataResponseUser (User user) {
        this
                (
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail()

                );
    }

}

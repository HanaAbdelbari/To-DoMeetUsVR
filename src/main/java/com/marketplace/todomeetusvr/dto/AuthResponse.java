package com.marketplace.todomeetusvr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String name;
    private String email;

    public static AuthResponse of(String token, String name, String email) {
        return AuthResponse.builder()
                .accessToken(token)
                .name(name)
                .email(email)
                .build();
    }
}
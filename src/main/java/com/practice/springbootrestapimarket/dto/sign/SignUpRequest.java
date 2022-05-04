package com.practice.springbootrestapimarket.dto.sign;

import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.member.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Data
@AllArgsConstructor
public class SignUpRequest {

    private String email;
    private String password;
    private String username;
    private String nickname;

    public static Member toEntity(SignUpRequest req, Role role, PasswordEncoder passwordEncoder) {

        return new Member(req.email, passwordEncoder.encode(req.password), req.username, req.nickname, Arrays.asList(role));
    }

}

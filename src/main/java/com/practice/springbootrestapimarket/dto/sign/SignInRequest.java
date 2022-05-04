package com.practice.springbootrestapimarket.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInRequest {
    String email;
    String password;
}

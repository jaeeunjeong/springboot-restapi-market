package com.practice.springbootrestapimarket.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class SignInRequest {

    @Email(message = "적절한 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀 번호를 입력해주세요.")
    private String password;

    public SignInRequest() {
    }
}

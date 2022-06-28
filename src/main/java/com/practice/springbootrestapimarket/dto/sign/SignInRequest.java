package com.practice.springbootrestapimarket.dto.sign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "로그인 요청")
@Data
@AllArgsConstructor
public class SignInRequest {

    @ApiModelProperty(value = "이메일", notes = "이메일을 입력해주세요", required = true, example = "honggildong@email.com")
    @Email(message = "적절한 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @ApiModelProperty(value = "비밀 번호", notes = "비밀 번호를 입력해주세요.", required = true, example = "password1!")
    @NotBlank(message = "비밀 번호를 입력해주세요.")
    private String password;

    public SignInRequest() {
    }
}

package com.practice.springbootrestapimarket.dto.sign;

import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.member.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;

@ApiModel(value = "회원 가입 요청")
@Data
@AllArgsConstructor
public class SignUpRequest {

    @ApiModelProperty(value = "이메일", notes = "이메일을 입력해주세요", required = true, example = "honggildong@email.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "적절한 이메일 형식이 아닙니다.")
    private String email;

    @ApiModelProperty(value = "비밀 번호", notes = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.", required = true, example = "password1!")
    @NotBlank(message = "비밀 번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;

    @ApiModelProperty(value = "이름", notes = "이름은 한글 또는 알파벳만 허용합니다.", required = true, example = "홍길동")
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, message = "최소 2글자 이상 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z가-힣]+$",
            message = "이름은 한글 또는 알파벳만 허용합니다.")
    private String username;

    @ApiModelProperty(value = "닉네임", notes = "닉네임은 한글 또는 알파벳만 허용합니다.", required = true, example = "길동역")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z가-힣]+$",
            message = "닉네임은 한글 또는 알파벳만 허용합니다.")
    @Size(min = 2, message = "최소 2글자 이상 입력해주세요.")
    private String nickname;

    public SignUpRequest() {
    }

    public static Member toEntity(SignUpRequest req, Role role, PasswordEncoder passwordEncoder) {
        return new Member(req.email, passwordEncoder.encode(req.password), req.username, req.nickname, Arrays.asList(role));
    }
}

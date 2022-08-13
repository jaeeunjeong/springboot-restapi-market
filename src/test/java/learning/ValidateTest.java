package learning;

import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateTest {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("제약조건에 대해 유효성 검사 ")
    @Test
    void test1() {
        // given
        Info info = new Info("abcd","jejeong000@gmail.com ");

        // when
        Set<ConstraintViolation<Info>> set = validator.validate(info);

        // then
        assertThat(set.size()).isEqualTo(1);
    }

    @DisplayName("제약조건에 대해 유효성 검사 - 이메일 조건 틀린 경우")
    @Test
    void test2() {
        // given
        Info info = new Info("abcd","jejeong000");

        // when
        Set<ConstraintViolation<Info>> set = validator.validate(info);

        // then
        assertEquals("올바른 형식의 이메일 주소여야 합니다", set.iterator().next().getMessage());
    }

    @DisplayName("제약조건에 대해 유효성 검사 - 이름의 글자수가 맞지 않는 경우")
    @Test
    void test3() {
        // given
        Info info = new Info("ab","jejeong000@gmail.com");

        // when
        Set<ConstraintViolation<Info>> set = validator.validate(info);

        // then
        assertEquals("길이가 3에서 10 사이여야 합니다", set.iterator().next().getMessage());
    }

    class Info {
        public Info(String name, String email) {
            this.name = name;
            this.email = email;
        }

        @NotNull
        @Length(min = 3, max = 10)
        private String name;

        @Email
        private String email;
    }
}

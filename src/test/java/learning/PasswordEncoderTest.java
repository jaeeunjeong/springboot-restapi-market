package learning;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    void bcrypt로_암호화_되었는지_확인(){
        // given
        String password = "PASSWORD";

        // when
        String encoderPassword = passwordEncoder.encode(password);

        // then
        Assertions.assertThat(encoderPassword).contains("bcrypt");
    }


    @Test
    void 암호화가_잘_되었는지_확인() {
        //given
        String password = "PASSWORD";
        String encodingPassword = passwordEncoder.encode(password);

        //when
        boolean isMatch = passwordEncoder.matches(password, encodingPassword);

        //then
        Assertions.assertThat(isMatch).isTrue();
    }
}

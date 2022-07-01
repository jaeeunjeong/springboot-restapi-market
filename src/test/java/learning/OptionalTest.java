package learning;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OptionalTest {

    @DisplayName("null 값이 떨어진 경우")
    @Test
    void test1() {
        // given, when
        Long result = Optional.ofNullable(null)
                .map(id -> Optional.ofNullable((Long) null).orElseThrow(RuntimeException::new))
                .orElse(5L);

        // then
        assertThat(result).isEqualTo(5L);
    }

    @DisplayName("null 값이 아닌 경우")
    @Test
    void test2() {
        // given, when
        assertThatThrownBy(
                () -> Optional.ofNullable(5L)
                        .map(id -> Optional.ofNullable((Long) null).orElseThrow(RuntimeException::new))
                        .orElse(1L))
                .isInstanceOf(RuntimeException.class);
    }
}

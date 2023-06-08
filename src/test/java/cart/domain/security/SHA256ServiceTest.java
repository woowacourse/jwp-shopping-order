package cart.domain.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SHA256ServiceTest {

    @Test
    @DisplayName("주어진 값을 SHA256으로 암호화한다.")
    void encrypt() {
        // given
        final String target = "password";

        // when
        final String encodedTarget = SHA256Service.encrypt(target);

        // then
        Assertions.assertThat(encodedTarget)
            .isEqualTo("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
    }
}

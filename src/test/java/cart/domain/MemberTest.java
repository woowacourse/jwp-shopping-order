package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void checkPasswordTrue() {
        final Member member = new Member(1L, "odo1@woowa.com", "1234");
        assertThat(member.checkPassword("1234")).isTrue();
    }

    @Test
    void checkPasswordFalse() {
        final Member member = new Member(1L, "odo1@woowa.com", "1234");
        assertThat(member.checkPassword("123")).isFalse();
    }

    @Test
    void checkPasswordFalseWhenNull() {
        final Member member = new Member(1L, "odo1@woowa.com", "1234");
        assertThat(member.checkPassword(null)).isFalse();
    }
}

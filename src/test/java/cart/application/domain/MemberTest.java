package cart.application.domain;

import cart.application.exception.ExceedOwnedPointException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("포인트를 추가할 수 있다")
    void addPoint() {
        // given
        Member member = new Member(0L, "a@a.com", "1234", 0);
        // when
        member.addPoint(100L);
        // then
        assertThat(member.getPoint()).isEqualTo(100L);
    }

    @Test
    @DisplayName("포인트를 사용할 수 있다")
    void usePoint() {
        // given
        Member member = new Member(0L, "a@a.com", "1234", 1000);
        // when
        member.usePoint(500);
        // then
        assertThat(member.getPoint()).isEqualTo(500L);
    }

    @Test
    @DisplayName("보유 포인트가 부족한 경우, 사용하려하면 예외가 발생한다")
    void usePoint_exception() {
        // given
        Member member = new Member(0L, "a@a.com", "1234", 0);
        // when, then
        assertThatThrownBy(() -> member.usePoint(500))
                .isInstanceOf(ExceedOwnedPointException.class);
    }

    @Test
    @DisplayName("비밀번호가 같은 경우를 확인할 수 있다")
    void hasPassword() {
        // given
        Member member = new Member(0L, "a@a.com", "1234", 0);
        // when, then
        assertThat(member.hasPassword("1234")).isTrue();
    }

    @Test
    @DisplayName("비밀번호가 다른 경우를 확인할 수 있다")
    void hasPassword_notSame() {
        // given
        Member member = new Member(0L, "a@a.com", "1234", 0);
        // when, then
        assertThat(member.hasPassword("4321")).isFalse();
    }
}

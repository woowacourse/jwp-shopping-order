package cart.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("첫 가입 시 기본 포인트는 5,000원을 갖는다.")
    @Test
    void initial_point() {
        Member member = new Member("test@email.com", "123");
        int point = member.getPoint().getValue();

        Assertions.assertThat(point).isEqualTo(5_000);
    }
}
package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("포인트를 사용하면 사용한만큼 사용자의 포인트가 삭감된다.")
    void usePoint() {
        //given
        final Member member = new Member(1L, "poi", "poiPassword", 3000);

        //when
        member.usePoint(300);

        //then
        assertThat(member.getPoint()).isEqualTo(2700);
    }
}

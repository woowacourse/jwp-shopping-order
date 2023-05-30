package cart.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void 모든_필드가_같으면_같은_Member이다() {
        // given
        Member member1 = new Member(1L, "email", "pw", 100);
        Member member2 = new Member(1L, "email", "pw", 100);

        // expect
        assertThat(member1).isEqualTo(member2);
    }
}

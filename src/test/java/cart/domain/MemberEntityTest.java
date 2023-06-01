package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MemberEntityTest {

    @Test
    void ID가_같으면_같은_객체이다() {
        // given
        Long id = 1L;
        MemberEntity memberEntity1 = new MemberEntity(id, "1", "1", 1);
        MemberEntity memberEntity2 = new MemberEntity(id, "2", "2", 2);

        // expect
        assertThat(memberEntity1).isEqualTo(memberEntity2);
    }
}

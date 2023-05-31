package cart.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SuppressWarnings("NonAsciiCharacters")
class MemberTest {
    @Test
    void 적립금_정상_추가() {
        // given
        final Member member = new Member(null, "aa", "1234", 1000L);
        
        // when
        member.usePoint(700L);
        
        // then
        assertThat(member.getPoint()).isEqualTo(300L);
    }
    
    @ParameterizedTest(name = "{displayName} : usedPoint = {0}")
    @ValueSource(longs = {-1L, 1001L})
    void 사용할_포인트가_보유한_적립금의_범위를_넘어서면_예외_처리(final Long usedPoint) {
        // given
        final Member member = new Member(null, "aa", "1234", 1000L);
        
        // expect
        assertThatIllegalArgumentException()
                .isThrownBy(() -> member.usePoint(usedPoint));
    }
}

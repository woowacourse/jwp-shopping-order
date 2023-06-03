package cart.member.application;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static cart.fixtures.MemberFixtures.Member_Dooly;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberDao memberDao;

    @Test
    void 캐시를_충전하다() {
        // given
        final Member member = Member.of(1L, "test@test.com", "1234", 5000L);
        when(memberDao.getMemberByEmail(member.getEmail())).thenReturn(member);
        doNothing().when(memberDao).updateMember(member);

        // when
        final Long totalCash = memberService.depositCash(member, 5000L);

        // then
        assertThat(totalCash).isEqualTo(10000L);
    }

    @Test
    void 캐시를_차감하다() {
        // given
        final Member member = Member.of(1L, "test@test.com", "1234", 5000L);
        when(memberDao.getMemberByEmail(member.getEmail())).thenReturn(member);
        doNothing().when(memberDao).updateMember(member);

        // when, then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertDoesNotThrow(() -> memberService.withdrawCash(member, 5000L));
            softAssertions.assertThat(member.getCash()).isEqualTo(0L);
        });
    }

    @Test
    void 현재_캐시를_확인하다() {
        // given
        when(memberDao.getMemberByEmail(Member_Dooly.EMAIL)).thenReturn(Member_Dooly.ENTITY);

        // when
        final Long totalCash = memberService.findCash(Member_Dooly.ENTITY);

        // then
        assertThat(totalCash).isEqualTo(Member_Dooly.CASH);
    }
}

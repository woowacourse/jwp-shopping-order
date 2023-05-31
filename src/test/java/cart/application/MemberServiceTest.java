package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.fixtures.MemberFixtures;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static cart.fixtures.MemberFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
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
    void 포인트를_충전하다() {
        // given
        final Member member = Member.of(1L, "test@test.com", "1234", 5000L);
        when(memberDao.getMemberByEmail(member.getEmail())).thenReturn(member);
        doNothing().when(memberDao).updateMember(member);

        // when
        final Long totalCash = memberService.depositPoint(member, 5000L);

        // then
        assertThat(totalCash).isEqualTo(10000L);
    }

    @Test
    void 포인트를_차감하다() {
        // given
        final Member member = Member.of(1L, "test@test.com", "1234", 5000L);
        when(memberDao.getMemberByEmail(member.getEmail())).thenReturn(member);
        doNothing().when(memberDao).updateMember(member);

        // when, then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertDoesNotThrow(() -> memberService.withdrawPoint(member, 5000L));
            softAssertions.assertThat(member.getCash()).isEqualTo(0L);
        });
    }
}

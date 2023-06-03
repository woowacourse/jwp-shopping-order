package cart.domain.member.application;

import static cart.fixtures.MemberFixtures.Dooly;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import cart.domain.member.application.dto.MemberCashChargeRequest;
import cart.domain.member.application.dto.MemberCashChargeResponse;
import cart.domain.member.application.dto.MemberShowCurrentCashResponse;
import cart.domain.member.domain.Member;
import cart.domain.member.persistence.MemberDao;
import cart.global.config.AuthMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("인증된 사용자와 충전할 금액을 받아서 충전된 사용자의 금액을 반환한다.")
    void chargeCash() {
        // given
        given(memberDao.selectMemberByEmail(Dooly.EMAIL)).willReturn(Dooly.ENTITY());
        willDoNothing().given(memberDao).updateMemberCash(any(Member.class));
        AuthMember authMember = new AuthMember(Dooly.EMAIL, Dooly.PASSWORD);
        MemberCashChargeRequest request = new MemberCashChargeRequest(10000);

        // when
        MemberCashChargeResponse memberCashChargeResponse = memberService.chargeCash(authMember, request);

        // then
        assertThat(memberCashChargeResponse.getTotalCash()).isEqualTo(15000);
    }

    @Test
    @DisplayName("인증된 사용자의 현재 금액을 반환한다.")
    void findMemberCurrentCharge() {
        // given
        given(memberDao.selectMemberByEmail(Dooly.EMAIL)).willReturn(Dooly.ENTITY());
        AuthMember authMember = new AuthMember(Dooly.EMAIL, Dooly.PASSWORD);

        // when
        MemberShowCurrentCashResponse response = memberService.findMemberCurrentCharge(authMember);

        // then
        assertThat(response.getTotalCash()).isEqualTo(5000);
    }
}

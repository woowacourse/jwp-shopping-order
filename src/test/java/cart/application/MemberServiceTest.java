package cart.application;

import static cart.fixture.TestFixture.MEMBER_A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cart.dao.MemberDao;
import cart.exception.NoSuchMemberException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private MemberService memberService;

    @Test
    void id로_멤버를_조회한다() {
        doReturn(Optional.of(MEMBER_A)).when(memberDao).getMemberById(eq(MEMBER_A.getId()));

        assertThat(memberService.getMemberBy(MEMBER_A.getId())).isEqualTo(MEMBER_A);
    }

    @Test
    void 멤버를_못찾으면_예외를_던진다() {
        doReturn(Optional.empty()).when(memberDao).getMemberById(anyLong());

        assertThatThrownBy(() -> memberService.getMemberBy(MEMBER_A.getId()))
                .isInstanceOf(NoSuchMemberException.class);
    }
}

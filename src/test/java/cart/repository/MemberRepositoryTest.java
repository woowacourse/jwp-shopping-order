package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import cart.dao.MemberDao;
import cart.domain.AuthMember;
import cart.entity.AuthMemberEntity;
import cart.entity.MemberEntity;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MemberRepositoryTest {

    @InjectMocks
    private MemberRepository memberRepository;

    @Mock
    private MemberDao memberDao;

    @Test
    void 사용자를_id로_조회한다() {
        // given
        given(memberDao.findAuthMemberById(1L))
                .willReturn(Optional.of(new AuthMemberEntity(new MemberEntity(1L, "email@email.com"), "password")));

        // when
        Optional<AuthMember> member = memberRepository.findAuthMemberById(1L);

        // then
        assertAll(
                () -> assertThat(member.get().getMember().getEmail()).isEqualTo("email@email.com"),
                () -> assertThat(member.get().getPassword()).isEqualTo("password")
        );
    }

    @Test
    void 사용자를_email로_조회한다() {
        // given
        given(memberDao.findAuthMemberByEmail("email@email.com"))
                .willReturn(Optional.of(new AuthMemberEntity(new MemberEntity(1L, "email@email.com"), "password")));

        // when
        Optional<AuthMember> member = memberRepository.findAuthMemberByEmail("email@email.com");

        // then
        assertAll(
                () -> assertThat(member.get().getMember().getEmail()).isEqualTo("email@email.com"),
                () -> assertThat(member.get().getPassword()).isEqualTo("password")
        );
    }
}

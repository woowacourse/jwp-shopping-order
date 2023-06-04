package cart.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import cart.domain.Member;
import cart.infrastructure.dao.MemberDao;
import cart.infrastructure.entity.MemberEntity;
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
    private JdbcMemberRepository memberRepository;

    @Mock
    private MemberDao memberDao;

    @Test
    void 사용자를_id로_조회한다() {
        // given
        given(memberDao.findById(1L))
                .willReturn(Optional.of(new MemberEntity(1L, "email@email.com", "password")));

        // when
        Optional<Member> member = memberRepository.findById(1L);

        // then
        assertAll(
                () -> assertThat(member.get().getEmail()).isEqualTo("email@email.com"),
                () -> assertThat(member.get().getPassword()).isEqualTo("password")
        );
    }

    @Test
    void 사용자를_email로_조회한다() {
        // given
        given(memberDao.findByEmail("email@email.com"))
                .willReturn(Optional.of(new MemberEntity(1L, "email@email.com", "password")));

        // when
        Optional<Member> member = memberRepository.findByEmail("email@email.com");

        // then
        assertAll(
                () -> assertThat(member.get().getEmail()).isEqualTo("email@email.com"),
                () -> assertThat(member.get().getPassword()).isEqualTo("password")
        );
    }
}

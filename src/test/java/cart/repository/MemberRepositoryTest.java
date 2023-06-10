package cart.repository;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.member.Member;
import cart.exception.MemberException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static cart.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberRepositoryTest {

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private MemberRepository memberRepository;

    @Test
    void 이메일로_조회한다() {
        String email = "a@a.aa";
        when(memberDao.findByEmail(email)).thenReturn(Optional.of(라잇_엔티티));

        Member member = memberRepository.findByEmail(email);

        assertThat(member).isEqualTo(라잇);
    }

    @Test
    void 이메일로_조회_시_없으면_예외_반환한다() {
        String email = "x@x.xx";
        when(memberDao.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberRepository.findByEmail(email)).isInstanceOf(MemberException.NoExist.class);
    }

    @Test
    void 아이디로_조회한다() {
        Long id = 1L;
        when(memberDao.findById(id)).thenReturn(Optional.of(라잇_엔티티));

        Member member = memberRepository.findById(id);

        assertThat(member).isEqualTo(라잇);
    }

    @Test
    void 아이디로_조회_시_없으면_예외를_발생한다() {
        Long id = 100L;
        when(memberDao.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberRepository.findById(id)).isInstanceOf(MemberException.NoExist.class);
    }

    @Test
    void 모두_조회한다() {
        List<MemberEntity> memberEntities = List.of(
                라잇_엔티티,
                엽토_엔티티
        );
        when(memberDao.findAll()).thenReturn(memberEntities);

        List<Member> members = memberRepository.findAll();

        assertThat(members).hasSize(2);
    }
}

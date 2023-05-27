package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import cart.exception.MemberException;
import cart.repository.mapper.MemberMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberRepositoryTest {

    @InjectMocks
    private MemberRepository memberRepository;

    @Mock
    private MemberDao memberDao;
    @Mock
    private MemberMapper memberMapper;

    @Nested
    @DisplayName("findByEmailAndPassword 메서드는 ")
    class FindByEmailAndPassword {

        @Test
        @DisplayName("조회 시 이메일, 비밀번호와 일치하는 멤버가 존재하면 해당 멤버를 반환한다.")
        void findMember() {
            MemberEntity memberEntity =
                    new MemberEntity(1L, "a@a.com", "password1", 0, LocalDateTime.now(), LocalDateTime.now());
            Member member = new Member(1L, "a@a.com", "password1", 0);
            given(memberDao.findByEmailAndPassword(anyString(), anyString())).willReturn(Optional.of(memberEntity));
            given(memberMapper.toDomain(any(MemberEntity.class))).willReturn(member);

            Member result = memberRepository.findByEmailAndPassword("a@a.com", "password1");

            assertThat(result).usingRecursiveComparison().isEqualTo(member);
        }

        @Test
        @DisplayName("조회 시 이메일, 비밀번호와 일치하는 멤버가 존재하지 않으면 예외를 던진다.")
        void findMemberWithException() {
            given(memberDao.findByEmailAndPassword(anyString(), anyString())).willReturn(Optional.empty());

            assertThatThrownBy(() -> memberRepository.findByEmailAndPassword("a@a.com", "password1"))
                    .isInstanceOf(MemberException.class)
                    .hasMessage("해당 멤버가 존재하지 않습니다.");
        }
    }
}

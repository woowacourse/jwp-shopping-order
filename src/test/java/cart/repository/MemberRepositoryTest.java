package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import cart.exception.MemberException;
import cart.repository.mapper.MemberMapper;
import java.time.LocalDateTime;
import java.util.List;
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

    @Test
    @DisplayName("getAllMembers 메서드는 ")
    void getAllMembers() {
        MemberEntity memberAEntity =
                new MemberEntity(1L, "a@a.com", "password1", 0, LocalDateTime.now(), LocalDateTime.now());
        MemberEntity memberBEntity =
                new MemberEntity(2L, "b@b.com", "password2", 0, LocalDateTime.now(), LocalDateTime.now());
        Member memberA = new Member(1L, "a@a.com", "password1", 0);
        Member memberB = new Member(2L, "b@b.com", "password2", 0);
        given(memberDao.getAllMembers()).willReturn(List.of(memberAEntity, memberBEntity));
        given(memberMapper.toDomain(memberAEntity)).willReturn(memberA);
        given(memberMapper.toDomain(memberBEntity)).willReturn(memberB);

        List<Member> result = memberRepository.getAllMembers();

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(memberA),
                () -> assertThat(result.get(1)).usingRecursiveComparison().isEqualTo(memberB)
        );
    }

    @Nested
    @DisplayName("getMemberByEmailAndPassword 메서드는 ")
    class GetMemberByEmailAndPassword {

        @Test
        @DisplayName("조회 시 이메일, 비밀번호와 일치하는 멤버가 존재하면 해당 멤버를 반환한다.")
        void getMember() {
            MemberEntity memberEntity =
                    new MemberEntity(1L, "a@a.com", "password1", 0, LocalDateTime.now(), LocalDateTime.now());
            Member member = new Member(1L, "a@a.com", "password1", 0);
            given(memberDao.getMemberByEmailAndPassword(anyString(), anyString())).willReturn(Optional.of(memberEntity));
            given(memberMapper.toDomain(any(MemberEntity.class))).willReturn(member);

            Member result = memberRepository.getMemberByEmailAndPassword("a@a.com", "password1");

            assertThat(result).usingRecursiveComparison().isEqualTo(member);
        }

        @Test
        @DisplayName("조회 시 이메일, 비밀번호와 일치하는 멤버가 존재하지 않으면 예외를 던진다.")
        void getMemberWithException() {
            given(memberDao.getMemberByEmailAndPassword(anyString(), anyString())).willReturn(Optional.empty());

            assertThatThrownBy(() -> memberRepository.getMemberByEmailAndPassword("a@a.com", "password1"))
                    .isInstanceOf(MemberException.class)
                    .hasMessage("해당 멤버가 존재하지 않습니다.");
        }
    }
}

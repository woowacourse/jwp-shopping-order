package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.member.Member;
import cart.exception.notfound.NotFoundException;
import cart.repository.mapper.MemberMapper;
import cart.test.RepositoryTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("findAll 메서드는 ")
    void findAll() {
        MemberEntity memberAEntity =
                new MemberEntity(1L, "a@a.com", "password1", 0, LocalDateTime.now(), LocalDateTime.now());
        MemberEntity memberBEntity =
                new MemberEntity(2L, "b@b.com", "password2", 0, LocalDateTime.now(), LocalDateTime.now());
        Long memberIdA = memberDao.save(memberAEntity);
        Long memberIdB = memberDao.save(memberBEntity);
        memberAEntity = memberAEntity.assignId(memberIdA);
        memberBEntity = memberBEntity.assignId(memberIdB);

        List<Member> result = memberRepository.findAll();

        Member memberA = MemberMapper.toDomain(memberAEntity);
        Member memberB = MemberMapper.toDomain(memberBEntity);
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(memberA),
                () -> assertThat(result.get(1)).usingRecursiveComparison().isEqualTo(memberB)
        );
    }

    @Test
    @DisplayName("update 메서드는 멤버 정보를 수정한다.")
    void update() {
        MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 0);
        Long savedMemberId = memberDao.save(memberEntity);
        MemberEntity newMemberEntity = new MemberEntity(savedMemberId, "b@b.com", "password2", 10);

        memberRepository.update(MemberMapper.toDomain(newMemberEntity));

        Member result = memberRepository.findById(savedMemberId);
        assertThat(result).usingRecursiveComparison().isEqualTo(MemberMapper.toDomain(newMemberEntity));
    }

    @Nested
    @DisplayName("findById 메서드는 ")
    class FindById {

        @Test
        @DisplayName("ID에 해당하는 멤버가 존재하지 않으면 예외를 던진다.")
        void notExistMember() {
            assertThatThrownBy(() -> memberRepository.findById(-1L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("해당 멤버가 존재하지 않습니다. 요청 멤버 ID: " + -1);
        }

        @Test
        @DisplayName("ID에 해당하는 멤버가 존재하면 해당 멤버를 반환한다.")
        void getMemberById() {
            MemberEntity memberEntity = new MemberEntity("a@a.com", "passwrod1", 10);
            Long memberId = memberDao.save(memberEntity);

            Member result = memberRepository.findById(memberId);

            Member member = MemberMapper.toDomain(memberEntity);
            member.assignId(memberId);
            assertThat(result).usingRecursiveComparison().isEqualTo(member);
        }
    }

    @Nested
    @DisplayName("findByEmail 메서드는 ")
    class FindByEmail {

        @Test
        @DisplayName("조회 시 이메일과 일치하는 멤버가 존재하면 해당 멤버를 반환한다.")
        void getMember() {
            MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 0);
            Long savedMemberId = memberDao.save(memberEntity);
            memberEntity = memberEntity.assignId(savedMemberId);

            Member result = memberRepository.findByEmail("a@a.com");

            Member member = MemberMapper.toDomain(memberEntity);
            assertAll(
                    () -> assertThat(result.getId()).isEqualTo(savedMemberId),
                    () -> assertThat(result).usingRecursiveComparison().isEqualTo(member)
            );
        }

        @Test
        @DisplayName("조회 시 이메일과 일치하는 멤버가 존재하지 않으면 빈 값을 반환한다.")
        void getEmpty() {
            String email = "a@a.com";
            assertThatThrownBy(() -> memberRepository.findByEmail(email))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("해당 멤버가 존재하지 않습니다. 요청 이메일: " + email);
        }
    }

    @Nested
    @DisplayName("findByEmailAndPassword 메서드는 ")
    class FindByEmailAndPassword {

        @Test
        @DisplayName("조회 시 이메일, 비밀번호와 일치하는 멤버가 존재하면 해당 멤버를 반환한다.")
        void getMember() {
            MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 0);
            Long savedMemberId = memberDao.save(memberEntity);
            memberEntity = memberEntity.assignId(savedMemberId);

            Member result = memberRepository.findByEmailAndPassword("a@a.com", "password1");

            Member member = MemberMapper.toDomain(memberEntity);
            assertAll(
                    () -> assertThat(result.getId()).isEqualTo(savedMemberId),
                    () -> assertThat(result).usingRecursiveComparison().isEqualTo(member)
            );
        }

        @Test
        @DisplayName("조회 시 이메일, 비밀번호와 일치하는 멤버가 존재하지 않으면 예외를 던진다.")
        void getMemberWithException() {
            String email = "a@a.com";
            String password = "password1";
            assertThatThrownBy(() -> memberRepository.findByEmailAndPassword(email, password))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("해당 멤버가 존재하지 않습니다. 요청 이메일: " + email + ", 요청 비밀번호: " + password);
        }
    }
}

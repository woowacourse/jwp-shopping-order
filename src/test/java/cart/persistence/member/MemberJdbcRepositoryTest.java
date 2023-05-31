package cart.persistence.member;

import cart.application.repository.MemberRepository;
import cart.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static cart.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@SuppressWarnings("NonAsciiCharacters")
class MemberJdbcRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new MemberJdbcRepository(jdbcTemplate);
    }


    @Test
    @DisplayName("사용자를 올바르게 생성한다.")
    void createMemberTest() {
        // given
        final Member beaver = 비버;

        // when, then
        assertDoesNotThrow(() -> memberRepository.createMember(beaver));
    }

    @Test
    @DisplayName("전체 사용자 조회")
    void findAllMembersTest() {
        // given
        final Member beaver = 비버;
        final Member dino = 디노;
        final Member leo = 레오;

        memberRepository.createMember(beaver);
        memberRepository.createMember(dino);
        memberRepository.createMember(leo);

        // when
        final List<Member> members = memberRepository.findAllMembers();

        // then
        assertThat(members).usingRecursiveComparison().ignoringFields("id").isEqualTo(List.of(beaver, dino, leo));
    }

    @Test
    @DisplayName("아이디에 따른 사용자 조회")
    void findMemberByIdTest() {
        // given
        final Member beaver = 비버;
        final Member dino = 디노;

        final Long beaverId = memberRepository.createMember(beaver);
        final Long dinoId = memberRepository.createMember(dino);

        // when
        final Optional<Member> member = memberRepository.findMemberById(beaverId);

        // then
        assertThat(member.get()).usingRecursiveComparison().ignoringFields("id").isEqualTo(beaver);
    }

    @Test
    @DisplayName("이메일에 따른 사용자 조회")
    void findMemberByEmailTest() {
        // given
        final Member beaver = 비버;
        final Member dino = 디노;

        final Long beaverId = memberRepository.createMember(beaver);
        final Long dinoId = memberRepository.createMember(dino);

        // when
        final Optional<Member> member = memberRepository.findMemberByEmail(beaver.getEmail());
        // then
        assertThat(member.get()).usingRecursiveComparison().ignoringFields("id").isEqualTo(beaver);
    }

}

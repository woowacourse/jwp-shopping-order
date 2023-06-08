package cart.repository;

import cart.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixtures.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class DBMemberRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new DBMemberRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("ID에 해당하는 Member를 가져온다.")
    void getMemberByIdTest() {
        // given
        Member member = MEMBER1;
        Long memberId = member.getId();

        // when
        Member findMember = memberRepository.getMemberById(memberId);

        // then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("email에 해당하는 Member를 가져온다.")
    void getMemberByEmailTest() {
        // given
        Member member = MEMBER1;
        String email = member.getEmail();

        // when
        Member findMember = memberRepository.getMemberByEmail(email);

        // then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("Member를 저장한다.")
    void addMemberTest() {
        // given
        Member newMemberToInsert = NEW_MEMBER_TO_INSERT;
        Member newMemberAfterAdd = NEW_MEMBER;
        Long newMemberId = NEW_MEMBER.getId();

        // when
        memberRepository.addMember(newMemberToInsert);

        // then
        assertThat(memberRepository.getAllMembers()).contains(newMemberAfterAdd);
    }

    @Test
    @DisplayName("Member 정보를 수정한다.")
    void updateMemberTest() {
        // given
        Member updateMember = UPDATE_MEMBER1;
        Long updateMemberId = updateMember.getId();

        // when
        memberRepository.updateMember(updateMember);

        // then
        assertThat(memberRepository.getMemberById(updateMemberId)).isEqualTo(updateMember);
    }

    @Test
    @DisplayName("ID에 해당하는 Member를 삭제한다.")
    void deleteMemberTest() {
        // given
        Member memberToDelete = MEMBER1;
        Long deleteMemberId = memberToDelete.getId();
        int membersCountBeforeDelete = memberRepository.getAllMembers().size();
        int membersCountAfterDelete = membersCountBeforeDelete - 1;

        // when
        memberRepository.deleteMember(deleteMemberId);

        // then
        assertThat(memberRepository.getAllMembers()).hasSize(membersCountAfterDelete);
    }

    @Test
    @DisplayName("모든 Member를 가져온다.")
    void getAllMembersTest() {
        // given
        Member member1 = MEMBER1;
        Member member2 = MEMBER2;
        List<Member> allMembers = List.of(member1, member2);

        // when
        List<Member> findAllMembers = memberRepository.getAllMembers();

        // then
        assertThat(findAllMembers).isEqualTo(allMembers);
    }
}
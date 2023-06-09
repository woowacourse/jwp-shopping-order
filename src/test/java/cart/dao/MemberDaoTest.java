package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
    }

    @DisplayName("멤버를 추가한다.")
    @Test
    void addMember() {
        //given
        final Member member = new Member(null, "email", "password");

        //when
        memberDao.addMember(member);

        //then
        final Optional<Member> memberByEmail = memberDao.getMemberByEmail("email");
        assertThat(memberByEmail.isPresent()).isTrue();
        assertThat(memberByEmail.get().getPassword()).isEqualTo("password");
    }

    @DisplayName("해당 이메일을 가진 멤버를 조회한다.")
    @Test
    void getMemberByEmail() {
        //given
        memberDao.addMember(new Member(null, "email", "password"));

        //when
        final Optional<Member> memberByEmail = memberDao.getMemberByEmail("email");

        //then
        assertAll(
            () -> assertThat(memberByEmail.isPresent()).isTrue(),
            () -> assertThat(memberByEmail.get().getEmail()).isEqualTo("email"),
            () -> assertThat(memberByEmail.get().getPassword()).isEqualTo("password")
        );
    }

    @DisplayName("해당 ID의 멤버를 조회한다.")
    @Test
    void getMemberById() {
        //given
        memberDao.addMember(new Member(null, "email", "password"));
        final Member member = memberDao.getMemberByEmail("email").get();

        //when
        final Member findMember = memberDao.getMemberById(member.getId()).get();

        //then
        assertThat(findMember).usingRecursiveComparison().isEqualTo(member);
    }

    @DisplayName("멤버의 정보를 업데이트한다.")
    @Test
    void updateMember() {
        //given
        memberDao.addMember(new Member(null, "email", "password"));
        final Member persistedMember = memberDao.getMemberByEmail("email").get();

        //when
        memberDao.updateMember(
            new Member(persistedMember.getId(), persistedMember.getEmail(), "newPassword"));

        //then
        final Member updatedMember = memberDao.getMemberById(persistedMember.getId()).get();
        assertThat(updatedMember.getPassword()).isEqualTo("newPassword");
    }

    @DisplayName("해당 ID의 멤버를 삭제한다.")
    @Test
    void deleteMember() {
        //given
        memberDao.addMember(new Member(null, "email", "password"));
        final Member persistedMember = memberDao.getMemberByEmail("email").get();

        //when
        memberDao.deleteMember(persistedMember.getId());

        //then
        final Optional<Member> findMember = memberDao.getMemberById(persistedMember.getId());
        assertThat(findMember).isEmpty();
    }

    @DisplayName("모든 멤버에 대한 정보를 조회한다.")
    @Test
    void getAllMembers() {
        //given
        final List<Member> beforeMembers = memberDao.getAllMembers();
        memberDao.addMember(new Member(null, "email1", "password1"));
        memberDao.addMember(new Member(null, "email2", "password2"));

        //when
        final List<Member> afterMembers = memberDao.getAllMembers();

        //then
        afterMembers.removeAll(beforeMembers);
        assertAll(
            () -> assertThat(afterMembers.size()).isEqualTo(2),
            () -> assertThat(afterMembers.get(0).getEmail()).isEqualTo("email1"),
            () -> assertThat(afterMembers.get(0).getPassword()).isEqualTo("password1"),
            () -> assertThat(afterMembers.get(1).getEmail()).isEqualTo("email2"),
            () -> assertThat(afterMembers.get(1).getPassword()).isEqualTo("password2")
        );
    }
}

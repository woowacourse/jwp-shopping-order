package cart.dao;

import cart.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@ContextConfiguration(classes = MemberDao.class)
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MemberDao memberDao;

    @DisplayName("id를 기준으로 조회한다.")
    @Test
    void getMemberById() {
        // given
        String email = "test@example.com";
        String password = "password";
        Long memberId = insertMember(new Member(null, email, password));

        // when
        Member member = memberDao.getMemberById(memberId).get();

        // then
        assertEquals(memberId, member.getId());
        assertEquals(email, member.getEmail());
        assertEquals(password, member.getPassword());
    }

    @DisplayName("없는 id로 조회하면 Optional empty를 반환한다.")
    @Test
    void getProductByIdEmpty() {
        // given
        Long id = 99999999999L;

        // when, then
        assertEquals(Optional.empty(),memberDao.getMemberById(id));
    }

    @DisplayName("email을 기준으로 조회한다.")
    @Test
    void getMemberByEmail() {
        // given
        String email = "test@example.com";
        String password = "password";
        Long memberId = insertMember(new Member(null, email, password));

        // when
        Member member = memberDao.getMemberByEmail(email).get();

        // then
        assertEquals(memberId, member.getId());
        assertEquals(email, member.getEmail());
        assertEquals(password, member.getPassword());
    }

    @DisplayName("없는 email로 조회하면 Optional empty를 반환한다.")
    @Test
    void getMemberByEmailEmpty() {
        // given
        String email = "NotExist";

        // when, then
        assertEquals(Optional.empty(),memberDao.getMemberByEmail(email));
    }

    @DisplayName("member를 추가한다.")
    @Test
    void addMember() {
        // given
        String email = "test@example.com";
        String password = "password";
        Member member = new Member(null, email, password);

        // when
        memberDao.addMember(member);

        // then
        Member insertedMember = memberDao.getMemberByEmail(email).get();
        assertEquals(email, insertedMember.getEmail());
        assertEquals(password, insertedMember.getPassword());
    }

    @DisplayName("member를 갱신한다.")
    @Test
    void updateMember() {
        // given
        String email = "test@example.com";
        String password = "password";
        Long memberId = insertMember(new Member(null, email, password));

        // when
        String newEmail = "newemail@example.com";
        String newPassword = "newpassword";
        Member updatedMember = new Member(memberId, newEmail, newPassword);

        memberDao.updateMember(updatedMember);

        // then
        Member updated = memberDao.getMemberById(memberId).get();
        assertEquals(memberId, updated.getId());
        assertEquals(newEmail, updated.getEmail());
        assertEquals(newPassword, updated.getPassword());
    }

    @DisplayName("member를 제거한다.")
    @Test
    void deleteMember() {
        // given
        String email = "test@example.com";
        String password = "password";
        Long memberId = insertMember(new Member(null, email, password));

        // when
        memberDao.deleteMember(memberId);

        // then
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "member", "id = " + memberId));
    }

    @DisplayName("모든 멤버를 가져온다.")
    @Test
    void getAllMembers() {
        // given
        Member member1 = new Member(null, "test1@example.com", "password1");
        Member member2 = new Member(null, "test2@example.com", "password2");
        Member member3 = new Member(null, "test3@example.com", "password3");

        Long id1 = insertMember(member1);
        Long id2 = insertMember(member2);
        Long id3 = insertMember(member3);

        // when
        List<Member> members = memberDao.getAllMembers();

        // then
        assertThat(members.stream().mapToLong(Member::getId)).contains(id1, id2, id3);
    }

    @DisplayName("멤버를 임의로 추가한다.")
    private Long insertMember(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO member (email, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, member.getEmail());
            ps.setString(2, member.getPassword());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}

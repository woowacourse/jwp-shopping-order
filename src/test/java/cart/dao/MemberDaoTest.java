package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.MemberEntity;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MemberDaoTest {

    private final MemberDao memberDao;
    private List<MemberEntity> members;
    private MemberEntity member;

    @Autowired
    public MemberDaoTest(final JdbcTemplate jdbcTemplate) {
        this.memberDao = new MemberDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        members = memberDao.findAll();
        member = members.get(0);
    }

    @DisplayName("아이디가 일치하는 회원 정보를 조회한다.")
    @Test
    void findById() {
        // given
        final Long id = member.getId();

        // when, then
        memberDao.findById(id)
                .ifPresentOrElse(
                        found -> {
                            assertThat(found.getEmail()).isEqualTo(member.getEmail());
                            assertThat(found.getPassword()).isEqualTo(member.getPassword());
                        },
                        () -> Assertions.fail("member not exist; memberId=" + id));
    }

    @DisplayName("아이디가 일치하는 이메일 정보를 조회한다.")
    @Test
    void findByEmail() {
        // given
        final String email = member.getEmail();

        // when, then
        memberDao.findByEmail(email)
                .ifPresentOrElse(
                        found -> {
                            assertThat(found.getId()).isEqualTo(member.getId());
                            assertThat(found.getPassword()).isEqualTo(member.getPassword());
                        },
                        () -> Assertions.fail("member not exist; memberEmail=" + email));
    }
}

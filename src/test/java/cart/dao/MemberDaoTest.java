package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import cart.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql"})
@JdbcTest
class MemberDaoTest {

    private MemberDao memberDao;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate) {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void findAll() {
        final List<Member> result = memberDao.findAll();
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getEmail()).isEqualTo("odo1@woowa.com"),
                () -> assertThat(result.get(0).getPassword()).isEqualTo("1234"),
                () -> assertThat(result.get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.get(1).getEmail()).isEqualTo("odo2@woowa.com"),
                () -> assertThat(result.get(1).getPassword()).isEqualTo("1234")
        );
    }

    @Test
    void findByEmail() {
        final Optional<Member> result = memberDao.findByEmail("odo1@woowa.com");
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getId()).isEqualTo(1L),
                () -> assertThat(result.get().getEmail()).isEqualTo("odo1@woowa.com"),
                () -> assertThat(result.get().getPassword()).isEqualTo("1234")
        );
    }

    @Test
    void findByNoExistEmail() {
        final Optional<Member> result = memberDao.findByEmail("odo3@woowa.com");
        assertThat(result).isEmpty();
    }
}

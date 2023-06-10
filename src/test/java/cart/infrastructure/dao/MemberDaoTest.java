package cart.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.MemberEntity;
import java.util.List;
import java.util.NoSuchElementException;
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
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    @DisplayName("id로 사용자를 조회할 수 있다.")
    void testCreate() {
        // when
        final MemberEntity result = memberDao.findById(1L).orElseThrow(NoSuchElementException::new);

        // then
        assertThat(result.getEmail()).isEqualTo("a@a.com");
        assertThat(result.getPassword()).isEqualTo("1234");
    }

    @Test
    @DisplayName("이메일로 사용자를 조회할 수 있다.")
    void testFindByEmail() {
        // when
        final MemberEntity result = memberDao.findByEmail("a@a.com").orElseThrow(NoSuchElementException::new);

        // then
        assertThat(result.getEmail()).isEqualTo("a@a.com");
        assertThat(result.getPassword()).isEqualTo("1234");
    }

    @Test
    @DisplayName("모든 사용자를 조회할 수 있다.")
    void testFindAll() {
        // given
        final MemberEntity expected1 = MemberEntity.of(1L, "a@a.com", "1234");
        final MemberEntity expected2 = MemberEntity.of(2L, "b@b.com", "1234");

        // when
        final List<MemberEntity> memberEntities = memberDao.findAll();

        // then
        assertAll(
                () -> assertThat(memberEntities).hasSize(2),
                () -> assertThat(memberEntities)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(expected1, expected2))
        );
    }
}

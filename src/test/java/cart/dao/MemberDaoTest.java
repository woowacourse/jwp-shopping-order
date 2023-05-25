package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
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
    void 사용자를_저장한다() {
        // given
        final Member member = new Member("pizza@pizza.com", "password");

        // when
        final Long id = memberDao.saveAndGetId(member);

        // then
        final List<Member> result = memberDao.findAll();
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(id).isPositive()
        );
    }

    @Test
    void 전체_사용자를_조회한다() {
        // given
        final Member member1 = new Member("pizza1@pizza.com", "password");
        final Member member2 = new Member("pizza2@pizza.com", "password");
        final Long id1 = memberDao.saveAndGetId(member1);
        final Long id2 = memberDao.saveAndGetId(member2);

        // when
        List<Member> result = memberDao.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new Member(id1, member1.getEmail(), member1.getPassword()),
                new Member(id2, member2.getEmail(), member2.getPassword())
        ));
    }
}

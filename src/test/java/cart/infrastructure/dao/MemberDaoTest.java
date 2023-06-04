package cart.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.infrastructure.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        jdbcTemplate.update("DELETE FROM member");
    }

    @Test
    void 사용자를_저장한다() {
        // given
        MemberEntity memberEntity = new MemberEntity("email@email.com", "password");

        // when
        Long id = memberDao.save(memberEntity);

        // then
        assertThat(id).isPositive();
    }

    @Test
    void 사용자를_id로_조회한다() {
        // given
        MemberEntity memberEntity = new MemberEntity("email@email.com", "password");
        Long id = memberDao.save(memberEntity);

        // when
        Optional<MemberEntity> savedMember = memberDao.findById(id);

        // then
        assertThat(savedMember).isPresent();
    }

    @Test
    void 사용자를_email로_조회한다() {
        // given
        MemberEntity memberEntity = new MemberEntity("email@email.com", "password");
        Long id = memberDao.save(memberEntity);

        // when
        Optional<MemberEntity> savedMember = memberDao.findByEmail("email@email.com");

        // then
        assertThat(savedMember).isPresent();
    }

    @Test
    void 전체_사용자를_조회한다() {
        // given
        memberDao.save(new MemberEntity("email1@email.com", "password"));
        memberDao.save(new MemberEntity("email2@email.com", "password"));

        // when
        List<MemberEntity> allMembers = memberDao.getAllMembers();

        // then
        assertThat(allMembers).hasSize(2);
    }
}

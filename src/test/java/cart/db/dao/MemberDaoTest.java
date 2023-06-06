package cart.db.dao;

import cart.db.entity.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(savedMember.get()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(memberEntity);
    }

    @Test
    void 사용자를_email로_조회한다() {
        // given
        MemberEntity memberEntity = new MemberEntity("email@email.com", "password");
        memberDao.save(memberEntity);

        // when
        Optional<MemberEntity> savedMember = memberDao.findByEmail("email@email.com");

        // then
        assertThat(savedMember).isPresent();
        assertThat(savedMember.get())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(memberEntity);
    }

    @Test
    void 전체_사용자를_조회한다() {
        // given
        MemberEntity memberEntity1 = new MemberEntity("email1@email.com", "password");
        MemberEntity memberEntity2 = new MemberEntity("email2@email.com", "password");
        memberDao.save(memberEntity1);
        memberDao.save(memberEntity2);

        // when
        List<MemberEntity> allMembers = memberDao.getAllMembers();

        // then
        assertThat(allMembers).hasSize(2);
        assertThat(allMembers).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(memberEntity1, memberEntity2));
    }
}

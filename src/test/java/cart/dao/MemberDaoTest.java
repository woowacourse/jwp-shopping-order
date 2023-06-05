package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
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
        final MemberEntity memberEntity = new MemberEntity("pizza@pizza.com", "password");

        // when
        memberDao.insert(memberEntity);

        // then
        assertThat(memberDao.findAll()).hasSize(1);
    }

    @Test
    void 전체_사용자를_조회한다() {
        // given
        final MemberEntity memberEntity1 = new MemberEntity("pizza1@pizza.com", "password");
        final MemberEntity memberEntity2 = new MemberEntity("pizza2@pizza.com", "password");
        final MemberEntity savedMemberEntity1 = memberDao.insert(memberEntity1);
        final MemberEntity savedMemberEntity2 = memberDao.insert(memberEntity2);

        // when
        final List<MemberEntity> result = memberDao.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(savedMemberEntity1, savedMemberEntity2));
    }

    @Test
    void 단일_사용자를_조회한다() {
        // given
        final MemberEntity memberEntity = new MemberEntity("pizza1@pizza.com", "password");
        final MemberEntity savedMemberEntity = memberDao.insert(memberEntity);

        // when
        final Optional<MemberEntity> result = memberDao.findById(savedMemberEntity.getId());

        // then
        assertThat(result).isPresent();
    }
}

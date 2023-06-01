package cart.dao;

import static cart.fixture.MemberFixture.사용자1_엔티티;
import static cart.fixture.MemberFixture.사용자2_엔티티;
import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.MemberEntity;
import cart.test.RepositoryTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@RepositoryTest
class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    void 사용자를_저장한다() {
        // given
        final MemberEntity memberEntity = new MemberEntity("pizza1@pizza.com", "password");

        // when
        memberDao.insert(memberEntity);

        // then
        assertThat(memberDao.findAll()).hasSize(1);
    }

    @Test
    void 전체_사용자를_조회한다() {
        // given
        final MemberEntity savedMemberEntity1 = memberDao.insert(사용자1_엔티티);
        final MemberEntity savedMemberEntity2 = memberDao.insert(사용자2_엔티티);

        // when
        final List<MemberEntity> result = memberDao.findAll();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(savedMemberEntity1, savedMemberEntity2));
    }

    @Test
    void 단일_사용자를_조회한다() {
        // given
        final MemberEntity savedMemberEntity = memberDao.insert(사용자1_엔티티);

        // when
        final Optional<MemberEntity> result = memberDao.findById(savedMemberEntity.getId());

        // then
        assertThat(result).isPresent();
    }
}

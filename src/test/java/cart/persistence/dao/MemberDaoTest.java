package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.persistence.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(MemberDao.class)
class MemberDaoTest extends DaoTest {

    private MemberEntity 져니;

    @Autowired
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        져니 = new MemberEntity("journey", "password");
    }

    @Test
    @DisplayName("사용자 정보를 저장한다.")
    void insert() {
        // when
        final Long 저장된_져니_아이디 = memberDao.insert(져니);

        // then
        final Optional<MemberEntity> member = memberDao.findById(저장된_져니_아이디);
        final MemberEntity findMember = member.get();
        assertThat(findMember)
            .extracting(MemberEntity::getName, MemberEntity::getPassword)
            .containsExactly("journey", "password");
    }

    @Test
    @DisplayName("유효한 사용자 아이디가 주어지면, 사용자 정보를 조회한다.")
    void findById_success() {
        // given
        final Long savedMemberId = memberDao.insert(져니);

        // when
        final Optional<MemberEntity> member = memberDao.findById(savedMemberId);

        // then
        final MemberEntity findMember = member.get();
        assertThat(findMember)
            .extracting(MemberEntity::getName, MemberEntity::getPassword)
            .containsExactly("journey", "password");
    }

    @Test
    @DisplayName("유효하지 않은 사용자 아이디가 주어지면, 빈 값을 반환한다.")
    void findById_empty() {
        // when
        final Optional<MemberEntity> member = memberDao.findById(1L);

        // then
        assertThat(member).isEmpty();
    }

    @Test
    @DisplayName("사용자 전체를 조회한다")
    void findAll() {
        // given
        memberDao.insert(져니);
        memberDao.insert(new MemberEntity("crew", "test"));

        // when
        final List<MemberEntity> memberEntities = memberDao.findAll();

        // then
        assertThat(memberEntities).hasSize(2);
        assertThat(memberEntities)
            .extracting(MemberEntity::getName, MemberEntity::getPassword)
            .containsExactly(tuple("journey", "password"),
                tuple("crew", "test"));
    }

    @Test
    @DisplayName("유효한 사용자 이름이 주어지면, 사용자 정보를 반환한다.")
    void findByName_success() {
        // given
        memberDao.insert(져니);

        // when
        final Optional<MemberEntity> member = memberDao.findByName("journey");
        final MemberEntity findMember = member.get();

        // then
        assertThat(findMember)
            .extracting(MemberEntity::getName, MemberEntity::getPassword)
            .containsExactly("journey", "password");
    }

    @Test
    @DisplayName("유효하지 않은 사용자 이름 주어지면, 빈 값을 반환한다.")
    void findByName_fail() {
        // when
        final Optional<MemberEntity> member = memberDao.findByName("test");

        // then
        assertThat(member).isEmpty();
    }
}

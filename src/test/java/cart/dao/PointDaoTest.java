package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.PointEntity;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/init_point.sql"})
class PointDaoTest {

    private static final long memberId = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PointDao pointDao;

    @BeforeEach
    void setUp() {
        this.pointDao = new PointDao(jdbcTemplate);
    }

    @Test
    @DisplayName("사용자의 포인트를 조회할 수 있다.")
    void findByMemberId_success() {
        // when
        Optional<PointEntity> pointEntity = pointDao.findByMemberId(memberId);

        // then
        assertThat(pointEntity).isPresent()
            .get()
            .hasFieldOrPropertyWithValue("point", 1000);
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 포인트를 조회하면 예외가 발생한다.")
    void findByMemberId_noMember_fail() {
        // given
        long nonExistingMemberId = 3L;

        // when
        Optional<PointEntity> pointEntity = pointDao.findByMemberId(nonExistingMemberId);

        // then
        assertThat(pointEntity).isEmpty();

    }

    @Test
    @DisplayName("사용자의 포인트를 업데이트할 수 있다.")
    void update_success() {
        // given
        PointEntity pointToUpdate = new PointEntity(1L, memberId, 1500);

        // when
        pointDao.update(pointToUpdate);

        // then
        assertThat(pointDao.findByMemberId(memberId))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(pointToUpdate);

    }
}
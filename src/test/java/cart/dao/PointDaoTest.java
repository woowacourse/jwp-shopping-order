package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import cart.domain.Point;
import cart.entity.PointEntity;
import cart.fixture.PointEntityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
class PointDaoTest {

    private PointDao pointDao;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate) {
        pointDao = new PointDao(jdbcTemplate);
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql"})
    @Test
    void insert() {
        final PointEntity result = pointDao.insert(PointEntityFixture.ODO1_1);
        assertAll(
                () -> assertThat(result.getId()).isPositive(),
                () -> assertThat(result.getEarnedPoint()).isEqualTo(300),
                () -> assertThat(result.getLeftPoint()).isEqualTo(200),
                () -> assertThat(result.getMemberId()).isEqualTo(1L),
                () -> assertThat(result.getExpiredAt()).isEqualTo(Timestamp.valueOf("2024-01-01 10:00:00")),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }

    @DisplayName("남아있는 포인트를 만료일이 빠른 순으로 정렬해서 반환한다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql"})
    @Test
    void findRemainingPointsByMemberId() {
        final Long id3 = pointDao.insert(PointEntityFixture.ODO1_3).getId();
        final Long id2 = pointDao.insert(PointEntityFixture.ODO1_2).getId();
        final Long id1 = pointDao.insert(PointEntityFixture.ODO1_1).getId();
        pointDao.insert(PointEntityFixture.ODO1_4);
        pointDao.insert(PointEntityFixture.ODO2_1);
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(1L);
        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result.get(0).getId()).isEqualTo(id1),
                () -> assertThat(result.get(0).getEarnedPoint()).isEqualTo(300),
                () -> assertThat(result.get(0).getLeftPoint()).isEqualTo(200),
                () -> assertThat(result.get(0).getMemberId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getExpiredAt()).isEqualTo(Timestamp.valueOf("2024-01-01 10:00:00")),
                () -> assertThat(result.get(0).getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00")),
                () -> assertThat(result.get(1).getId()).isEqualTo(id2),
                () -> assertThat(result.get(1).getEarnedPoint()).isEqualTo(500),
                () -> assertThat(result.get(1).getLeftPoint()).isEqualTo(300),
                () -> assertThat(result.get(1).getMemberId()).isEqualTo(1L),
                () -> assertThat(result.get(1).getExpiredAt()).isEqualTo(Timestamp.valueOf("2024-02-01 10:00:00")),
                () -> assertThat(result.get(1).getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00")),
                () -> assertThat(result.get(2).getId()).isEqualTo(id3),
                () -> assertThat(result.get(2).getEarnedPoint()).isEqualTo(600),
                () -> assertThat(result.get(2).getLeftPoint()).isEqualTo(100),
                () -> assertThat(result.get(2).getMemberId()).isEqualTo(1L),
                () -> assertThat(result.get(2).getExpiredAt()).isEqualTo(Timestamp.valueOf("2024-03-01 10:00:00")),
                () -> assertThat(result.get(2).getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }

    @Sql({"classpath:deleteAll.sql"})
    @Test
    void findRemainingPointsByNoExistMemberId() {
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(1L);
        assertThat(result).isEmpty();
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql"})
    @Test
    void findById() {
        final Long id = pointDao.insert(PointEntityFixture.ODO1_1).getId();
        final Optional<PointEntity> result = pointDao.findById(id);
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getId()).isEqualTo(id),
                () -> assertThat(result.get().getEarnedPoint()).isEqualTo(300),
                () -> assertThat(result.get().getLeftPoint()).isEqualTo(200),
                () -> assertThat(result.get().getMemberId()).isEqualTo(1L),
                () -> assertThat(result.get().getExpiredAt()).isEqualTo(Timestamp.valueOf("2024-01-01 10:00:00")),
                () -> assertThat(result.get().getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql"})
    @Test
    void updateLeftPoint() {
        final Long id = pointDao.insert(PointEntityFixture.ODO1_1).getId();
        pointDao.updateLeftPoint(id, Point.valueOf(10));
        final Optional<PointEntity> result = pointDao.findById(id);
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getId()).isEqualTo(id),
                () -> assertThat(result.get().getEarnedPoint()).isEqualTo(300),
                () -> assertThat(result.get().getLeftPoint()).isEqualTo(10),
                () -> assertThat(result.get().getMemberId()).isEqualTo(1L),
                () -> assertThat(result.get().getExpiredAt()).isEqualTo(Timestamp.valueOf("2024-01-01 10:00:00")),
                () -> assertThat(result.get().getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }
}

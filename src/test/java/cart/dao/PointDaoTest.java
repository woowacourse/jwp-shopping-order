package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Timestamp;

import cart.domain.Point;
import cart.entity.PointEntity;
import cart.fixture.PointEntityFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
                () -> assertThat(result.getExpiredAt()).isEqualTo(Timestamp.valueOf("2023-12-31 10:00:00")),
                () -> assertThat(result.getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }

    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql"})
    @ParameterizedTest
    @CsvSource({"1, 600", "2, 200"})
    void findLeftPointByMemberId(final Long memberId, final int value) {
        pointDao.insert(PointEntityFixture.ODO1_1);
        pointDao.insert(PointEntityFixture.ODO1_2);
        pointDao.insert(PointEntityFixture.ODO1_3);
        pointDao.insert(PointEntityFixture.ODO2_1);
        final Point leftPoint = pointDao.findLeftPointByMemberId(memberId);
        assertThat(leftPoint).isEqualTo(Point.valueOf(value));
    }
}

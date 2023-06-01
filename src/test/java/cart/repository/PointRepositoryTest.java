package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Timestamp;
import java.util.List;

import cart.dao.PointDao;
import cart.domain.DefaultPointManager;
import cart.domain.Point;
import cart.domain.Price;
import cart.entity.PointEntity;
import cart.exception.NotEnoughPointException;
import cart.fixture.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
class PointRepositoryTest {

    private PointDao pointDao;
    private PointRepository pointRepository;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate) {
        pointDao = new PointDao(jdbcTemplate);
        pointRepository = new PointRepository(pointDao, new DefaultPointManager());
    }

    @DisplayName("포인트가 모자랄 때 예외를 발생시킨다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void updatePointWhenNotEnoughPoint() {
        assertThatThrownBy(() -> pointRepository.updatePoint(MemberFixture.MEMBER, Point.valueOf(601), null, null))
                .isInstanceOf(NotEnoughPointException.class)
                .hasMessage(new NotEnoughPointException(600, 601).getMessage());
    }

    @DisplayName("보유 포인트가 0일 때 사용 포인트가 있으면 예외를 발생시킨다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql"})
    @Test
    void updatePointWhenLeftPointIsZero() {
        assertThatThrownBy(() -> pointRepository.updatePoint(MemberFixture.MEMBER, Point.valueOf(1), null, null))
                .isInstanceOf(NotEnoughPointException.class)
                .hasMessage(new NotEnoughPointException(0, 1).getMessage());
    }

    @DisplayName("보유 포인트를 모두 사용했을 때 기존 포인트는 모두 0이 되고 새로운 포인트가 추가된다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void updatePointWhenUsedAndLeftSame() {
        pointRepository.updatePoint(MemberFixture.MEMBER, Point.valueOf(600), Price.valueOf(1000), Timestamp.valueOf("2023-05-31 10:00:00"));
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(MemberFixture.MEMBER.getId());
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0).getId()).isPositive(),
                () -> assertThat(result.get(0).getEarnedPoint()).isEqualTo(50),
                () -> assertThat(result.get(0).getLeftPoint()).isEqualTo(50),
                () -> assertThat(result.get(0).getMemberId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getExpiredAt()).isEqualTo(Timestamp.valueOf("2023-11-30 10:00:00")),
                () -> assertThat(result.get(0).getCreatedAt()).isEqualTo(Timestamp.valueOf("2023-05-31 10:00:00"))
        );
    }

    @DisplayName("보유 포인트가 200, 300, 100이고 100을 사용하면 100, 300, 100이 되고 새로운 포인트가 추가된다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void updatePoint100() {
        pointRepository.updatePoint(MemberFixture.MEMBER, Point.valueOf(100), Price.valueOf(1000), Timestamp.valueOf("2023-05-31 10:00:00"));
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(MemberFixture.MEMBER.getId());
        assertAll(
                () -> assertThat(result.get(0).getLeftPoint()).isEqualTo(50),
                () -> assertThat(result.get(1).getLeftPoint()).isEqualTo(100),
                () -> assertThat(result.get(2).getLeftPoint()).isEqualTo(300),
                () -> assertThat(result.get(3).getLeftPoint()).isEqualTo(100)
        );
    }

    @DisplayName("보유 포인트가 200, 300, 100이고 200을 사용하면 300, 100이 되고 새로운 포인트가 추가된다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void updatePoint200() {
        pointRepository.updatePoint(MemberFixture.MEMBER, Point.valueOf(200), Price.valueOf(1000), Timestamp.valueOf("2023-05-31 10:00:00"));
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(MemberFixture.MEMBER.getId());
        assertAll(
                () -> assertThat(result.get(0).getLeftPoint()).isEqualTo(50),
                () -> assertThat(result.get(1).getLeftPoint()).isEqualTo(300),
                () -> assertThat(result.get(2).getLeftPoint()).isEqualTo(100)
        );
    }

    @DisplayName("보유 포인트가 200, 300, 100이고 201을 사용하면 299, 100이 되고 새로운 포인트가 추가된다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void updatePoint201() {
        pointRepository.updatePoint(MemberFixture.MEMBER, Point.valueOf(201), Price.valueOf(1000), Timestamp.valueOf("2023-05-31 10:00:00"));
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(MemberFixture.MEMBER.getId());
        assertAll(
                () -> assertThat(result.get(0).getLeftPoint()).isEqualTo(50),
                () -> assertThat(result.get(1).getLeftPoint()).isEqualTo(299),
                () -> assertThat(result.get(2).getLeftPoint()).isEqualTo(100)
        );
    }

    @DisplayName("보유 포인트가 200, 300, 100이고 500 사용하면 100이 되고 새로운 포인트가 추가된다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void updatePoint500() {
        pointRepository.updatePoint(MemberFixture.MEMBER, Point.valueOf(500), Price.valueOf(1000), Timestamp.valueOf("2023-05-31 10:00:00"));
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(MemberFixture.MEMBER.getId());
        assertAll(
                () -> assertThat(result.get(0).getLeftPoint()).isEqualTo(50),
                () -> assertThat(result.get(1).getLeftPoint()).isEqualTo(100)
        );
    }
}

package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import cart.dao.PointDao;
import cart.domain.Point;
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
        pointRepository = new PointRepository(pointDao);
    }

    @DisplayName("포인트가 모자랄 때 예외를 발생시킨다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void reduceMemberPointWhenNotEnoughPoint() {
        assertThatThrownBy(() -> pointRepository.reduceMemberPoint(MemberFixture.MEMBER, Point.valueOf(601)))
                .isInstanceOf(NotEnoughPointException.class)
                .hasMessage(new NotEnoughPointException(600, 601).getMessage());
    }

    @DisplayName("보유 포인트가 0일 때 사용 포인트가 있으면 예외를 발생시킨다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql"})
    @Test
    void reduceMemberPointWhenLeftPointIsZero() {
        assertThatThrownBy(() -> pointRepository.reduceMemberPoint(MemberFixture.MEMBER, Point.valueOf(1)))
                .isInstanceOf(NotEnoughPointException.class)
                .hasMessage(new NotEnoughPointException(0, 1).getMessage());
    }

    @DisplayName("보유 포인트를 모두 사용했을 때 남은 포인트는 모두 0이 된다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void reduceAllMemberPoint() {
        pointRepository.reduceMemberPoint(MemberFixture.MEMBER, Point.valueOf(600));
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(MemberFixture.MEMBER.getId());
        assertThat(result).isEmpty();
    }

    @DisplayName("보유 포인트가 200, 300, 100이고 100을 사용하면 100, 300, 100이 된다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void reduceMemberPoint100() {
        pointRepository.reduceMemberPoint(MemberFixture.MEMBER, Point.valueOf(100));
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(MemberFixture.MEMBER.getId());
        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getLeftPoint()).isEqualTo(100),
                () -> assertThat(result.get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.get(1).getLeftPoint()).isEqualTo(300),
                () -> assertThat(result.get(2).getId()).isEqualTo(3L),
                () -> assertThat(result.get(2).getLeftPoint()).isEqualTo(100)
        );
    }

    @DisplayName("보유 포인트가 200, 300, 100이고 200을 사용하면 300, 100이 된다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void reduceAllMemberPoint200() {
        pointRepository.reduceMemberPoint(MemberFixture.MEMBER, Point.valueOf(200));
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(MemberFixture.MEMBER.getId());
        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(2L),
                () -> assertThat(result.get(0).getLeftPoint()).isEqualTo(300),
                () -> assertThat(result.get(1).getId()).isEqualTo(3L),
                () -> assertThat(result.get(1).getLeftPoint()).isEqualTo(100)
        );
    }

    @DisplayName("보유 포인트가 200, 300, 100이고 201을 사용하면 299, 100이 된다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void reduceAllMemberPoint201() {
        pointRepository.reduceMemberPoint(MemberFixture.MEMBER, Point.valueOf(201));
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(MemberFixture.MEMBER.getId());
        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(2L),
                () -> assertThat(result.get(0).getLeftPoint()).isEqualTo(299),
                () -> assertThat(result.get(1).getId()).isEqualTo(3L),
                () -> assertThat(result.get(1).getLeftPoint()).isEqualTo(100)
        );
    }

    @DisplayName("보유 포인트가 200, 300, 100이고 500 사용하면 100이 된다.")
    @Sql({"classpath:deleteAll.sql", "classpath:insertMember.sql", "classpath:insertPoint.sql"})
    @Test
    void reduceAllMemberPoint500() {
        pointRepository.reduceMemberPoint(MemberFixture.MEMBER, Point.valueOf(500));
        final List<PointEntity> result = pointDao.findRemainingPointsByMemberId(MemberFixture.MEMBER.getId());
        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(3L),
                () -> assertThat(result.get(0).getLeftPoint()).isEqualTo(100)
        );
    }
}

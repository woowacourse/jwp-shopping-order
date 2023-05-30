package cart.dao;

import cart.domain.Member;
import cart.domain.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@ContextConfiguration(classes = PointDao.class)
class PointDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PointDao pointDao;

    private Long memberId;
    private final String MEMBER_EMAIL = "test@example.com";


    @DisplayName("point를 제외한 더미 데이터를 임의로 생성한다.")
    @BeforeEach
    void setUp() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO member (email, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, MEMBER_EMAIL);
            ps.setString(2, "test");

            return ps;
        }, keyHolder);
        memberId = Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @DisplayName("사용할 수 있는 포인트를 memberId로 조회한다.")
    @Test
    void getBeforeExpirationAndRemainingPointsByMemberId() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Point point = new Point(1000, 1000, member, now.plusDays(7), now);
        Long pointId = insertPoint(point);

        // when
        List<Point> points = pointDao.getBeforeExpirationAndRemainingPointsByMemberId(memberId);

        // then
        Point firstPoint = points.get(0);
        assertEquals(1, points.size());
        assertEquals(member, firstPoint.getMember());
        assertEquals(point.getEarnedPoint(), firstPoint.getEarnedPoint());
    }

    @DisplayName("point를 생성한다.")
    @Test
    void createPoint() {
        // given
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Point point = new Point(100, 100, member, LocalDateTime.now().plusDays(30), LocalDateTime.now());

        // when
        Long generatedId = pointDao.createPoint(point);

        // then
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "point", "id = " + generatedId));
    }

    @DisplayName("point의 잔액을 갱신한다.")
    @Test
    void updatePointLeftBalance() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Member member = new Member(memberId, MEMBER_EMAIL, null);
        Point point = new Point(1000, 1000, member, now.plusDays(7), now);
        Long pointId = insertPoint(point);
        Point createdPoint = new Point(pointId, point.getEarnedPoint(), point.getLeftPoint(), point.getMember(), point.getExpiredAt(), point.getCreatedAt());

        // when
        int leftBalance = 10;
        pointDao.updatePointLeftBalance(createdPoint, leftBalance);

        // then
        List<Point> remainingPointsByMemberId = pointDao.getBeforeExpirationAndRemainingPointsByMemberId(memberId);
        Point firstPoint = remainingPointsByMemberId.get(0);
        assertEquals(leftBalance, firstPoint.getLeftPoint());
    }

    @DisplayName("point를 임의로 추가한다.")
    private Long insertPoint(Point point) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO point (earned_point, left_point, member_id, expired_at, created_at) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setInt(1, point.getEarnedPoint());
            ps.setInt(2, point.getEarnedPoint());
            ps.setLong(3, point.getMember().getId());
            ps.setTimestamp(4, Timestamp.valueOf(point.getExpiredAt()));
            ps.setTimestamp(5, Timestamp.valueOf(point.getCreatedAt()));

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}

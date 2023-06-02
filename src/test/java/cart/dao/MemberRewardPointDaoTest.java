package cart.dao;

import cart.domain.point.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cart.TestFeatures.*;
import static org.assertj.core.api.Assertions.*;

@JdbcTest
class MemberRewardPointDaoTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final RowMapper<Point> rowMapper = (rs, rowNum) ->
            new Point(
                    rs.getLong("id"),
                    rs.getInt("point"),
                    rs.getTimestamp("created_at")
                      .toLocalDateTime(),
                    rs.getTimestamp("expired_at")
                      .toLocalDateTime()
            );

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private MemberRewardPointDao memberRewardPointDao;

    @BeforeEach
    void setUp() {
        memberRewardPointDao = new MemberRewardPointDao(namedParameterJdbcTemplate, jdbcTemplate, dataSource);
    }

    @DisplayName("포인트와 회원 아이디, 주문 아이디르르 통해 포인트 정보를 저장한다")
    @Test
    void save() {
        // given
        Long memberId = 1L;
        Long orderId = 1L;
        Point point = new Point(500, LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                LocalDateTime.parse("2023-05-25 12:12:12", formatter));

        // when
        Long saveId = memberRewardPointDao.save(memberId, point, orderId);

        // then
        Point result = getPointById(saveId);
        assertThat(result).usingRecursiveComparison()
                          .ignoringFields("id")
                          .isEqualTo(point);
    }

    private Point getPointById(Long saveId) {
        String sql = "SELECT * FROM member_reward_point WHERE id = :id";
        SqlParameterSource source = new MapSqlParameterSource("id", saveId);
        return namedParameterJdbcTemplate.queryForObject(sql, source, rowMapper);
    }

    @DisplayName("회원 아이디를 통해 회원이 갖고 있는 모든 포인트를 조회한다")
    @Test
    void getAllByMemberId() {
        // given
        Long memberId = 1L;

        // when
        List<Point> points = memberRewardPointDao.getAllByMemberId(memberId);

        // then
        assertThat(points).usingRecursiveFieldByFieldElementComparator()
                          .contains(회원1_주문1_포인트, 회원1_주문2_포인트, 회원1_주문3_포인트);
    }

    @DisplayName("적립된 주문 아이디를 통해 포인트를 조회한다")
    @Test
    void getAllByOrderId() {
        // given
        Long orderId = 1L;

        // when
        Point point = memberRewardPointDao.getPointByOrderId(orderId);

        // then
        assertThat(point).usingRecursiveComparison()
                         .isEqualTo(회원1_주문1_포인트);
    }

    @DisplayName("기존의 데이터를 수정된 데이터로 한 번에 수정한다")
    @Test
    void updatePoints() {
        // given
        List<Point> updatePoints = List.of(
                new Point(1L, 0, LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                        LocalDateTime.parse("2023-05-25 12:12:12", formatter)),
                new Point(3L, 500, LocalDateTime.parse("2023-05-15 12:12:12", formatter),
                        LocalDateTime.parse("2023-05-30 12:12:12", formatter))
        );
        List<Point> expectResult = List.of(
                new Point(1L, 0, LocalDateTime.parse("2023-05-20 12:12:12", formatter),
                        LocalDateTime.parse("2023-05-25 12:12:12", formatter)),
                new Point(2L, 700, LocalDateTime.parse("2023-05-18 12:12:12", formatter),
                        LocalDateTime.parse("2023-05-29 12:12:12", formatter)),
                new Point(3L, 500, LocalDateTime.parse("2023-05-15 12:12:12", formatter),
                        LocalDateTime.parse("2023-05-30 12:12:12", formatter))
        );

        // when
        memberRewardPointDao.updatePoints(updatePoints);

        // then
        List<Point> resultPoints = getAllPoint();
        assertThat(resultPoints).usingRecursiveFieldByFieldElementComparator()
                                .containsAll(expectResult);
    }

    private List<Point> getAllPoint() {
        String sql = "SELECT * FROM member_reward_point";
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }
}

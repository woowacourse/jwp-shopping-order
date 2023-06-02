package cart.dao;

import cart.domain.point.UsedPoint;
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
import java.util.List;

import static cart.TestFeatures.*;
import static org.assertj.core.api.Assertions.*;

@JdbcTest
class OrderMemberUsedPointDaoTest {

    private static final RowMapper<UsedPoint> rowMapper = (rs, rowNum) ->
            new UsedPoint(
                    rs.getLong("id"),
                    rs.getLong("used_reward_point_id"),
                    rs.getInt("used_point")
            );

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private OrderMemberUsedPointDao orderMemberUsedPointDao;

    @BeforeEach
    void setUp() {
        orderMemberUsedPointDao = new OrderMemberUsedPointDao(jdbcTemplate, dataSource, namedParameterJdbcTemplate);
    }

    @DisplayName("UsedPoint를 사용해 사용한 ")
    @Test
    void save() {
        // given
        UsedPoint usedPoint = new UsedPoint(1L, 500);
        Long orderId = 1L;

        // when
        Long saveId = orderMemberUsedPointDao.save(usedPoint, orderId);

        // then
        UsedPoint result = getUsedPointById(saveId);
        assertThat(result).usingRecursiveComparison()
                          .ignoringFields("id")
                          .isEqualTo(usedPoint);
    }

    private UsedPoint getUsedPointById(Long id) {
        String sql = "SELECT * FROM order_member_used_point WHERE id = :id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, source, rowMapper);
    }

    @DisplayName("UsedPoint 리스트를 통해 한 번에 포인트들을 저장할 수 있다")
    @Test
    void saveAll() {
        // given
        int originalUsedPointsSize = getAllUsedPoint().size();
        List<UsedPoint> usedPoints = List.of(
                new UsedPoint(1L, 500),
                new UsedPoint(2L, 300),
                new UsedPoint(2L, 1000)
        );
        Long orderId = 1L;

        // when
        orderMemberUsedPointDao.saveAll(usedPoints, orderId);

        // then
        int afterSaveUsedPointsSize = getAllUsedPoint().size();
        assertThat(afterSaveUsedPointsSize).isEqualTo(originalUsedPointsSize + usedPoints.size());
    }

    private List<UsedPoint> getAllUsedPoint() {
        String sql = "SELECT * FROM order_member_used_point";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @DisplayName("주문 아이디를 통해 해당 주문에 사용한 포인트 목록을 조회할 수 있다")
    @Test
    void getAllUsedPointByOrderId() {
        // given
        Long orderId = 1L;

        // when
        List<UsedPoint> usedPoints = orderMemberUsedPointDao.getAllUsedPointByOrderId(orderId);

        // then
        assertThat(usedPoints).usingRecursiveFieldByFieldElementComparator()
                              .contains(주문1_포인트_사용1, 주문1_포인트_사용2, 주문1_포인트_사용3);
    }
}

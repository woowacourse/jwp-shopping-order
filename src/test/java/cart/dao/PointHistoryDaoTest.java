package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.dto.point.PointHistoryDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/init_point_history.sql"})
class PointHistoryDaoTest {

    private final long memberId = 1L;
    private final long orderId = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PointHistoryDao pointHistoryDao;

    @BeforeEach
    void setUp() {
        this.pointHistoryDao = new PointHistoryDao(jdbcTemplate);
    }

    @Test
    @DisplayName("한 주문에서 적립된 포인트, 사용한 포인트를 저장하고 저장 내역을 조회할 수 있다.")
    void save_findByOrderId() {
        // given
        PointHistoryDto pointHistoryDto = new PointHistoryDto(memberId, 100, 50, orderId);

        // when
        pointHistoryDao.save(pointHistoryDto);

        // then
        Optional<PointHistoryDto> foundHistory = pointHistoryDao.findByOrderId(orderId);
        assertThat(foundHistory).isPresent()
            .get()
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(pointHistoryDto);
    }

    @Test
    @DisplayName("존재하지 않는 주문의 포인트 적립 내역을 조회하면 빈 값이 반환된다.")
    void findByOrderId_notExistingId_fail() {
        // given
        long nonExistingOrderId = 2L;

        // when
        Optional<PointHistoryDto> pointHistoryEntity = pointHistoryDao.findByOrderId(
            nonExistingOrderId);

        // then
        assertThat(pointHistoryEntity).isEmpty();
    }

}
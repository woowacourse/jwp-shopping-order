package cart.order.dao;

import cart.init.DBInit;
import cart.order.dto.OrderInfoEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class OrderInfoDaoTest extends DBInit {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderInfoDao orderInfoDao;

    @BeforeEach
    void setUp() {
        orderInfoDao = new OrderInfoDao(jdbcTemplate);
    }

    @Test
    void 주문_상세를_저장한다() {
        // given
        final OrderInfoEntity orderInfoEntity =
                new OrderInfoEntity(null, 1L, 1L, "치킨", 10000L, "aa", 5L);

        // when
        final Long orderInfoId = orderInfoDao.insert(orderInfoEntity);

        // then
        assertThat(orderInfoId).isOne();
    }

    @Test
    void orderId로_주문_상세를_조회한다() {
        // given
        final OrderInfoEntity orderInfoEntity =
                new OrderInfoEntity(null, 1L, 1L, "치킨", 10000L, "aa", 5L);
        final Long orderInfoId = orderInfoDao.insert(orderInfoEntity);

        // when
        final List<OrderInfoEntity> orderInfoEntities = orderInfoDao.findByOrderId(orderInfoId);

        // then
        assertThat(orderInfoEntities).hasSize(1);
    }
}

package cart.dao;

import cart.domain.OrderInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static cart.ShoppingOrderFixture.chicken;
import static cart.ShoppingOrderFixture.pizza;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ActiveProfiles("test")
@JdbcTest
class OrderInfoDaoTest {

    private static final OrderInfo CHICKEN_ORDERINFO = new OrderInfo(1L, chicken, 2L);
    private static final OrderInfo PIZZA_ORDERINFO = new OrderInfo(1L, pizza, 3L);

    private OrderInfoDao orderInfoDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        orderInfoDao = new OrderInfoDao(jdbcTemplate);
    }

    @DisplayName("getOrderInfoById 테스트")
    @Test
    void getOrderInfoByIdTest() {
        Long orderInfoId = orderInfoDao.save(CHICKEN_ORDERINFO);
        OrderInfo orderInfo = orderInfoDao.getOrderInfoById(orderInfoId);

        assertSoftly(softly -> {
            softly.assertThat(orderInfo.getId()).isEqualTo(orderInfoId);
            softly.assertThat(orderInfo.getOrderId()).isEqualTo(CHICKEN_ORDERINFO.getOrderId());
            softly.assertThat(orderInfo.getProduct()).isEqualTo(CHICKEN_ORDERINFO.getProduct());
            softly.assertThat(orderInfo.getQuantity()).isEqualTo(CHICKEN_ORDERINFO.getQuantity());
        });
    }

    @DisplayName("getOrderInfoByOrderId 테스트")
    @Test
    void getOrderInfoByOrderIdTest() {
        orderInfoDao.save(CHICKEN_ORDERINFO);
        orderInfoDao.save(PIZZA_ORDERINFO);
        List<OrderInfo> orderInfos = orderInfoDao.getOrderInfoByOrderId(CHICKEN_ORDERINFO.getOrderId());

        assertSoftly(softly -> {
            softly.assertThat(orderInfos).hasSize(2);
            softly.assertThat(orderInfos.get(0).getProduct()).isEqualTo(chicken);
            softly.assertThat(orderInfos.get(1).getProduct()).isEqualTo(pizza);
        });
    }
}

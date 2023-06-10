package cart.persistence.repository;

import cart.entity.OrderInfo;
import cart.entity.OrderInfos;
import cart.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Sql({"classpath:truncate.sql", "classpath:data.sql"})
class OrderInfosPersistenceAdapterTest {

    private OrderInfosPersistenceAdapter orderInfosPersistenceAdapter;

    private Product product;

    @Autowired
    public OrderInfosPersistenceAdapterTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.orderInfosPersistenceAdapter = new OrderInfosPersistenceAdapter(namedParameterJdbcTemplate);
        this.product = new Product(1L, "고기", 10000, "https://", 10.0, true);

    }

    @Test
    @DisplayName("주문 ID를 통해 주문 정보를 찾을 수 있다")
    void findByOrderId() {
        // given, when
        OrderInfos orderInfos = new OrderInfos(List.of(
                new OrderInfo(1L, product, "고기", 10000, "https://", 1)));
        orderInfosPersistenceAdapter.insert(orderInfos, 100L);
        OrderInfos found = orderInfosPersistenceAdapter.findByOrderId(100L);
        // then
        assertThat(found).isNotNull();
    }
}

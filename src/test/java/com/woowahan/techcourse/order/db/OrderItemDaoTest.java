package com.woowahan.techcourse.order.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.woowahan.techcourse.order.domain.OrderItem;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class OrderItemDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final OrderItemDao orderItemDao;

    @Autowired
    public OrderItemDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        orderItemDao = new OrderItemDao(jdbcTemplate);
    }

    @BeforeEach
    void setOrder() {
        jdbcTemplate.execute(
                "INSERT INTO orders (id, member_id, actual_price, original_price) VALUES (1, 1, 9000, 10000)");
    }

    @Test
    void 여러개_저장하고_가져오는_기능이_정상적으로_작동한다() {
        OrderItem orderItem1 = new OrderItem(2, 10000L, 20000, "name1", "image");
        OrderItem orderItem2 = new OrderItem(2, 10000L, 20000, "name2", "image");

        orderItemDao.insertAll(1L, List.of(orderItem1, orderItem2));

        List<OrderItem> orderItems = orderItemDao.findAllByOrderId(1L);
        assertSoftly(softly -> {
            softly.assertThat(orderItems).hasSize(2);
            softly.assertThat(orderItems).usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(List.of(orderItem1, orderItem2));
        });
    }

    @Test
    void 없는_주문_번호일_경우_빈_데이터가_나온다() {
        OrderItem orderItem1 = new OrderItem(2, 10000L, 20000, "name1", "image");
        OrderItem orderItem2 = new OrderItem(2, 10000L, 20000, "name2", "image");

        orderItemDao.insertAll(1L, List.of(orderItem1, orderItem2));

        List<OrderItem> orderItems = orderItemDao.findAllByOrderId(2L);
        assertThat(orderItems).isEmpty();
    }
}

package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.CartItem;
import cart.entity.OrderEntity;
import cart.entity.OrderedItemEntity;
import cart.fixture.Fixture;

class OrderedItemDaoTest extends DaoTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    OrderDao orderDao;
    OrderedItemDao orderedItemDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        orderedItemDao = new OrderedItemDao(jdbcTemplate);
    }

    @Test
    @DisplayName("cartItems를 저장한다.")
    void saveAll() {
        //given
        final List<CartItem> items = List.of(Fixture.CART_ITEM1, Fixture.CART_ITEM2);
        final Long orderId = orderDao.save(new OrderEntity(10000, 1L));

        //when
        final int[] rows = orderedItemDao.saveAll(items, orderId);
        final int result = Arrays.stream(rows)
                .sum();

        //then
        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("orderId로 orderedItems를 조회한다.")
    void findItemsByOrderId() {
        //given
        final Long orderId = orderDao.save(new OrderEntity(10000, 1L));
        final List<CartItem> items = List.of(Fixture.CART_ITEM1, Fixture.CART_ITEM2);
        orderedItemDao.saveAll(items, orderId);

        //when
        final List<OrderedItemEntity> result = orderedItemDao.findByOrderId(orderId);

        //then
        Assertions.assertAll(
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.get(0).getQuantity()).isEqualTo(Fixture.CART_ITEM1.getQuantity()),
                () -> assertThat(result.get(1).getQuantity()).isEqualTo(Fixture.CART_ITEM2.getQuantity())
        );
    }
}

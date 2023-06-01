package cart.dao;

import cart.Fixture;
import cart.entity.OrderEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@JdbcTest
@Sql("classpath:schema.sql")
class OrderDaoTest {

    OrderDao orderDao;

    private OrderDaoTest(@Autowired final JdbcTemplate jdbcTemplate) {
        this.orderDao = new OrderDao(jdbcTemplate);
    }


    @DisplayName("주문 저장")
    @Test
    void insert() {
        // when & then
        assertThat(orderDao.insert(Fixture.order1)).isPositive();
    }

    @DisplayName("ID로 주문 조회")
    @Test
    void findById() {
        // given
        final Long id = orderDao.insert(Fixture.order1);

        // when
        final OrderEntity orderEntity = orderDao.findById(id);

        // then
        assertAll(
                () -> assertThat(orderEntity.getId()).isEqualTo(id),
                () -> assertThat(orderEntity.getMemberId()).isEqualTo(Fixture.order1.getMemberId()),
                () -> assertThat(orderEntity.getTotalPrice()).isEqualTo(Fixture.order1.getTotalPrice())
        );
    }

    @DisplayName("30개의 목록에서 내역을 인덱스로 조회한다")
    @Test
    void findByIndexRange() {
        // given
        for (int i = 0; i < 30; i++) {
            orderDao.insert(Fixture.order1);
        }

        // when
        final List<OrderEntity> orderEntitiesZero = orderDao.findByIndexRange(1L, 0L);
        final List<OrderEntity> orderEntitiesTen = orderDao.findByIndexRange(1L, 10L);
        final List<OrderEntity> orderEntitiesTwenty = orderDao.findByIndexRange(1L, 20L);

        // then
        assertAll(
                () -> assertThat(orderEntitiesZero.size()).isEqualTo(20),
                () -> assertThat(orderEntitiesTen.size()).isEqualTo(20),
                () -> assertThat(orderEntitiesTwenty.size()).isEqualTo(10),
                () -> assertThat(orderEntitiesZero.get(0).getId() - orderEntitiesZero.get(19).getId()).isEqualTo(19L)
        );
    }
}

package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.OrderEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class OrderDaoTest {

    private final OrderDao orderDao;

    @Autowired
    public OrderDaoTest(final JdbcTemplate jdbcTemplate) {
        this.orderDao = new OrderDao(jdbcTemplate);
    }

    @DisplayName("새 주문 정보를 DB에 저장한다")
    @Test
    void save() {
        // given, when
        final Long createdId = orderDao.save(new OrderEntity(1L, 3000L));

        // then
        assertThat(createdId).isNotNull();
    }


    @DisplayName("사용자 주문 정보 목록을 DB에서 최신순으로 조회한다.")
    @Test
    void findByMemberId() {
        // given
        final Long createdId = orderDao.save(new OrderEntity(1L, 3000L));
        final Long createdId2 = orderDao.save(new OrderEntity(1L, 3000L));

        // when
        final List<OrderEntity> found = orderDao.findByMemberId(1L);

        // then
        assertThat(found)
                .containsExactly(new OrderEntity(createdId2, 1L, 3000L), new OrderEntity(createdId, 1L, 3000L));
    }

    @DisplayName("특정 주문 상세 정보(상품 목록 제외)를 DB에서 조회한다.")
    @Test
    void findDetailById() {
        // given
        final long deliveryFee = 3000L;
        final long memberId = 1L;
        final Long createdId = orderDao.save(new OrderEntity(memberId, deliveryFee));

        // when
        final OrderEntity order = orderDao.find(createdId).get();

        // then
        assertThat(order).isEqualTo(new OrderEntity(createdId, memberId, deliveryFee));
    }


    @DisplayName("특정 주문 정보를 DB에서 삭제한다.")
    @Test
    void deleteById() {
        // given
        final Long createdId = orderDao.save(new OrderEntity(1L, 3000L));

        // when
        orderDao.deleteById(createdId);

        // then
        assertThat(orderDao.find(createdId).isEmpty()).isTrue();
    }
}

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
        final Long createdId = orderDao.save(new OrderEntity(1L, 1L, 3000L));

        // then
        assertThat(createdId).isNotNull();
    }


    @DisplayName("사용자 주문 정보 목록을 DB에서 최신순으로 조회한다.")
    @Test
    void findByMemberId() {
        // given
        final OrderEntity createEntity = new OrderEntity(1L, 1L, 3000L);
        final OrderEntity createEntity2 = new OrderEntity(1L, 2L, 3000L);
        final Long createdId = orderDao.save(createEntity);
        final Long createdId2 = orderDao.save(createEntity2);

        // when
        final List<OrderEntity> found = orderDao.findByMemberId(1L);

        // then
        final OrderEntity firstShown = found.get(0);
        final OrderEntity secondShown = found.get(1);
        assertThat(firstShown.getCouponId()).isEqualTo(2L);
        assertThat(secondShown.getCouponId()).isEqualTo(1L);
    }

    @DisplayName("특정 주문 상세 정보(상품 목록 제외)를 DB에서 조회한다.")
    @Test
    void findDetailById() {
        // given
        final OrderEntity create = new OrderEntity(1L, 1L, 3000L);
        final Long createdId = orderDao.save(create);

        // when
        final OrderEntity found = orderDao.findById(createdId).get();

        // then
        assertThat(found).isNotNull();
        assertThat(found.getMemberId()).isEqualTo(create.getMemberId());
        assertThat(found.getCouponId()).isEqualTo(create.getCouponId());
        assertThat(found.getDeliveryFee()).isEqualTo(create.getDeliveryFee());
        assertThat(found.getStatus()).isEqualTo(create.getStatus());
    }


    @DisplayName("특정 주문 정보를 DB에서 삭제한다.")
    @Test
    void deleteById() {
        // given
        final Long createdId = orderDao.save(new OrderEntity(1L, 1L, 3000L));

        // when
        orderDao.deleteById(createdId);

        // then
        assertThat(orderDao.findById(createdId).isEmpty()).isTrue();
    }
}

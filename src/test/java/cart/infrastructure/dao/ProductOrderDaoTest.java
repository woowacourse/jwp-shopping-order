package cart.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.OrderEntity;
import cart.entity.ProductEntity;
import cart.entity.ProductOrderEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ProductOrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ProductOrderDao productOrderDao;
    private Long productId;
    private Long orderId;

    @BeforeEach
    void setUp() {
        productOrderDao = new ProductOrderDao(jdbcTemplate);
        final ProductDao productDao = new ProductDao(jdbcTemplate);
        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        final OrderDao orderDao = new OrderDao(jdbcTemplate);
        productId = productDao.create(ProductEntity.of(1L, "망고", 1_000, "mango.png"));
        final Long memberId = memberDao.findById(1L).orElseThrow().getId();
        final OrderEntity orderEntity = new OrderEntity(null, memberId, null, 2_000, 1_000, 1_000, "서울특별시 송파구");
        orderId = orderDao.create(orderEntity, memberId);
    }

    @Test
    @DisplayName("상품 주문을 생성할 수 있다.")
    void testCreate() {
        // given
        final ProductOrderEntity productOrderEntity = ProductOrderEntity.of(null, productId, orderId, 2);

        // when
        productOrderDao.create(productOrderEntity);

        // then
        final List<ProductOrderEntity> result = productOrderDao.findAllByOrderId(orderId);
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result)
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(productOrderEntity))
        );
    }
}

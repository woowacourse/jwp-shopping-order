package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.domain.MemberEntity;
import cart.domain.OrderEntity;
import cart.domain.Product;
import cart.exception.OrderNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderDaoTest extends DaoTest {

    private MemberDao memberDao;
    private ProductDao productDao;
    private OrderDao orderDao;

    private final MemberEntity memberEntity = new MemberEntity(1L, "1@email.com", "11", 1_000);
    private final Product product = new Product(1L, "productName", 1_000, "imageUrl", 10);
    private OrderEntity orderEntity;

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
        this.orderDao = new OrderDao(jdbcTemplate);

        this.orderEntity = new OrderEntity(
                1L,
                LocalDateTime.now(),
                memberEntity.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                1,
                1_000,
                3_000,
                0,
                4_000
        );

        setDummyData();
    }

    private void setDummyData() {
        memberDao.insert(memberEntity);
        productDao.insert(product);
    }

    @Test
    void 주문을_삽입한다() {
        assertThatNoException().isThrownBy(
                () -> orderDao.insert(orderEntity)
        );
    }

    @Test
    void 주문을_ID로_조회한다() {
        // given
        long savedId = orderDao.insert(orderEntity);

        // when
        OrderEntity result = orderDao.findById(savedId);

        // then
        assertThat(result).isEqualTo(orderEntity);
    }

    @Test
    void 모든_주문을_조회한다() {
        // given
        long savedId = orderDao.insert(orderEntity);

        // when
        List<OrderEntity> result = orderDao.findAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result).containsOnly(orderEntity);
    }

    @Test
    void 주문을_수정한다() {
        // given
        long savedId = orderDao.insert(orderEntity);

        String expectName = "newName";
        String expectImageUrl = "newImageUrl";
        int expectValue = 100_000;

        OrderEntity newOrderEntity = new OrderEntity(
                orderEntity.getId(),
                orderEntity.getDateTime(),
                orderEntity.getMemberId(),
                orderEntity.getProductId(),
                expectName,
                expectValue,
                expectImageUrl,
                expectValue,
                expectValue,
                expectValue,
                expectValue,
                expectValue
        );

        // when
        orderDao.update(newOrderEntity);

        // then
        OrderEntity result = orderDao.findById(newOrderEntity.getId());
        assertThat(result.getProductName()).isEqualTo(expectName);
        assertThat(result.getProductPrice()).isEqualTo(expectValue);
        assertThat(result.getProductImageUrl()).isEqualTo(expectImageUrl);
        assertThat(result.getQuantity()).isEqualTo(expectValue);
        assertThat(result.getTotalProductPrice()).isEqualTo(expectValue);
        assertThat(result.getTotalDeliveryFee()).isEqualTo(expectValue);
        assertThat(result.getUsePoint()).isEqualTo(expectValue);
        assertThat(result.getTotalPrice()).isEqualTo(expectValue);
    }

    @Test
    void ID로_주문을_삭제한다() {
        // given
        long savedId = orderDao.insert(orderEntity);

        // when
        orderDao.deleteById(savedId);

        // then
        assertThatThrownBy(() -> orderDao.findById(savedId))
                .isInstanceOf(OrderNotFoundException.class);
    }
}

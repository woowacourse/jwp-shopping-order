package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.Product;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class OrderItemDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;
    private OrderItemDao orderItemDao;
    private MemberDao memberDao;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        orderItemDao = new OrderItemDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    @DisplayName("주문 상품 정보를 저장한다.")
    void create() {
        //given
        final List<Product> products = productDao.findAll();
        final Member member = memberDao.findByEmail("kangsj9665@gmail.com");
        final Long orderId = orderDao.create(OrderEntity.toCreate(member.getId(), 300, 400));
        final List<OrderItemEntity> orderItemEntities = products.stream()
                .map(product -> OrderItemEntity.toCreate(product.getId(), orderId, 2))
                .collect(Collectors.toUnmodifiableList());

        //when
        orderItemDao.create(orderItemEntities);

        //then
        final List<OrderItemEntity> savedOrderEntities = orderItemDao.findByOrderId(orderId);
        final Long[] productIds = products.stream()
                .map(Product::getId)
                .toArray(Long[]::new);

        assertThat(savedOrderEntities).extracting(OrderItemEntity::getProductId)
                .containsExactlyInAnyOrder(productIds);
    }
}

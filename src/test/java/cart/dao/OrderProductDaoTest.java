package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderProductEntity;
import cart.entity.ProductEntity;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class OrderProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderProductDao orderProductDao;

    private Long memberId;
    private Long pizzaId;
    private Long chickenId;
    private Long hamburgerId;
    private Long orderId;

    @BeforeEach
    void setUp() {
        orderProductDao = new OrderProductDao(jdbcTemplate);

        MemberDao memberDao = new MemberDao(jdbcTemplate);
        memberId = memberDao.save(new MemberEntity("email@email.com", "password"));

        ProductDao productDao = new ProductDao(jdbcTemplate);
        pizzaId = productDao.save(new ProductEntity("피자", BigDecimal.valueOf(10000), "http://pizza.com"));
        chickenId = productDao.save(new ProductEntity("치킨", BigDecimal.valueOf(20000), "http://chicken.com"));
        hamburgerId = productDao.save(new ProductEntity("햄버거", BigDecimal.valueOf(30000), "http://hamburger.com"));

        OrderDao orderDao = new OrderDao(jdbcTemplate);
        orderId = orderDao.save(new OrderEntity(memberId, 3000));
    }

    @Test
    void 주문상품을_모두_저장한다() {
        // given
        List<OrderProductEntity> orderProductEntities = List.of(
                new OrderProductEntity(orderId, pizzaId, 1, "피자", BigDecimal.valueOf(10000), "http://pizza.com"),
                new OrderProductEntity(orderId, chickenId, 2, "치킨", BigDecimal.valueOf(20000),
                        "http://chicken.com"),
                new OrderProductEntity(orderId, hamburgerId, 3, "햄버거", BigDecimal.valueOf(30000),
                        "http://hamburger.com")
        );

        // when
        orderProductDao.saveAll(orderProductEntities);

        // then
        assertThat(orderProductDao.findAll()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(orderProductEntities);
    }
}

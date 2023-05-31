package cart.repository;

import cart.dao.MemberDao;
import cart.dao.MemberEntity;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql("classpath:test.sql")
class OrderRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderRepository orderRepository;
    private Member huchu;
    private Product chicken;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository(new OrderDao(jdbcTemplate));

        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        final ProductDao productDao = new ProductDao(jdbcTemplate);

        final Long memberId = memberDao.addMember(new MemberEntity("huchu@woowahan.com", "1234567a!", 1000));
        final Long productId = productDao.createProduct(new Product("chicken", 20000, "chicken.jpeg"));

        huchu = new Member(memberId, "huchu@woowahan.com", "1234567a!", 1000);
        chicken = new Product(productId, "chicken", 20000, "chicken.jpeg");
    }

    @Test
    void 주문을_추가한다() {
        //given
        final Order order = Order.from(huchu, 19000, 1000, List.of(new OrderItem(chicken, 1)));

        //when
        final Long id = orderRepository.addOrder(order);

        //then
        assertThat(id).isEqualTo(1L);
    }
}

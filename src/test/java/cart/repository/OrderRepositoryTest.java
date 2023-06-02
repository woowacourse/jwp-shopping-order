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
@Sql(value = "classpath:test_truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderRepository orderRepository;
    private Member member;
    private Product product;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository(new OrderDao(jdbcTemplate), new ProductDao(jdbcTemplate));

        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        final ProductDao productDao = new ProductDao(jdbcTemplate);

        final Long memberId = memberDao.addMember(new MemberEntity("huchu@woowahan.com", "1234567a!", 1000));
        final Long productId = productDao.createProduct(new Product("chicken", 20000, "chicken.jpeg"));

        member = new Member(memberId, "huchu@woowahan.com", "1234567a!", 1000);
        product = new Product(productId, "chicken", 20000, "chicken.jpeg");
    }

    @Test
    void 주문을_추가한다() {
        //given
        final Order order = Order.from(member, 19000, 1000, List.of(new OrderItem(product, 1)));

        //when
        final Long id = orderRepository.addOrder(order);

        //then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 주문_id로_회원의_주문을_얻는다() {
        //given
        final Long id = orderRepository.addOrder(Order.from(member, 19000, 1000, List.of(new OrderItem(product, 1))));

        //when
        final Order order = orderRepository.getOrderById(member, id);

        //then
        assertThat(order).isEqualTo(Order.from(member, 19000, 1000, List.of(new OrderItem(product, 1))));
    }

    @Test
    void 회원의_주문_목록을_얻는다() {
        //given
        orderRepository.addOrder(Order.from(member, 19000, 1000, List.of(new OrderItem(product, 1))));

        //when
        final List<Order> orders = orderRepository.getAllOrders(member);

        //then
        assertThat(orders).isEqualTo(List.of(Order.from(member, 19000, 1000, List.of(new OrderItem(product, 1)))));
    }
}

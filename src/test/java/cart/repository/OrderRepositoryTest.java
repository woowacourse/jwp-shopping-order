package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.ProductEntity;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Product;
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
class OrderRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderRepository orderRepository;
    private MemberDao memberDao;
    private ProductDao productDao;

    private final ProductEntity 첫번째_상품_엔티티 = new ProductEntity(null, "치킨", 10_000, "http://example/chicken.png");
    private final ProductEntity 두번째_상품_엔티티 = new ProductEntity(null, "피자", 20_000, "http://example/chicken.png");
    private final MemberEntity 멤버_엔티티 = new MemberEntity(
            null, "vero@email", "password", 20000, null, null
    );

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);

        OrderDao orderDao = new OrderDao(jdbcTemplate);
        OrderProductDao orderProductDao = new OrderProductDao(jdbcTemplate);
        OrderProductRepository orderProductRepository = new OrderProductRepository(orderProductDao);

        orderRepository = new OrderRepository(orderDao, orderProductRepository);
    }

    @Test
    void 주문_ID로_주문을_조회한다() {
        // given
        Product 첫번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Product 두번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(두번째_상품_엔티티);
        OrderProduct 주문_상품 = new OrderProduct(첫번째_상품, 2);
        OrderProduct 두번째_주문_상품 = new OrderProduct(두번째_상품, 3);
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);
        Order 주문 = new Order(null, List.of(주문_상품, 두번째_주문_상품), 멤버, 200);

        // when
        Long 저장된_주문 = orderRepository.save(주문);

        // then
        assertAll(
                () -> assertThat(orderRepository.findById(저장된_주문, 멤버)).isNotNull(),
                () -> assertThat(orderRepository.findOrdersByMember(멤버)).hasSize(1),
                () -> assertThat(orderRepository.findOrdersByMember(멤버).get(0).getOrderProducts()).hasSize(2)
        );
    }

    private Member 멤버를_저장하고_ID가_있는_멤버를_리턴한다(MemberEntity 멤버_엔티티) {
        Long 멤버_ID = memberDao.save(멤버_엔티티);

        return new Member(멤버_ID, 멤버_엔티티.getEmail(), 멤버_엔티티.getPassword(), 멤버_엔티티.getPoint());
    }

    private Product 상품을_저장하고_ID를_갖는_상품을_리턴한다(ProductEntity 상품_엔티티) {
        Long 저장된_상품_ID = productDao.save(상품_엔티티);
        return new Product(저장된_상품_ID, 상품_엔티티.getName(), 상품_엔티티.getPrice(), 상품_엔티티.getImageUrl());
    }

    @Test
    void 멤버의_전체_주문을_조회한다() {
        // given
        Product 첫번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(첫번째_상품_엔티티);
        Product 두번째_상품 = 상품을_저장하고_ID를_갖는_상품을_리턴한다(두번째_상품_엔티티);
        OrderProduct 주문_상품 = new OrderProduct(첫번째_상품, 2);
        OrderProduct 두번째_주문_상품 = new OrderProduct(두번째_상품, 3);
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(멤버_엔티티);

        Order 주문 = new Order(null, List.of(주문_상품, 두번째_주문_상품), 멤버, 200);
        Order 두번째_주문 = new Order(null, List.of(주문_상품), 멤버, 200);
        Long 저장된_주문_ID = orderRepository.save(주문);
        Long 저장된_두번째_주문_ID = orderRepository.save(두번째_주문);

        // when
        List<Order> 주문들 = orderRepository.findOrdersByMember(멤버);
        Order 저장된_주문 = orderRepository.findById(저장된_주문_ID, 멤버);
        Order 저장된_두번째_주문 = orderRepository.findById(저장된_두번째_주문_ID, 멤버);

        List<Order> 맞는_결과 = List.of(저장된_주문, 저장된_두번째_주문);

        // then
        assertAll(

                () -> assertThat(주문들).hasSize(2),
                () -> assertThat(주문들).usingRecursiveComparison()
                        .isEqualTo(맞는_결과)
        );
    }
}

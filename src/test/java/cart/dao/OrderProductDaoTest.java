package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Product;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class OrderProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;
    private ProductDao productDao;
    private OrderDao orderDao;

    private OrderProductDao orderProductDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);

        orderProductDao = new OrderProductDao(jdbcTemplate);
    }

    @Test
    void 주문_상품_전체_저장_테스트() {
        //given
        final Member memberFixture = getSavedMemberFixture("testMember");
        final Product testProductA = getSavedProductFixture("testProductA");
        final Product testProductB = getSavedProductFixture("testProductB");
        final Order orderFixture = getSavedOrderFixture(memberFixture);
        final List<OrderProduct> orderProducts = List.of(
                new OrderProduct(orderFixture, testProductA, 3),
                new OrderProduct(orderFixture, testProductB, 1)
        );

        //when
        orderProductDao.saveOrderProducts(orderProducts);

        //then
        final List<OrderProduct> all = orderProductDao.findAll();
        assertThat(all).hasSize(2);
        assertThat(all.get(0).getProduct().getName()).isEqualTo("testProductA");
        assertThat(all.get(0).getOrder().getMember().getEmail()).isEqualTo("testMember");
    }

    @Test
    void 주문으로_상품_조회_테스트() {
        //given
        final Member memberFixture = getSavedMemberFixture("testMember");
        final Product testProductA = getSavedProductFixture("testProductA");
        final Order orderFixtureA = getSavedOrderFixture(memberFixture);
        final Order orderFixtureB = getSavedOrderFixture(memberFixture);
        final List<OrderProduct> orderProducts = List.of(
                new OrderProduct(orderFixtureA, testProductA, 3),
                new OrderProduct(orderFixtureB, testProductA, 4)
        );
        orderProductDao.saveOrderProducts(orderProducts);

        //when
        final List<OrderProduct> byOrderId = orderProductDao.findByOrderId(orderFixtureA.getId());

        //then
        assertThat(byOrderId).hasSize(1);
        assertThat(byOrderId.get(0).getOrder().getId()).isEqualTo(orderFixtureA.getId());
    }

    @Test
    void 사용자_주문_상품_조회_테스트() {
        //given
        final Member memberFixtureA = getSavedMemberFixture("testMemberA");
        final Member memberFixtureB = getSavedMemberFixture("testMemberB");
        final Product testProductA = getSavedProductFixture("testProductA");
        final Order orderFixtureA = getSavedOrderFixture(memberFixtureA);
        final Order orderFixtureB = getSavedOrderFixture(memberFixtureB);
        final List<OrderProduct> orderProducts = List.of(
                new OrderProduct(orderFixtureA, testProductA, 3),
                new OrderProduct(orderFixtureB, testProductA, 4)
        );
        orderProductDao.saveOrderProducts(orderProducts);

        //when
        final List<OrderProduct> byOrderId = orderProductDao.findByMemberId(memberFixtureA.getId());

        //then
        assertThat(byOrderId).hasSize(1);
        assertThat(byOrderId.get(0).getOrder().getMember().getId()).isEqualTo(memberFixtureA.getId());
    }

    private Member getSavedMemberFixture(final String email) {
        final Member member = new Member(email, "password");
        final Long saveMemberId = memberDao.saveMember(member);

        return new Member(saveMemberId, member.getEmail(), member.getPassword());
    }

    private Product getSavedProductFixture(final String productName) {
        final Product product = new Product(productName, 3_000, "testImage");
        final Long savedProductId = productDao.saveProduct(product);

        return new Product(savedProductId, product.getName(), product.getPrice(), product.getImageUrl());
    }

    private Order getSavedOrderFixture(final Member member) {
        final Order order = new Order(member, 3_000, 3_000);
        final Long saveOrderId = orderDao.saveOrder(order);

        return new Order(saveOrderId, null, member, order.getTotalPrice(), order.getFinalPrice());
    }
}

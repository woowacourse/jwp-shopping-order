package cart.repository;

import static cart.fixture.JdbcTemplateFixture.insertCoupon;
import static cart.fixture.JdbcTemplateFixture.insertMember;
import static cart.fixture.JdbcTemplateFixture.insertMemberCoupon;
import static cart.fixture.JdbcTemplateFixture.insertOrder;
import static cart.fixture.JdbcTemplateFixture.insertOrderCoupon;
import static cart.fixture.JdbcTemplateFixture.insertOrderItem;
import static cart.fixture.JdbcTemplateFixture.insertProduct;
import static cart.fixture.MemberFixture.MEMBER;
import static cart.fixture.ProductFixture.CHICKEN;
import static cart.fixture.ProductFixture.PIZZA;

import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.MemberCoupon;
import cart.domain.Order;
import cart.domain.Product;
import cart.entity.OrderCouponEntity;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderRepository 은(는)")
@JdbcTest(includeFilters = {
        @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class OrderRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderRepository orderRepository;


    @Test
    void 아이디를_통해_주문을_찾는다() {
        // given
        OrderEntity orderEntity = new OrderEntity(1L, MEMBER.getId());
        OrderItemEntity orderItemChicken = getOrderItemEntity(1L, orderEntity.getId(), CHICKEN, 10);
        OrderItemEntity orderItemPizza = getOrderItemEntity(2L, orderEntity.getId(), PIZZA, 5);
        Coupon coupon = new Coupon(1L, "10% 쿠폰", CouponType.RATE, 10);
        insertMember(MEMBER, jdbcTemplate);
        insertProduct(CHICKEN, jdbcTemplate);
        insertProduct(PIZZA, jdbcTemplate);
        insertCoupon(coupon, jdbcTemplate);
        MemberCoupon memberCouponA = new MemberCoupon(1L, MEMBER.getId(), coupon, false);
        MemberCoupon memberCouponB = new MemberCoupon(2L, MEMBER.getId(), coupon, false);
        insertMemberCoupon(memberCouponA, jdbcTemplate);
        insertMemberCoupon(memberCouponB, jdbcTemplate);

        insertOrder(orderEntity, jdbcTemplate);
        insertOrderItem(orderItemChicken, jdbcTemplate);
        insertOrderItem(orderItemPizza, jdbcTemplate);
        insertOrderCoupon(new OrderCouponEntity(orderItemChicken.getId(), memberCouponA.getId()), jdbcTemplate);
        insertOrderCoupon(new OrderCouponEntity(orderItemPizza.getId(), memberCouponB.getId()), jdbcTemplate);

        // when
        Order order = orderRepository.findById(1L);

        // then
        //TODO
    }

    private OrderItemEntity getOrderItemEntity(Long id, Long orderId, Product product, int quantity) {
        return new OrderItemEntity(id, orderId, product.getId(), quantity, product.getName(), product.getPrice(),
                product.getImageUrl(), 50000);
    }
}

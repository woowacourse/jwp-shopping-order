package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import cart.application.dto.coupon.CreateCouponRequest;
import cart.application.dto.coupon.IssueCouponRequest;
import cart.application.dto.order.CreateOrderByCartItemsRequest;
import cart.application.dto.order.FindOrderCouponsResponse;
import cart.application.dto.order.FindOrderDetailResponse;
import cart.application.dto.order.FindOrdersResponse;
import cart.application.dto.order.OrderCouponResponse;
import cart.application.dto.order.OrderDetailProductResponse;
import cart.application.dto.order.OrderProductRequest;
import cart.application.dto.order.OrderProductResponse;
import cart.application.dto.order.OrderResponse;
import cart.application.dto.product.ProductRequest;
import cart.domain.Member;
import cart.domain.coupon.CouponType;
import cart.persistence.dao.MemberCouponDao;
import cart.persistence.dao.MemberDao;
import cart.persistence.entity.MemberEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberCouponDao memberCouponDao;

    private Long productId;
    private Long productId2;
    private Member member;
    private Member other;
    private Long cartItemId1;
    private Long cartItemId2;
    private Long couponId1;
    private Long couponId2;

    @BeforeEach
    void setUp() {
        super.setUp();

        productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        long memberId = memberDao.create(new MemberEntity("test@test.com", "test!", "testNickname"));
        long otherId = memberDao.create(new MemberEntity("other@test.com", "test!", "otherNickname"));
        member = new Member(memberId, "test@test.com", "test!", "testNickname");
        other = new Member(otherId, "other@test.com", "test!", "otherNickname");

        cartItemId1 = requestAddCartItemAndGetId(member, productId);
        cartItemId2 = requestAddCartItemAndGetId(member, productId2);

        couponId1 = createCoupon(
                new CreateCouponRequest("10%(10,000~)", 10_000, 3000, CouponType.FIXED_PERCENTAGE, 10));
        couponId2 = createCoupon(
                new CreateCouponRequest("1000원(50,000~)", 50_000, 1000, CouponType.FIXED_AMOUNT, 10_000));

    }

    @Test
    void 장바구니_아이디로_사용자_쿠폰을_조회한다() {
        LocalDateTime pastDate = LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime futureDate = LocalDateTime.of(9999, 12, 31, 0, 0);

        long memberCouponId1 = issueCoupon(member, couponId1, new IssueCouponRequest(futureDate));
        long memberCouponId2 = issueCoupon(member, couponId2, new IssueCouponRequest(futureDate));
        long memberCouponId3 = issueCoupon(member, couponId2, new IssueCouponRequest(futureDate));
        issueCoupon(member, couponId2, new IssueCouponRequest(pastDate));
        issueCoupon(other, couponId2, new IssueCouponRequest(futureDate));
        memberCouponDao.changeCouponStatus(memberCouponId3, true);

        FindOrderCouponsResponse response = findCouponsByCartItemIds(member, List.of(cartItemId1, cartItemId2));

        List<OrderCouponResponse> expected = List.of(
                new OrderCouponResponse(memberCouponId1, "10%(10,000~)", 10_000, 3000, true, 2500, futureDate),
                new OrderCouponResponse(memberCouponId2, "1000원(50,000~)", 50_000, 1000, false, null, futureDate)
        );

        assertThat(response.getCoupons())
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @Test
    void 장바구니_상품을_주문_및_조회한다() {

        LocalDateTime futureDate = LocalDateTime.of(9999, 12, 31, 0, 0);
        requestUpdateCartItemQuantity(member, cartItemId1, 3);
        long memberCouponId1 = issueCoupon(member, couponId1, new IssueCouponRequest(futureDate));

        CreateOrderByCartItemsRequest request = new CreateOrderByCartItemsRequest(
                memberCouponId1,
                List.of(
                        new OrderProductRequest(cartItemId1, 3, "치킨", 10_000, "http://example.com/chicken.jpg"),
                        new OrderProductRequest(cartItemId2, 1, "피자", 15_000, "http://example.com/pizza.jpg")
                )
        );

        long orderId = orderCartItems(member, request);
        FindOrderDetailResponse response = findOrderById(member, orderId);

        FindOrderDetailResponse expected = new FindOrderDetailResponse(orderId, List.of(
                new OrderDetailProductResponse(productId, "치킨", "http://example.com/chicken.jpg", 10_000, 3),
                new OrderDetailProductResponse(productId2, "피자", "http://example.com/pizza.jpg", 15_000, 1)),
                45000, 4500, 0);

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @Test
    void 장바구니_상품을_쿠폰없이_주문_및_조회한다() {
        requestUpdateCartItemQuantity(member, cartItemId1, 3);

        CreateOrderByCartItemsRequest request = new CreateOrderByCartItemsRequest(
                null,
                List.of(
                        new OrderProductRequest(cartItemId1, 3, "치킨", 10_000, "http://example.com/chicken.jpg"),
                        new OrderProductRequest(cartItemId2, 1, "피자", 15_000, "http://example.com/pizza.jpg")
                )
        );

        long orderId = orderCartItems(member, request);
        FindOrderDetailResponse response = findOrderById(member, orderId);

        FindOrderDetailResponse expected = new FindOrderDetailResponse(orderId, List.of(
                new OrderDetailProductResponse(productId, "치킨", "http://example.com/chicken.jpg", 10_000, 3),
                new OrderDetailProductResponse(productId2, "피자", "http://example.com/pizza.jpg", 15_000, 1)),
                45000, 0, 0);

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @Test
    void 배송비_유료_주문을_한다() {
        CreateOrderByCartItemsRequest request = new CreateOrderByCartItemsRequest(
                null,
                List.of(
                        new OrderProductRequest(cartItemId1, 1, "치킨", 10_000, "http://example.com/chicken.jpg"),
                        new OrderProductRequest(cartItemId2, 1, "피자", 15_000, "http://example.com/pizza.jpg")
                )
        );

        long orderId = orderCartItems(member, request);
        FindOrderDetailResponse response = findOrderById(member, orderId);

        FindOrderDetailResponse expected = new FindOrderDetailResponse(orderId, List.of(
                new OrderDetailProductResponse(productId, "치킨", "http://example.com/chicken.jpg", 10_000, 1),
                new OrderDetailProductResponse(productId2, "피자", "http://example.com/pizza.jpg", 15_000, 1)),
                25000, 0, 3000);

        assertThat(response)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @Test
    void 전체_주문목록을_조회한다() {
        LocalDateTime futureDate = LocalDateTime.of(9999, 12, 31, 0, 0);
        requestUpdateCartItemQuantity(member, cartItemId1, 3);
        long memberCouponId1 = issueCoupon(member, couponId1, new IssueCouponRequest(futureDate));

        CreateOrderByCartItemsRequest request1 = new CreateOrderByCartItemsRequest(
                memberCouponId1,
                List.of(new OrderProductRequest(cartItemId1, 3, "치킨", 10_000, "http://example.com/chicken.jpg"))
        );

        CreateOrderByCartItemsRequest request2 = new CreateOrderByCartItemsRequest(
                null,
                List.of(new OrderProductRequest(cartItemId2, 1, "피자", 15_000, "http://example.com/pizza.jpg"))
        );

        long orderId1 = orderCartItems(member, request1);
        long orderId2 = orderCartItems(member, request2);

        FindOrdersResponse response = findOrders(member);
        FindOrdersResponse expected = new FindOrdersResponse(List.of(
                new OrderResponse(orderId1,
                        List.of(new OrderProductResponse(productId, "치킨", 10_000, "http://example.com/chicken.jpg",
                                3))),
                new OrderResponse(orderId2,
                        List.of(new OrderProductResponse(productId2, "피자", 15_000, "http://example.com/pizza.jpg", 1)))
        ));

        assertThat(response.getOrders())
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected.getOrders());
    }
}

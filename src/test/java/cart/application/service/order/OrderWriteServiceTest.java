package cart.application.service.order;

import cart.application.repository.CartItemRepository;
import cart.application.repository.CouponRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.PointRepository;
import cart.application.repository.ProductRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.repository.order.OrderedItemRepository;
import cart.domain.PointHistory;
import cart.domain.Product;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.exception.OverFullPointException;
import cart.fixture.MemberFixture;
import cart.fixture.ProductFixture;
import cart.ui.MemberAuth;
import cart.ui.order.dto.CreateOrderDto;
import cart.ui.order.dto.request.CreateOrderDiscountRequest;
import cart.ui.order.dto.request.CreateOrderItemRequest;
import cart.ui.order.dto.request.CreateOrderRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static cart.fixture.MemberFixture.레오;
import static cart.fixture.MemberFixture.비버;
import static cart.fixture.ProductFixture.배변패드;
import static cart.fixture.ProductFixture.통구이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderWriteServiceTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    OrderWriteService orderWriteService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderedItemRepository orderedItemRepository;

    @Autowired
    PointRepository pointRepository;

    private long leoId;
    private long dinoId;
    private long beaverId;

    private long bbqProductId;
    private long padProductId;
    private long tailProductId;

    private long usablePercentCouponId;
    private long usableAmountCouponId;

    private long padCartItemId;
    private long bbqCartItemId;

    Product bbq;
    Product pad;

    @BeforeEach
    void setUp() {
        leoId = memberRepository.createMember(레오);
        dinoId = memberRepository.createMember(MemberFixture.디노);

        createCoupon();
        createProduct();
        createCartItem();
    }

    @Test
    @DisplayName("정상적인 주문")
    void orderTest() {
        Member leo = new Member(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        CartItem bbqCart = new CartItem(3, bbq, leo);
        CartItem padCart = new CartItem(2, pad, leo);

        bbqCartItemId = cartItemRepository.createCartItem(bbqCart);
        padCartItemId = cartItemRepository.createCartItem(padCart);

        // 1000원 * 3
        CreateOrderItemRequest bbqCartRequest = new CreateOrderItemRequest(bbqCartItemId, bbqProductId, bbqCart.getQuantity());
        // 10000원 * 2
        CreateOrderItemRequest padCartRequest = new CreateOrderItemRequest(padCartItemId, padProductId, padCart.getQuantity());
        CreateOrderDiscountRequest discountRequest = new CreateOrderDiscountRequest(List.of(usablePercentCouponId), 3000);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(bbqCartRequest, padCartRequest), discountRequest);
        CreateOrderDto createOrderDto = CreateOrderDto.from(createOrderRequest);

        레오_포인트_15000적립();

        MemberAuth leoAuth = new MemberAuth(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        Long orderId = orderWriteService.createOrder(leoAuth, createOrderDto);

        Optional<Order> orderFindResult = orderRepository.findOrderBy(orderId);

        Assertions.assertAll(
                () -> assertThat(orderId).isPositive(),
                () -> assertThat(cartItemRepository.findById(bbqCartItemId)).isEmpty(),
                () -> assertThat(orderedItemRepository.findOrderItemsByOrderId(orderId)).hasSize(2),
                () -> assertThat(orderFindResult).isNotEmpty(),
                () -> assertThat(orderFindResult.get().getTotalPrice()).isEqualTo(23000),
                // 23,000 10%할인 3000포인트 사용
                () -> assertThat(orderFindResult.get().getPaymentPrice()).isEqualTo(17700),
                () -> assertThat(couponRepository.findUsableCouponByMemberCouponId(usablePercentCouponId)).isEmpty()
        );
    }

    @Test
    @DisplayName("주문하려는 장바구니가 본인 소유가 아니면 예외 발생")
    void invalidOwnerOrderTest() {
        Member leo = new Member(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        CartItem bbqCart = new CartItem(3, bbq, leo);
        CartItem padCart = new CartItem(2, pad, leo);

        bbqCartItemId = cartItemRepository.createCartItem(bbqCart);
        padCartItemId = cartItemRepository.createCartItem(padCart);

        // 1000원 * 3
        CreateOrderItemRequest bbqCartRequest = new CreateOrderItemRequest(bbqCartItemId, bbqProductId, bbqCart.getQuantity());
        // 10000원 * 2
        CreateOrderItemRequest padCartRequest = new CreateOrderItemRequest(padCartItemId, padProductId, padCart.getQuantity());
        CreateOrderDiscountRequest discountRequest = new CreateOrderDiscountRequest(List.of(usablePercentCouponId), 3000);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(bbqCartRequest, padCartRequest), discountRequest);
        CreateOrderDto createOrderDto = CreateOrderDto.from(createOrderRequest);

        MemberAuth leoAuth = new MemberAuth(beaverId, 비버.getName(), 비버.getEmail(), 비버.getPassword());
        assertThatThrownBy(() -> orderWriteService.createOrder(leoAuth, createOrderDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("장바구니 소유자가 올바르지 않습니다.");
    }

    @Test
    @DisplayName("주문하려는 장바구니 아이템이 아무것도 없으면 예외발생")
    void nonExistCartItemOrderTest() {
        CreateOrderDiscountRequest discountRequest = new CreateOrderDiscountRequest(List.of(), 0);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(), discountRequest);
        CreateOrderDto createOrderDto = CreateOrderDto.from(createOrderRequest);

        MemberAuth leoAuth = new MemberAuth(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        assertThatThrownBy(() -> orderWriteService.createOrder(leoAuth, createOrderDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문하려는 상품이 0개입니다.");
    }

    @Test
    @DisplayName("사용하려는 포인트보다 실제 포인트가 적으면 예외발생")
    void invalidPointOrderTest() {
        Member leo = new Member(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        CartItem bbqCart = new CartItem(3, bbq, leo);
        CartItem padCart = new CartItem(2, pad, leo);

        bbqCartItemId = cartItemRepository.createCartItem(bbqCart);
        padCartItemId = cartItemRepository.createCartItem(padCart);

        // 1000원 * 3
        CreateOrderItemRequest bbqCartRequest = new CreateOrderItemRequest(bbqCartItemId, bbqProductId, bbqCart.getQuantity());
        // 10000원 * 2
        CreateOrderItemRequest padCartRequest = new CreateOrderItemRequest(padCartItemId, padProductId, padCart.getQuantity());
        CreateOrderDiscountRequest discountRequest = new CreateOrderDiscountRequest(List.of(10L), 100);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(bbqCartRequest, padCartRequest), discountRequest);
        CreateOrderDto createOrderDto = CreateOrderDto.from(createOrderRequest);

        MemberAuth leoAuth = new MemberAuth(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());

        assertThatThrownBy(() -> orderWriteService.createOrder(leoAuth, createOrderDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잔여 포인트가 부족합니다.");
    }

    @Test
    @DisplayName("사용할 수 없는 쿠폰 사용 시 예외 발생")
    void invalidCouponOrderTest() {
        Member leo = new Member(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        CartItem bbqCart = new CartItem(3, bbq, leo);
        CartItem padCart = new CartItem(2, pad, leo);

        bbqCartItemId = cartItemRepository.createCartItem(bbqCart);
        padCartItemId = cartItemRepository.createCartItem(padCart);

        // 1000원 * 3
        CreateOrderItemRequest bbqCartRequest = new CreateOrderItemRequest(bbqCartItemId, bbqProductId, bbqCart.getQuantity());
        // 10000원 * 2
        CreateOrderItemRequest padCartRequest = new CreateOrderItemRequest(padCartItemId, padProductId, padCart.getQuantity());
        CreateOrderDiscountRequest discountRequest = new CreateOrderDiscountRequest(List.of(10L), 3000);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(bbqCartRequest, padCartRequest), discountRequest);
        CreateOrderDto createOrderDto = CreateOrderDto.from(createOrderRequest);

        레오_포인트_15000적립();
        MemberAuth leoAuth = new MemberAuth(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        assertThatThrownBy(() -> orderWriteService.createOrder(leoAuth, createOrderDto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("유효하지 않은 쿠폰입니다.");
    }

    @Test
    @DisplayName("최종 결제금액 보다 사용하려는 포인트가 많으면 예외 발생")
    void overPointOrderTest() {
        Member leo = new Member(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        CartItem bbqCart = new CartItem(15, bbq, leo);

        bbqCartItemId = cartItemRepository.createCartItem(bbqCart);

        // 1000원 * 15
        CreateOrderItemRequest bbqCartRequest = new CreateOrderItemRequest(bbqCartItemId, bbqProductId, bbqCart.getQuantity());
        // 최소주문 금액 15000, 3000원 할인 쿠폰
        CreateOrderDiscountRequest discountRequest = new CreateOrderDiscountRequest(List.of(usableAmountCouponId), 13000);
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(List.of(bbqCartRequest), discountRequest);
        CreateOrderDto createOrderDto = CreateOrderDto.from(createOrderRequest);

        레오_포인트_15000적립();

        MemberAuth leoAuth = new MemberAuth(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());
        assertThatThrownBy(() -> orderWriteService.createOrder(leoAuth, createOrderDto))
                .isInstanceOf(OverFullPointException.class)
                .hasMessage("사용하려는 포인트가 결제 예상 금액보다 큽니다.");
    }

    private void 레오_포인트_15000적립() {
        PointHistory pointHistory = new PointHistory(5L, 15000, 0);
        pointRepository.createPointHistory(leoId, pointHistory);
    }


    private void createCoupon() {
        SimpleJdbcInsert simpleCouponJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coupon")
                .usingGeneratedKeyColumns("id");
        MapSqlParameterSource welcomeCoupon = new MapSqlParameterSource()
                .addValue("name", "웰컴 쿠폰 - 10%할인")
                .addValue("min_amount", 10000)
                .addValue("discount_percent", 10)
                .addValue("discount_amount", 0);

        MapSqlParameterSource againCoupon = new MapSqlParameterSource()
                .addValue("name", "또 와요 쿠폰 - 3000원 할인")
                .addValue("min_amount", 15000)
                .addValue("discount_percent", 0)
                .addValue("discount_amount", 3000);

        long welcomeCouponId = simpleCouponJdbcInsert.executeAndReturnKey(welcomeCoupon).longValue();
        long againCouponId = simpleCouponJdbcInsert.executeAndReturnKey(againCoupon).longValue();

        createMemberCoupon(welcomeCouponId, againCouponId);
    }

    private void createMemberCoupon(long welcomeCouponId, long againCouponId) {
        SimpleJdbcInsert simpleMemberCouponInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingGeneratedKeyColumns("id");

        MapSqlParameterSource percent10Coupon = new MapSqlParameterSource()
                .addValue("member_id", 1L)
                .addValue("coupon_id", welcomeCouponId)
                .addValue("status", 1);

        MapSqlParameterSource dicount15000Coupon = new MapSqlParameterSource()
                .addValue("member_id", 1L)
                .addValue("coupon_id", againCouponId)
                .addValue("status", 1);

        usablePercentCouponId = simpleMemberCouponInsert.executeAndReturnKey(percent10Coupon).longValue();
        usableAmountCouponId = simpleMemberCouponInsert.executeAndReturnKey(dicount15000Coupon).longValue();
    }

    private void createProduct() {
        bbqProductId = productRepository.createProduct(통구이);
        padProductId = productRepository.createProduct(배변패드);
        tailProductId = productRepository.createProduct(ProductFixture.꼬리요리);
    }

    private void createCartItem() {
        bbq = new Product(bbqProductId, 통구이.getName(), 통구이.getPrice(), 통구이.getImageUrl());
        pad = new Product(padProductId, 배변패드.getName(), 배변패드.getPrice(), 배변패드.getImageUrl());

        Member leo = new Member(leoId, 레오.getName(), 레오.getEmail(), 레오.getPassword());

        bbqCartItemId = cartItemRepository.createCartItem(new CartItem(3, bbq, leo));
        padCartItemId = cartItemRepository.createCartItem(new CartItem(6, pad, leo));
    }
}

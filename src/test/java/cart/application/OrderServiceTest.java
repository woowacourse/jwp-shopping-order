package cart.application;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.product.CartItem;
import cart.domain.product.Product;
import cart.dto.CartItemDto;
import cart.dto.ProductDto;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.exception.OrderException;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.MemberRepository;
import cart.repository.ProductRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cart.domain.fixture.CouponFixture.AMOUNT_1000_COUPON;
import static cart.domain.fixture.CouponFixture.RATE_10_COUPON;
import static cart.domain.fixture.ProductFixture.PRODUCT_A;
import static cart.domain.fixture.ProductFixture.PRODUCT_B;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@AutoConfigureTestDatabase
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Member member;
    private Product productA;
    private Product productB;
    private Coupon amountCoupon;
    private Coupon rateCoupon;

    @BeforeEach
    void setUp() {
        member = memberRepository.getMemberById(1L);
        productA = productRepository.getProductById(productRepository.createProduct(PRODUCT_A));
        productB = productRepository.getProductById(productRepository.createProduct(PRODUCT_B));

        rateCoupon = couponRepository.findById(couponRepository.insert(RATE_10_COUPON));
        amountCoupon = couponRepository.findById(couponRepository.insert(AMOUNT_1000_COUPON));
    }


    @Test
    @DisplayName("상품들을 주문한다.")
    void order_test() {
        // given
        Long memberCouponId = memberCouponRepository.insert(member, amountCoupon);

        Long aId = addProductToCart(productA, 1);
        Long bId = addProductToCart(productB, 3);

        // when
        Long orderId = orderService.makeOrder(member, new OrderRequest(
                List.of(
                        new CartItemDto(aId, 1, ProductDto.of(productA)),
                        new CartItemDto(bId,  3, ProductDto.of(productB))
                ),
                3000,
                List.of(memberCouponId)
        )).getOrderId();

        // then
        OrderResponse orderInfo = orderService.findOrderInfo(member, orderId);
        int expectedPrice = PRODUCT_A.getPrice() + PRODUCT_B.getPrice()*3;
        final List<String> cartItemNames = orderInfo.getCartItems().stream()
                .map(cartItemDto -> cartItemDto.getProduct().getName())
                .collect(Collectors.toList());
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(orderInfo.getId()).isEqualTo(orderId);
            softly.assertThat(orderInfo.getOriginalPrice()).isEqualTo(expectedPrice);
            softly.assertThat(orderInfo.getActualPrice()).isEqualTo(expectedPrice - 1000);
            softly.assertThat(orderInfo.getDeliveryFee()).isEqualTo(3000);
            softly.assertThat(cartItemNames).containsExactlyInAnyOrder(PRODUCT_A.getName(), PRODUCT_B.getName());
        });
    }

    private Long addProductToCart(Product product, int quantity) {
        Long id = cartItemRepository.save(new CartItem(member, product));
        CartItem item = cartItemRepository.findById(id);
        item.changeQuantity(quantity);
        cartItemRepository.updateQuantity(item);
        return id;
    }

    @Test
    @DisplayName("상품의 가격이 다르면 예외를 발생시킨다")
    void product_price_not_match_order_test() {
        // given
        Long memberCouponId = memberCouponRepository.insert(member, amountCoupon);
        Long aId = addProductToCart(productA, 1);
        Long bId = addProductToCart(productB, 3);

        Product changedB = new Product(productB.getId(), productB.getName(), productB.getPrice() + 1000, productB.getImageUrl());

        // when
        OrderRequest wrongRequest = new OrderRequest(
                List.of(
                        new CartItemDto(aId, 1, ProductDto.of(productA)),
                        new CartItemDto(bId, 3, ProductDto.of(changedB))
                ),
                3000,
                List.of(memberCouponId)
        );

        // then
        assertThatThrownBy(() -> orderService.makeOrder(member, wrongRequest))
                .isInstanceOf(OrderException.ProductPriceUpdated.class)
                .hasMessageContaining(productB.getName());
    }

    @Test
    @DisplayName("요청과 실제 카트의 상품 수량이 맞지 않으면 예외를 발생시킨다")
    void item_quantity_not_match_order_test() {
        // given
        Long memberCouponId = memberCouponRepository.insert(member, amountCoupon);

        Long aId = addProductToCart(productA, 1);
        Long bId = addProductToCart(productB, 3);

        // when
        OrderRequest wrongRequest = new OrderRequest(
                List.of(
                        new CartItemDto(aId, 2, ProductDto.of(productA)),
                        new CartItemDto(bId, 3, ProductDto.of(productB))
                ),
                3000,
                List.of(memberCouponId)
        );

        // then
        assertThatThrownBy(() -> orderService.makeOrder(member, wrongRequest))
                .isInstanceOf(OrderException.QuantityNotMatched.class)
                .hasMessageContaining(productA.getName());
    }
}

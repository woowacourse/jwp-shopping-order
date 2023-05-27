package cart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberCouponDao;
import cart.domain.CartItem;
import cart.domain.Item;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.coupon.AmountDiscountPolicy;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.NoneDiscountCondition;
import cart.dto.ItemDto;
import cart.dto.ItemIdDto;
import cart.dto.OrderResponse;
import cart.dto.OrderSaveRequest;
import cart.entity.MemberCouponEntity;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberCouponDao memberCouponDao;

    @Test
    void 상품을_주문한다() {
        // given
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 18900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final Item cartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final Item cartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final Coupon coupon = couponRepository.save(new Coupon(
                "2000원 할인 쿠폰",
                new AmountDiscountPolicy(2000L),
                new NoneDiscountCondition()
        ));
        memberCouponDao.insert(new MemberCouponEntity(coupon.getId(), member.getId(), false));
        final OrderSaveRequest orderSaveRequest = new OrderSaveRequest(
                List.of(new ItemIdDto(cartItem1.getId()), new ItemIdDto(cartItem2.getId())),
                coupon.getId()
        );

        // when
        final Long result = orderService.save(orderSaveRequest, member.getId());

        // then
        assertAll(
                () -> assertThat(orderRepository.findAllByMemberId(member.getId())).hasSize(1),
                () -> assertThat(result).isPositive()
        );
    }

    @Test
    void 주문_전체_조회() {
        // given
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 18900L));
        final Product product3 = productRepository.save(new Product("pizza3", "pizza3.jpg", 18900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final Item cartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final Item cartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final Item cartItem3 = cartItemRepository.save(new CartItem(member, product3));
        final OrderSaveRequest orderSaveRequest1 = new OrderSaveRequest(
                List.of(new ItemIdDto(cartItem1.getId()), new ItemIdDto(cartItem2.getId())), null
        );
        final OrderSaveRequest orderSaveRequest2 = new OrderSaveRequest(
                List.of(new ItemIdDto(cartItem3.getId())), null
        );
        final Long orderId1 = orderService.save(orderSaveRequest1, member.getId());
        final Long orderId2 = orderService.save(orderSaveRequest2, member.getId());

        // when
        final List<OrderResponse> result = orderService.findAll(member.getId());

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(List.of(
                new OrderResponse(orderId1, 27800L, 0L, 3000L, List.of(
                        new ItemDto(null, "pizza1", 8900L, "pizza1.jpg", 1),
                        new ItemDto(null, "pizza2", 18900L, "pizza2.jpg", 1)
                )),
                new OrderResponse(orderId2, 18900L, 0L, 3000L, List.of(
                        new ItemDto(null, "pizza3", 18900L, "pizza3.jpg", 1)
                ))
        ));
    }

    @Test
    void 주문_단일_조회() {
        // given
        final Product product = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final Item cartItem = cartItemRepository.save(new CartItem(member, product));
        final OrderSaveRequest orderSaveRequest = new OrderSaveRequest(List.of(new ItemIdDto(cartItem.getId())), null);
        final Long orderId = orderService.save(orderSaveRequest, member.getId());

        // when
        final OrderResponse result = orderService.findById(orderId, member.getId());

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(
                new OrderResponse(orderId, 8900L, 0L, 3000L, List.of(
                        new ItemDto(null, "pizza1", 8900L, "pizza1.jpg", 1)
                ))
        );
    }
}

package cart.controller;

import static cart.fixture.CouponFixture._3만원_이상_2천원_할인_쿠폰;
import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.ProductFixture.상품_18900원;
import static cart.fixture.ProductFixture.상품_28900원;
import static cart.fixture.ProductFixture.상품_8900원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.cart.CartItem;
import cart.domain.cart.MemberCoupon;
import cart.domain.cart.Order;
import cart.domain.cart.Product;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.dto.cart.OrderSaveRequest;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 상품을_주문한다() throws Exception {
        // given
        final Product product1 = productRepository.save(상품_8900원);
        final Product product2 = productRepository.save(상품_28900원);
        final Member member = memberRepository.save(사용자1);
        final CartItem cartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final CartItem cartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final Coupon coupon = couponRepository.save(_3만원_이상_2천원_할인_쿠폰);
        final MemberCoupon memberCoupon = memberCouponRepository.save(new MemberCoupon(member.getId(), coupon));
        final OrderSaveRequest orderSaveRequest = new OrderSaveRequest(
                List.of(cartItem1.getId(), cartItem2.getId()),
                memberCoupon.getId()
        );
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza1@pizza.com:password".getBytes()));
        final String request = objectMapper.writeValueAsString(orderSaveRequest);

        // when
        mockMvc.perform(post("/orders")
                        .header("Authorization", header)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // then
        assertThat(orderRepository.findAllByMemberId(member.getId())).hasSize(1);
    }

    @Test
    void 주문을_전체_조회한다() throws Exception {
        // given
        final Product product1 = productRepository.save(상품_8900원);
        final Product product2 = productRepository.save(상품_18900원);
        final Product product3 = productRepository.save(상품_28900원);
        final Member member = memberRepository.save(사용자1);
        final CartItem cartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final CartItem cartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final CartItem cartItem3 = cartItemRepository.save(new CartItem(member, product3));
        final Order order1 = orderRepository.save(
                Order.of(MemberCoupon.empty(member.getId()), member.getId(), List.of(cartItem1, cartItem2)));
        final Order order2 = orderRepository.save(
                Order.of(MemberCoupon.empty(member.getId()), member.getId(), List.of(cartItem3)));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza1@pizza.com:password".getBytes()));

        // expect
        mockMvc.perform(get("/orders")
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(order1.getId().intValue())))
                .andExpect(jsonPath("$[0].totalItemsPrice", is(27800)))
                .andExpect(jsonPath("$[0].discountPrice", is(0)))
                .andExpect(jsonPath("$[0].deliveryFee", is(3000)))
                .andExpect(jsonPath("$[1].id", is(order2.getId().intValue())))
                .andExpect(jsonPath("$[1].totalItemsPrice", is(28900)))
                .andExpect(jsonPath("$[1].discountPrice", is(0)))
                .andExpect(jsonPath("$[1].deliveryFee", is(3000)))
                .andDo(print());
    }

    @Test
    void 주문을_단일_조회한다() throws Exception {
        // given
        final Product product = productRepository.save(상품_8900원);
        final Member member = memberRepository.save(사용자1);
        final CartItem cartItem = cartItemRepository.save(new CartItem(member, product));
        final Order order = orderRepository.save(
                Order.of(MemberCoupon.empty(member.getId()), member.getId(), List.of(cartItem)));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza1@pizza.com:password".getBytes()));

        // expect
        mockMvc.perform(get("/orders/" + order.getId())
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(order.getId().intValue())))
                .andExpect(jsonPath("totalItemsPrice", is(8900)))
                .andExpect(jsonPath("discountPrice", is(0)))
                .andExpect(jsonPath("deliveryFee", is(3000)))
                .andDo(print());
    }
}

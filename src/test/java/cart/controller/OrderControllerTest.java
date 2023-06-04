package cart.controller;

import cart.domain.CartItem;
import cart.domain.Money;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.discountPolicy.PricePolicy;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.order.Order;
import cart.dto.OrderRequest;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    private String header;
    private Member member;
    private Product product1;
    private Product product2;
    private CartItem cartItem1;
    private CartItem cartItem2;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));
        member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", new Money(8900L)));
        product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", new Money(9900L)));
        cartItem1 = cartItemRepository.save(new CartItem(member.getId(), product1));
        cartItem2 = cartItemRepository.save(new CartItem(member.getId(), product2));
        orderRequest = new OrderRequest(List.of(cartItem1.getId(), cartItem2.getId()));
    }

    @Test
    void 사용자의_주문을_저장한다() throws Exception {
        //given
        final String request = objectMapper.writeValueAsString(orderRequest);

        // expect
        final MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // then
        final String location = mvcResult.getResponse().getHeader("Location");
        final Long id = Long.parseLong(location.substring("/orders/".length()));
        final Order result = orderRepository.findById(id).get();
        assertThat(result.getOrderItems().size()).isEqualTo(2);
    }

    @Test
    void 사용자의_모든_주문을_조회한다() throws Exception {
        // given
        final Coupon coupon = couponRepository.save(new Coupon("30000원 이상 3000원 할인 쿠폰", new PricePolicy(), BigDecimal.valueOf(3000L), new Money(30000L)));
        final MemberCoupon memberCoupon = memberCouponRepository.save(new MemberCoupon(member, coupon, false));
        final Order order = Order.createFromCartItems(List.of(cartItem1, cartItem2), new Money(3000L), memberCoupon, member.getId());
        orderRepository.save(order);

        // expect
        mockMvc.perform(get("/orders")
                        .header("Authorization", header)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andDo(print());
    }

    @Test
    void 아이디에_해당하는_사용자의_주문을_조회한다() throws Exception {
        // given
        final Coupon coupon = couponRepository.save(new Coupon("10000원 이상 1000원 할인 쿠폰", new PricePolicy(), BigDecimal.valueOf(1000L), new Money(10000L)));
        final MemberCoupon memberCoupon = memberCouponRepository.save(new MemberCoupon(member, coupon, false));
        final Order order = Order.createFromCartItems(List.of(cartItem1, cartItem2), new Money(3000L), memberCoupon, member.getId());
        final Order saved = orderRepository.save(order);

        // expect
        mockMvc.perform(get("/orders/" + saved.getId())
                        .header("Authorization", header)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItemsPrice", is(18800)))
                .andExpect(jsonPath("$.discountPrice", is(1000)))
                .andExpect(jsonPath("$.deliveryFee", is(3000)))
                .andExpect(jsonPath("$.orderItems", hasSize(2)))
                .andDo(print());
    }
}

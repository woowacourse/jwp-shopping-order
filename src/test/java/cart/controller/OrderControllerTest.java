package cart.controller;

import static cart.domain.coupon.Coupon.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.dao.MemberCouponDao;
import cart.domain.cart.CartItem;
import cart.domain.cart.Item;
import cart.domain.cart.Order;
import cart.domain.cart.Product;
import cart.domain.coupon.AmountDiscountPolicy;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.NoneDiscountCondition;
import cart.domain.member.Member;
import cart.dto.ItemIdDto;
import cart.dto.OrderSaveRequest;
import cart.entity.MemberCouponEntity;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
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
    private MemberCouponDao memberCouponDao;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 상품을_주문한다() throws Exception {
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
        memberCouponDao.insert(new MemberCouponEntity(member.getId(), coupon.getId(), false));
        final OrderSaveRequest orderSaveRequest = new OrderSaveRequest(
                List.of(new ItemIdDto(cartItem1.getId()), new ItemIdDto(cartItem2.getId())),
                coupon.getId()
        );
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));
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
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 18900L));
        final Product product3 = productRepository.save(new Product("pizza3", "pizza3.jpg", 18900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final Item cartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final Item cartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final Item cartItem3 = cartItemRepository.save(new CartItem(member, product3));
        final Order order1 = orderRepository.save(new Order(EMPTY, member.getId(), List.of(cartItem1, cartItem2)));
        final Order order2 = orderRepository.save(new Order(EMPTY, member.getId(), List.of(cartItem3)));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

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
                .andExpect(jsonPath("$[1].totalItemsPrice", is(18900)))
                .andExpect(jsonPath("$[1].discountPrice", is(0)))
                .andExpect(jsonPath("$[1].deliveryFee", is(3000)))
                .andDo(print());
    }

    @Test
    void 주문을_단일_조회한다() throws Exception {
        // given
        final Product product = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));
        final Item cartItem = cartItemRepository.save(new CartItem(member, product));
        final Order order = orderRepository.save(new Order(EMPTY, member.getId(), List.of(cartItem)));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

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

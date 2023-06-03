package cart.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.policy.DiscountPolicyType;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
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
    private OrderRepository orderRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsert;

    @BeforeEach
    void init() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingColumns("coupon_id", "member_id", "used")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 주문을_완료한다() throws Exception {
        // given
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 18900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));

        final CartItem savedCartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final CartItem savedCartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

        final OrderSaveRequest orderSaveRequest = new OrderSaveRequest(
                List.of(savedCartItem1.getId(), savedCartItem2.getId()), null);
        final String request = objectMapper.writeValueAsString(orderSaveRequest);

        // when
        MvcResult authorization = mockMvc.perform(post("/orders")
                        .header("Authorization", header)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        String uri = authorization.getResponse().getHeader("Location");
        String[] split = uri.split("/");
        String orderId = split[split.length - 1];

        // then
        List<CartItem> findCartItems = cartItemRepository.findAllByMemberId(member.getId());
        assertThat(findCartItems).isEmpty();

        Order findOrder = orderRepository.findById(Long.valueOf(orderId));
        assertThat(findOrder.getOrderItems()).hasSize(2);
    }

    @Test
    void 쿠폰을_사용하여_주문을_완료한다() throws Exception {
        // given
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 9000L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 10000L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));

        final Coupon savedCoupon = couponRepository.saveCoupon(
                new Coupon("1만원 이상 1000원할인", DiscountPolicyType.findDiscountPolicy("price", 1000L), 10000L));

        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(savedCoupon.getId(), member.getId(),
                false);
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(memberCouponEntity);
        final long savedMemberCouponId = jdbcInsert.executeAndReturnKey(parameterSource).longValue();

        final CartItem savedCartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final CartItem savedCartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

        final OrderSaveRequest orderSaveRequest = new OrderSaveRequest(
                List.of(savedCartItem1.getId(), savedCartItem2.getId()), savedMemberCouponId);
        final String request = objectMapper.writeValueAsString(orderSaveRequest);

        // when
        MvcResult authorization = mockMvc.perform(post("/orders")
                        .header("Authorization", header)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        String uri = authorization.getResponse().getHeader("Location");
        String[] split = uri.split("/");
        String orderId = split[split.length - 1];

        // then
        List<CartItem> findCartItems = cartItemRepository.findAllByMemberId(member.getId());
        assertThat(findCartItems).isEmpty();

        Order findOrder = orderRepository.findById(Long.valueOf(orderId));
        assertThat(findOrder.getOrderItems()).hasSize(2);
        assertThat(findOrder.getTotalPrice()).isEqualTo(19000);
        assertThat(findOrder.getDeliveryFee()).isEqualTo(3000);
        assertThat(findOrder.getDiscountValue()).isEqualTo(1000);
        assertThat(findOrder.getCalculateDiscountPrice().longValue()).isEqualTo(18000);
    }

    @Test
    void 주문을_상세_조회한다() throws Exception {
        // given
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 18900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));

        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

        final Order order = new Order(MemberCoupon.makeNonMemberCoupon(), member, List.of(
                new OrderItem(product1.getName(), product1.getPrice(), product1.getImageUrl(), 2),
                new OrderItem(product2.getName(), product2.getPrice(), product2.getImageUrl(), 1))
        );
        Long savedOrdersId = orderRepository.save(order).getId();
        // when
        mockMvc.perform(get("/orders/" + savedOrdersId)
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedOrdersId.intValue())))
                .andExpect(jsonPath("$.totalItemsPrice", is(order.getCalculateDiscountPrice().intValue())))
                .andExpect(jsonPath("$.discountPrice", is(0)))
                .andExpect(jsonPath("$.deliveryFee", is(order.getDeliveryFee().intValue())))
                .andExpect(jsonPath("$.orderItems", hasSize(2)))
                .andExpect(jsonPath("$.orderItems[0].name", is(product1.getName())))
                .andExpect(jsonPath("$.orderItems[0].imageUrl", is(product1.getImageUrl())))
                .andExpect(jsonPath("$.orderItems[0].price", is((int) product1.getPrice())))
                .andExpect(jsonPath("$.orderItems[1].name", is(product2.getName())))
                .andExpect(jsonPath("$.orderItems[1].imageUrl", is(product2.getImageUrl())))
                .andExpect(jsonPath("$.orderItems[1].price", is((int) product2.getPrice())))
                .andDo(print())
                .andReturn();
    }

    @Test
    void 주문을_전체_조회한다() throws Exception {
        // given
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 18900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));

        cartItemRepository.save(new CartItem(member, product1));
        cartItemRepository.save(new CartItem(member, product2));
        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

        final Order order1 = new Order(MemberCoupon.makeNonMemberCoupon(), member, List.of(
                new OrderItem(product1.getName(), product1.getPrice(), product1.getImageUrl(), 2))
        );
        final Order order2 = new Order(MemberCoupon.makeNonMemberCoupon(), member, List.of(
                new OrderItem(product2.getName(), product2.getPrice(), product2.getImageUrl(), 1))
        );
        Long savedOrdersId1 = orderRepository.save(order1).getId();
        Long savedOrdersId2 = orderRepository.save(order2).getId();
        // when
        mockMvc.perform(get("/orders")
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(savedOrdersId1.intValue())))
                .andExpect(jsonPath("$[0].totalItemsPrice", is(order1.getCalculateDiscountPrice().intValue())))
                .andExpect(jsonPath("$[0].discountPrice", is(0)))
                .andExpect(jsonPath("$[0].deliveryFee", is(order1.getDeliveryFee().intValue())))
                .andExpect(jsonPath("$[0].orderItems", hasSize(1)))
                .andExpect(jsonPath("$[0].orderItems[0].name", is(product1.getName())))
                .andExpect(jsonPath("$[0].orderItems[0].imageUrl", is(product1.getImageUrl())))
                .andExpect(jsonPath("$[0].orderItems[0].price", is((int) product1.getPrice())))

                .andExpect(jsonPath("$[1].id", is(savedOrdersId2.intValue())))
                .andExpect(jsonPath("$[1].totalItemsPrice", is(order2.getCalculateDiscountPrice().intValue())))
                .andExpect(jsonPath("$[1].discountPrice", is(0)))
                .andExpect(jsonPath("$[1].deliveryFee", is(order2.getDeliveryFee().intValue())))
                .andExpect(jsonPath("$[1].orderItems", hasSize(1)))
                .andExpect(jsonPath("$[1].orderItems[0].name", is(product2.getName())))
                .andExpect(jsonPath("$[1].orderItems[0].imageUrl", is(product2.getImageUrl())))
                .andExpect(jsonPath("$[1].orderItems[0].price", is((int) product2.getPrice())))
                .andDo(print())
                .andReturn();
    }
}

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
import cart.domain.MemberCoupon;
import cart.domain.OrderItem;
import cart.domain.Orders;
import cart.domain.Product;
import cart.dto.OrderSaveRequest;
import cart.repository.CartItemRepository;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class OrdersControllerTest {

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
        assertThat(findCartItems).hasSize(2);

        Orders findOrders = orderRepository.findByOrderIdAndMemberId(Long.valueOf(orderId), member.getId());
        assertThat(findOrders.getOrderItems()).hasSize(2);
    }

    @Test
    void 주문을_상세_조회한다() throws Exception {
        // given
        final Product product1 = productRepository.save(new Product("pizza1", "pizza1.jpg", 8900L));
        final Product product2 = productRepository.save(new Product("pizza2", "pizza2.jpg", 18900L));
        final Member member = memberRepository.save(new Member("pizza@pizza.com", "password"));

        final String header = "Basic " + new String(Base64.getEncoder().encode("pizza@pizza.com:password".getBytes()));

        final Orders orders = new Orders(MemberCoupon.makeNonMemberCoupon(), member, List.of(
                new OrderItem(product1.getName(), product1.getPrice(), product1.getImageUrl(), 2),
                new OrderItem(product2.getName(), product2.getPrice(), product2.getImageUrl(), 1))
        );
        Long savedOrdersId = orderRepository.save(orders).getId();
        // when
        mockMvc.perform(get("/orders/" + savedOrdersId)
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedOrdersId.intValue())))
                .andExpect(jsonPath("$.totalItemsPrice", is(orders.getCalculateDiscountPrice().intValue())))
                .andExpect(jsonPath("$.discountPrice", is(0)))
                .andExpect(jsonPath("$.deliveryFee", is(orders.getDeliveryFee().intValue())))
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

        final List<CartItem> result = cartItemRepository.findAllByMemberId(member.getId());
        final Orders orders1 = new Orders(MemberCoupon.makeNonMemberCoupon(), member, List.of(
                new OrderItem(product1.getName(), product1.getPrice(), product1.getImageUrl(), 2))
        );
        final Orders orders2 = new Orders(MemberCoupon.makeNonMemberCoupon(), member, List.of(
                new OrderItem(product2.getName(), product2.getPrice(), product2.getImageUrl(), 1))
        );
        Long savedOrdersId1 = orderRepository.save(orders1).getId();
        Long savedOrdersId2 = orderRepository.save(orders2).getId();
        // when
        mockMvc.perform(get("/orders")
                        .header("Authorization", header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(savedOrdersId1.intValue())))
                .andExpect(jsonPath("$[0].totalItemsPrice", is(orders1.getCalculateDiscountPrice().intValue())))
                .andExpect(jsonPath("$[0].discountPrice", is(0)))
                .andExpect(jsonPath("$[0].deliveryFee", is(orders1.getDeliveryFee().intValue())))
                .andExpect(jsonPath("$[0].orderItems", hasSize(1)))
                .andExpect(jsonPath("$[0].orderItems[0].name", is(product1.getName())))
                .andExpect(jsonPath("$[0].orderItems[0].imageUrl", is(product1.getImageUrl())))
                .andExpect(jsonPath("$[0].orderItems[0].price", is((int) product1.getPrice())))

                .andExpect(jsonPath("$[1].id", is(savedOrdersId2.intValue())))
                .andExpect(jsonPath("$[1].totalItemsPrice", is(orders2.getCalculateDiscountPrice().intValue())))
                .andExpect(jsonPath("$[1].discountPrice", is(0)))
                .andExpect(jsonPath("$[1].deliveryFee", is(orders2.getDeliveryFee().intValue())))
                .andExpect(jsonPath("$[1].orderItems", hasSize(1)))
                .andExpect(jsonPath("$[1].orderItems[0].name", is(product2.getName())))
                .andExpect(jsonPath("$[1].orderItems[0].imageUrl", is(product2.getImageUrl())))
                .andExpect(jsonPath("$[1].orderItems[0].price", is((int) product2.getPrice())))
                .andDo(print())
                .andReturn();
    }
}

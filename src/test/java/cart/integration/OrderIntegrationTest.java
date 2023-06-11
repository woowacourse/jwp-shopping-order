package cart.integration;

import static java.util.Base64.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import cart.application.dto.PostOrderRequest;
import cart.application.dto.SingleKindProductRequest;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderStatus;
import cart.domain.QuantityAndProduct;
import cart.repository.OrderRepository;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderRepository orderRepository;

    private Long productId1;
    private Long productId2;
    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberDao.getMemberById(1L);
        productId1 = productDao.getProductById(1L).getId();
        productId2 = productDao.getProductById(2L).getId();
    }

    @Test
    void 주문을_추가한다() throws Exception {
        // given
        SingleKindProductRequest productRequest1 = new SingleKindProductRequest(productId1, 1);
        SingleKindProductRequest productRequest2 = new SingleKindProductRequest(productId2, 2);
        PostOrderRequest orderRequest = new PostOrderRequest(0, List.of(productRequest1, productRequest2));
        String authHeader = getEncodedAuthHeader(member);
        String jsonRequest = new ObjectMapper().writeValueAsString(orderRequest);

        // when & then
        mockMvc.perform(post("/orders")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));

        Order order = orderRepository.findAllByMemberId(member.getId()).get(0);
        List<QuantityAndProduct> quantityAndProducts = order.getQuantityAndProducts();
        assertThat(quantityAndProducts).hasSize(2);
        assertThat(quantityAndProducts.get(0).getQuantity()).isEqualTo(1);
        assertThat(quantityAndProducts.get(1).getQuantity()).isEqualTo(2);
    }

    @Test
    void 페이지가_포함된_주문을_조회한다() throws Exception {
        // given
        SingleKindProductRequest productRequest1 = new SingleKindProductRequest(productId1, 1);
        SingleKindProductRequest productRequest2 = new SingleKindProductRequest(productId2, 2);
        PostOrderRequest orderRequest = new PostOrderRequest(0, List.of(productRequest1, productRequest2));
        String authHeader = getEncodedAuthHeader(member);
        String jsonRequest = new ObjectMapper().writeValueAsString(orderRequest);

        postOrder(authHeader, jsonRequest);

        // when & then
        mockMvc.perform(get("/orders?page=1")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.totalPages").value(1))
            .andExpect(jsonPath("$.currentPage").value(1))
            .andExpect(jsonPath("$.contents.length()").value(1))
            .andExpect(jsonPath("$.contents[0].totalProductCount").value(3));
    }

    @Test
    void 페이지_당_정해진_개수의_주문_이력만_조회된다() throws Exception {
        // given
        SingleKindProductRequest productRequest1 = new SingleKindProductRequest(productId1, 1);
        SingleKindProductRequest productRequest2 = new SingleKindProductRequest(productId2, 2);
        PostOrderRequest orderRequest = new PostOrderRequest(0, List.of(productRequest1, productRequest2));
        String authHeader = getEncodedAuthHeader(member);
        String jsonRequest = new ObjectMapper().writeValueAsString(orderRequest);

        int orderCount = 11;
        for (int i = 0; i < orderCount; i++) {
            postOrder(authHeader, jsonRequest);
        }
        int pageSize = 10;

        // when & then
        List<Order> orders = orderRepository.findAllByMemberId(member.getId());
        mockMvc.perform(get("/orders?page=1")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.totalPages").value(2))
            .andExpect(jsonPath("$.currentPage").value(1))
            .andExpect(jsonPath("$.contents.length()").value(pageSize));
        mockMvc.perform(get("/orders?page=2")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.totalPages").value(2))
            .andExpect(jsonPath("$.currentPage").value(2))
            .andExpect(jsonPath("$.contents.length()").value(orderCount - pageSize));
    }

    @Test
    void 특정_주문을_상세_조회한다() throws Exception {
        // given
        SingleKindProductRequest productRequest1 = new SingleKindProductRequest(productId1, 1);
        SingleKindProductRequest productRequest2 = new SingleKindProductRequest(productId2, 2);
        PostOrderRequest orderRequest = new PostOrderRequest(0, List.of(productRequest1, productRequest2));
        String authHeader = getEncodedAuthHeader(member);
        String jsonRequest = new ObjectMapper().writeValueAsString(orderRequest);

        postOrder(authHeader, jsonRequest);
        Long orderId = orderRepository.findAllByMemberId(member.getId()).get(0).getId();

        // when & then
        mockMvc.perform(get("/orders/" + orderId)
                .header("Authorization", authHeader))
            .andExpect(jsonPath("$.products.length()").value(2))
            .andExpect(jsonPath("$.products[0].quantity").value(1))
            .andExpect(jsonPath("$.products[1].quantity").value(2));
    }

    @Test
    void 특정_주문을_취소한다() throws Exception {
        // given
        SingleKindProductRequest productRequest1 = new SingleKindProductRequest(productId1, 1);
        SingleKindProductRequest productRequest2 = new SingleKindProductRequest(productId2, 2);
        PostOrderRequest orderRequest = new PostOrderRequest(0, List.of(productRequest1, productRequest2));
        String authHeader = getEncodedAuthHeader(member);
        String jsonRequest = new ObjectMapper().writeValueAsString(orderRequest);

        postOrder(authHeader, jsonRequest);
        Long orderId = orderRepository.findAllByMemberId(member.getId()).get(0).getId();

        // when
        mockMvc.perform(delete("/orders/" + orderId)
                .header("Authorization", authHeader))
            .andExpect(status().isOk());

        // then
        Order persistOrder = orderRepository.findById(orderId);
        assertThat(persistOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    private void postOrder(String authHeader, String jsonRequest) throws Exception {
        mockMvc.perform(post("/orders")
            .header("Authorization", authHeader)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonRequest));
    }

    private String getEncodedAuthHeader(Member member) {
        return "Basic " + new String(
            getEncoder().encode(String.format("%s:%s", member.getEmail(), member.getPassword()).getBytes()));
    }
}

package cart.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.OrderService;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderItemResponse;
import cart.dto.ProductResponse;
import cart.entity.MemberEntity;
import cart.ui.pageable.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private MemberDao memberDao;

    private String basicAuthHeader;

    private MemberEntity member;

    @BeforeEach
    void setUp() {
        basicAuthHeader = "Basic " + Base64Utils.encodeToString("id:password".getBytes());

        member = new MemberEntity(1L, "id", "password");
        given(memberDao.findByEmail("id"))
                .willReturn(Optional.of(member));
    }

    @Test
    @DisplayName("주문 정보를 등록한다.")
    void createOrder() throws Exception {
        final OrderCreateRequest request = new OrderCreateRequest(
                List.of(1L, 2L),
                "7777-7777-7777-7777",
                343,
                400
        );

        given(orderService.saveOrder(any(Member.class), any(OrderCreateRequest.class))).willReturn(1L);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/orders/1"));
    }

    @Test
    @DisplayName("사용자의 전체 주문 정보를 조회한다.")
    void findOrders() throws Exception {
        final ProductResponse pizza = new ProductResponse(1L, "pizza", 10000, "example.com/pizza");
        final OrderItemResponse orderItemResponse = new OrderItemResponse(1L, 4, pizza);
        final OrderDetailResponse expected = new OrderDetailResponse(
                1L,
                300,
                400,
                LocalDateTime.of(2023, 6, 5, 12, 0, 0),
                List.of(orderItemResponse)
        );

        given(orderService.findOrdersByMember(
                eq(new Member(
                        member.getId(),
                        member.getEmail(),
                        member.getPassword()
                )),
                any(Page.class)
        )).willReturn(List.of(expected));

        final MvcResult mvcResult = mockMvc.perform(get("/orders")
                        .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        List<OrderDetailResponse> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0)).usingRecursiveComparison().isEqualTo(expected)
        );
    }

    @Test
    void findOrderDetailById() throws Exception {
        final ProductResponse pizza = new ProductResponse(1L, "pizza", 10000, "example.com/pizza");
        final OrderItemResponse orderItemResponse = new OrderItemResponse(1L, 4, pizza);
        final OrderDetailResponse expected = new OrderDetailResponse(
                1L,
                300,
                400,
                LocalDateTime.of(2023, 6, 5, 12, 0, 0),
                List.of(orderItemResponse)
        );

        given(orderService.findOrderDetailById(any(Member.class), eq(1L))).willReturn(expected);

        final MvcResult mvcResult = mockMvc.perform(get("/orders/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, basicAuthHeader))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final OrderDetailResponse actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                OrderDetailResponse.class
        );
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}

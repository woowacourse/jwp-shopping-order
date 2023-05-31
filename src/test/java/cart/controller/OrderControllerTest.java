package cart.controller;

import static cart.fixture.TestFixture.AUTHORIZATION_HEADER_MEMBER_A;
import static cart.fixture.TestFixture.CART_ITEM_샐러드_MEMBER_A;
import static cart.fixture.TestFixture.COUPON_FIXED_2000;
import static cart.fixture.TestFixture.MEMBER_A;
import static cart.fixture.TestFixture.ORDER_ONE_MEMBER_A;
import static cart.fixture.TestFixture.ORDER_TWO_MEMBER_A;
import static cart.fixture.TestFixture.샐러드;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import cart.application.OrderService;
import cart.dao.MemberDao;
import cart.domain.Order;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.ProductRequest;
import cart.ui.OrderController;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest extends ControllerTestWithDocs {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberDao memberDao;
    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        when(memberDao.getMemberByEmail(anyString())).thenReturn(Optional.empty());
        when(memberDao.getMemberByEmail(eq(MEMBER_A.getEmail()))).thenReturn(Optional.of(MEMBER_A));
    }

    @Test
    void 주문요청() throws Exception {
        List<OrderRequest> orderRequest = List.of(
                new OrderRequest(
                        CART_ITEM_샐러드_MEMBER_A.getId(),
                        new ProductRequest.WithId(
                                샐러드.getId(),
                                샐러드.getName(),
                                샐러드.getPrice().getValue(),
                                샐러드.getImageUrl()
                        ),
                        10,
                        Collections.emptyList()
                )
        );
        String body = objectMapper.writeValueAsString(orderRequest);

        ResultActions result = mockMvc.perform(post("/orders")
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_MEMBER_A)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        );

        result
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(documentationOf(orderRequest));
    }

    @Test
    void 주문요청_쿠폰사용() throws Exception {
        List<OrderRequest> orderRequest = List.of(
                new OrderRequest(
                        CART_ITEM_샐러드_MEMBER_A.getId(),
                        new ProductRequest.WithId(
                                샐러드.getId(),
                                샐러드.getName(),
                                샐러드.getPrice().getValue(),
                                샐러드.getImageUrl()
                        ),
                        10,
                        List.of(COUPON_FIXED_2000.getId())
                )
        );
        String body = objectMapper.writeValueAsString(orderRequest);

        ResultActions result = mockMvc.perform(post("/orders")
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_MEMBER_A)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        );

        result
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(documentationOf(
                        orderRequest,
                        requestHeaders(headerWithName("Authorization").description("인증 정보"))
                ));
    }

    @Test
    void 사용자_주문_조회() throws Exception {
        List<Order> orders = List.of(ORDER_ONE_MEMBER_A, ORDER_TWO_MEMBER_A);
        when(orderService.getBy(eq(MEMBER_A))).thenReturn(orders);
        List<OrderResponse> orderResponses = OrderResponse.of(orders);

        ResultActions result = mockMvc.perform(get("/orders")
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_MEMBER_A)
        );

        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentationOf(
                        orderResponses,
                        requestHeaders(headerWithName("Authorization").description("인증 정보"))
                ));
    }

    @Test
    void id로_주문_조회() throws Exception {
        when(orderService.getBy(eq(ORDER_ONE_MEMBER_A.getId()))).thenReturn(ORDER_ONE_MEMBER_A);
        OrderResponse orderResponse = OrderResponse.of(ORDER_ONE_MEMBER_A);

        ResultActions result = mockMvc.perform(get("/orders/{id}", ORDER_ONE_MEMBER_A.getId())
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_MEMBER_A)
        );

        result
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(documentationOf(
                        orderResponse,
                        requestHeaders(headerWithName("Authorization").description("인증 정보"))
                ));
    }
}

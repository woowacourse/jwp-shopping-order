package cart.ui;

import static cart.TestSource.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cart.WebMvcConfig;
import cart.application.OrderService;
import cart.application.dto.GetOrdersRequest;
import cart.application.dto.PostOrderRequest;
import cart.application.dto.SingleKindProductRequest;
import cart.domain.Member;

@WebMvcTest(controllers = OrdersApiController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfig.class)})
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PaginatorApiControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String encodedAuth = Base64.getEncoder()
        .encodeToString("a@a.com:1234".getBytes(StandardCharsets.UTF_8));
    private final String authorizationHeader = "Basic " + encodedAuth;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    @Nested
    class 주문_목록_조회_테스트 {

        @Test
        void 페이지를_지정하지_않은_경우_BAD_REQUEST_가_반환된다() throws Exception {
            // given
            given(orderService.getOrdersWithPagination(any(Member.class), any(GetOrdersRequest.class))).willReturn(
                ordersResponse1);

            // when & then
            mockMvc.perform(get("/orders")
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isBadRequest());
        }

        @Test
        void 페이지를_지정하면_특정_페이지에_대한_주문_목록을_조회한다() throws Exception {
            // given
            given(orderService.getOrdersWithPagination(any(Member.class), any(GetOrdersRequest.class))).willReturn(
                ordersResponse1);
            long page = 1L;

            // when & then
            mockMvc.perform(get("/orders?page=" + page)
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents", Matchers.hasSize(3)));
        }

        @Test
        void orderId를_통해_주문을_상세_조회한다() throws Exception {
            // given
            given(orderService.getOrder(any(Member.class), anyLong())).willReturn(detailedOrderResponse1);

            // when & then
            mockMvc.perform(get("/orders/" + 1L)
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.orderAt").exists())
                .andExpect(jsonPath("$.orderStatus").exists())
                .andExpect(jsonPath("$.payAmount").exists())
                .andExpect(jsonPath("$.usedPoint").exists())
                .andExpect(jsonPath("$.savedPoint").exists())
                .andExpect(jsonPath("$.products").exists());
        }
    }

    @Nested
    class 주문_추가_테스트 {

        @Test
        void 주문_시_사용할_포인트는_0_이상이어야_한다() throws Exception {
            // given
            SingleKindProductRequest singleKindProductRequest = new SingleKindProductRequest(1L, 1);
            PostOrderRequest request = new PostOrderRequest(-1, List.of(singleKindProductRequest));
            String jsonRequest = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/orders")
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("0이상의 값을 입력해야 합니다"));
        }

        @Test
        void 주문에_포함된_상품_id는_1_이상이어야_한다() throws Exception {
            // given
            SingleKindProductRequest singleKindProductRequest = new SingleKindProductRequest(-1L, 1);
            PostOrderRequest request = new PostOrderRequest(0, List.of(singleKindProductRequest));
            String jsonRequest = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/orders")
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("유효하지 않은 상품 id 입니다"));
        }

        @Test
        void 주문할_상품의_종류는_1가지_이상이어야_한다() throws Exception {
            // given
            PostOrderRequest request = new PostOrderRequest(0, Collections.emptyList());
            String jsonRequest = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/orders")
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("하나 이상의 상품이 포함되어야 합니다"));
        }

        @Test
        void 주문할_상품의_수량은_1개_이상이어야_한다() throws Exception {
            // given
            SingleKindProductRequest singleKindProductRequest = new SingleKindProductRequest(1L, 0);
            PostOrderRequest request = new PostOrderRequest(0, List.of(singleKindProductRequest));
            String jsonRequest = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/orders")
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("구매할 상품의 개수는 1개 이상이어야 합니다"));
        }
    }
}

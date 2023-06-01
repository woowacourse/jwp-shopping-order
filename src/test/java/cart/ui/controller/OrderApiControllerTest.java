package cart.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.Product;
import cart.test.ControllerTest;
import cart.ui.controller.dto.request.OrderRequest;
import cart.ui.controller.dto.response.MemberResponse;
import cart.ui.controller.dto.response.OrderResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;

@WebMvcTest(OrderApiController.class)
class OrderApiControllerTest extends ControllerTest {

    @MockBean
    private OrderService orderService;

    @Nested
    @DisplayName("getOrders 메서드는 ")
    class GetOrders {

        @Test
        @DisplayName("인증 정보가 존재하지 않으면 401 상태를 반환한다.")
        void notAuthentication() throws Exception {
            mockMvc.perform(get("/orders"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("인증 정보가 존재하지 않습니다."));
        }

        @Test
        @DisplayName("모든 주문 정보를 조회한다.")
        void getOrders() throws Exception {
            Member member = new Member(1L, "a@a.com", "password1", 0);
            Product productA = new Product(1L, "치킨", 10000, "http://chicken.com");
            Product productB = new Product(2L, "피자", 10000, "http://pizza.com");
            OrderProduct orderProductA = new OrderProduct(1L, productA, 5);
            OrderProduct orderProductB = new OrderProduct(2L, productB, 10);
            LocalDateTime now = LocalDateTime.now();
            Order orderA = new Order(1L, member, List.of(orderProductA, orderProductB), 100, now);
            Order orderB = new Order(2L, member, List.of(orderProductA, orderProductB), 500, now);
            List<OrderResponse> response = List.of(OrderResponse.from(orderA), OrderResponse.from(orderB));
            given(memberService.getMemberByEmail(anyString())).willReturn(MemberResponse.from(member));
            given(memberService.getMemberByEmailAndPassword(anyString(), anyString())).willReturn(MemberResponse.from(member));
            given(orderService.getOrders(any(Member.class))).willReturn(response);

            MvcResult mvcResult = mockMvc.perform(get("/orders")
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = mvcResult.getResponse().getContentAsString();
            List<OrderResponse> result = objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<List<OrderResponse>>() {
                    }
            );
            assertThat(result).usingRecursiveComparison().isEqualTo(response);
        }
    }

    @Nested
    @DisplayName("getOrderDetail 메서드는 ")
    class GetOrderDetail {

        @Test
        @DisplayName("인증 정보가 존재하지 않으면 401 상태를 반환한다.")
        void notAuthentication() throws Exception {
            mockMvc.perform(get("/orders/{id}", 1))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("인증 정보가 존재하지 않습니다."));
        }

        @ParameterizedTest
        @ValueSource(strings = {"@", "1.2"})
        @DisplayName("ID로 변환할 수 없는 타입이라면 400 상태를 반환한다.")
        void invalidIDType(String id) throws Exception {
            Member member = new Member(1L, "a@a.com", "password1", 0);
            given(memberService.getMemberByEmail(anyString())).willReturn(MemberResponse.from(member));

            mockMvc.perform(get("/orders/{id}", id)
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes())))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("올바르지 않은 타입입니다. 필요한 타입은 [Long]입니다. 현재 입력값: " + id));
        }

        @Test
        @DisplayName("주문 정보를 조회한다.")
        void getOrderDetail() throws Exception {
            Member member = new Member(1L, "a@a.com", "password1", 0);
            Product productA = new Product(1L, "치킨", 10000, "http://chicken.com");
            Product productB = new Product(2L, "피자", 10000, "http://pizza.com");
            OrderProduct orderProductA = new OrderProduct(1L, productA, 5);
            OrderProduct orderProductB = new OrderProduct(2L, productB, 10);
            LocalDateTime now = LocalDateTime.now();
            Order order = new Order(1L, member, List.of(orderProductA, orderProductB), 0, now);
            OrderResponse response = OrderResponse.from(order);
            given(memberService.getMemberByEmail(anyString())).willReturn(MemberResponse.from(member));
            given(memberService.getMemberByEmailAndPassword(anyString(), anyString())).willReturn(MemberResponse.from(member));
            given(orderService.getOrderDetail(anyLong(), any(Member.class))).willReturn(response);

            MvcResult mvcResult = mockMvc.perform(get("/orders/{id}", 1)
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();

            String jsonResponse = mvcResult.getResponse().getContentAsString();
            OrderResponse result = objectMapper.readValue(jsonResponse, OrderResponse.class);
            assertThat(result).usingRecursiveComparison().isEqualTo(response);
        }
    }

    @Nested
    @DisplayName("processOrder 메서드는 ")
    class ProcessOrder {

        @Test
        @DisplayName("인증 정보가 존재하지 않으면 401 상태를 반환한다.")
        void notAuthentication() throws Exception {
            OrderRequest request = new OrderRequest(List.of(1L), 500);

            mockMvc.perform(post("/orders")
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message").value("인증 정보가 존재하지 않습니다."));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("장바구니 상품 ID 목록이 존재하지 않거나 비어있으면 400 상태를 반환한다.")
        void emptyIds(List<Long> ids) throws Exception {
            Member member = new Member(1L, "a@a.com", "password1", 0);
            OrderRequest request = new OrderRequest(ids, 500);
            given(memberService.getMemberByEmail(anyString())).willReturn(MemberResponse.from(member));

            mockMvc.perform(post("/orders")
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes()))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("장바구니 상품 ID가 최소한 1개는 필요합니다.")));
        }

        @Test
        @DisplayName("포인트가 존재하지 않으면 400 상태를 반환한다.")
        void emptyPoint() throws Exception {
            Member member = new Member(1L, "a@a.com", "password1", 0);
            OrderRequest request = new OrderRequest(List.of(1L), null);
            given(memberService.getMemberByEmail(anyString())).willReturn(MemberResponse.from(member));

            mockMvc.perform(post("/orders")
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes()))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("사용할 포인트는 존재해야 합니다.")));
        }

        @Test
        @DisplayName("포인트가 음수라면 400 상태를 반환한다.")
        void negativePoint() throws Exception {
            Member member = new Member(1L, "a@a.com", "password1", 0);
            OrderRequest request = new OrderRequest(List.of(1L), -1);
            given(memberService.getMemberByEmail(anyString())).willReturn(MemberResponse.from(member));

            mockMvc.perform(post("/orders")
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes()))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("사용할 포인트는 음수일 수 없습니다.")));
        }

        @Test
        @DisplayName("유효한 요청이라면 주문 이후 남은 장바구니 상품 목록을 응답한다.")
        void processOrder() throws Exception {
            OrderRequest request = new OrderRequest(List.of(1L), 100);
            Member member = new Member(1L, "a@a.com", "password1", 0);
            given(memberService.getMemberByEmail(anyString())).willReturn(MemberResponse.from(member));
            given(memberService.getMemberByEmailAndPassword(anyString(), anyString())).willReturn(MemberResponse.from(member));
            given(orderService.processOrder(any(Member.class), any(OrderRequest.class))).willReturn(1L);

            mockMvc.perform(post("/orders")
                            .header("Authorization", "Basic " + Base64Utils.encodeToUrlSafeString("a@a.com:password1".getBytes()))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/orders/" + 1));
        }
    }
}

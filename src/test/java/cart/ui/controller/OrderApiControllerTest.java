package cart.ui.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.application.OrderService;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.Product;
import cart.test.ControllerTest;
import cart.ui.controller.dto.response.MemberResponse;
import cart.ui.controller.dto.response.OrderResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
            Order orderA = new Order(1L, member, List.of(orderProductA, orderProductB), 100, 500, now);
            Order orderB = new Order(2L, member, List.of(orderProductA, orderProductB), 500, 1000, now);
            List<OrderResponse> response = List.of(OrderResponse.from(orderA), OrderResponse.from(orderB));
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
            Order order = new Order(1L, member, List.of(orderProductA, orderProductB), 0, 0, now);
            OrderResponse response = OrderResponse.from(order);
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
}

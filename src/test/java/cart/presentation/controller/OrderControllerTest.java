package cart.presentation.controller;

import cart.WebMvcConfig;
import cart.application.service.OrderService;
import cart.presentation.dto.request.OrderRequest;
import cart.presentation.dto.response.OrderDto;
import cart.presentation.dto.response.OrderResponse;
import cart.presentation.dto.response.SpecificOrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = OrderController.class, excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = {WebMvcConfig.class}))
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;

    @Test
    @DisplayName("모든 주문을 조회할 수 있다")
    void getAllOrders() throws Exception {
        // given
        OrderResponse orderResponse = new OrderResponse(1L,
                List.of(new OrderDto(1L, 1000L, "피자", "https://", 1L)));
        when(orderService.getAllOrders(any())).thenReturn(List.of(orderResponse));
        // when
        mockMvc.perform(get("/orders"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").exists())
                .andExpect(jsonPath("$[0].orderInfos[*].productId").exists())
                .andExpect(jsonPath("$[0].orderInfos[*].price").exists())
                .andExpect(jsonPath("$[0].orderInfos[*].name").exists())
                .andExpect(jsonPath("$[0].orderInfos[*].imageUrl").exists())
                .andExpect(jsonPath("$[0].orderInfos[*].quantity").exists());

    }

    @Test
    @DisplayName("특정 주문을 조회할 수 있다")
    void getSpecificOrder() throws Exception {
        // given
        SpecificOrderResponse orderResponse = new SpecificOrderResponse(1L,
                List.of(new OrderDto(1L, 1000L, "피자", "https://", 1L)),
                1000L, 100L, 0L);
        when(orderService.getSpecificOrder(any(), any())).thenReturn(orderResponse);
        // when
        mockMvc.perform(get("/orders/{id}", 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.originalPrice").exists())
                .andExpect(jsonPath("$.pointToAdd").exists())
                .andExpect(jsonPath("$.orderInfos[*].productId").exists())
                .andExpect(jsonPath("$.orderInfos[*].price").exists())
                .andExpect(jsonPath("$.orderInfos[*].name").exists())
                .andExpect(jsonPath("$.orderInfos[*].imageUrl").exists())
                .andExpect(jsonPath("$.orderInfos[*].quantity").exists());
    }

    @Test
    @DisplayName("주문을 발행할 수 있다")
    void issueOrder() throws Exception {
        // given
        OrderRequest request = new OrderRequest(List.of(1L), 1000L, 500L, 0L);
        String body = objectMapper.writeValueAsString(request);
        when(orderService.issue(any(), any())).thenReturn(1L);
        // when
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                // then
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/orders/1"));
    }
}

package cart.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import cart.application.OrderService;
import cart.dao.MemberDao;
import cart.dto.OrderRequest;
import cart.fixture.OrderResponseFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderApiController.class)
class OrderApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;
    @MockBean
    private MemberDao memberDao;

    @Test
    void createOrder() throws Exception {
        final String request = objectMapper.writeValueAsString(new OrderRequest(List.of(1L, 2L, 3L), 1_000, 15_000));
        given(orderService.createOrder(any(), any())).willReturn(OrderResponseFixture.ORDER_RESPONSE);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/orders/10"))
                .andExpect(jsonPath("$.orderId", is(10)))
                .andExpect(jsonPath("$.createdAt", is("2023-05-31T10:00:00")))
                .andExpect(jsonPath("$.orderItems", hasSize(2)))
                .andExpect(jsonPath("$.orderItems[0].productId", is(1)))
                .andExpect(jsonPath("$.orderItems[0].productName", is("치킨")))
                .andExpect(jsonPath("$.orderItems[0].quantity", is(1)))
                .andExpect(jsonPath("$.orderItems[0].price", is(10000)))
                .andExpect(jsonPath("$.orderItems[0].imageUrl", is("http://example.com/chicken.jpg")))
                .andExpect(jsonPath("$.orderItems[1].productId", is(2)))
                .andExpect(jsonPath("$.orderItems[1].productName", is("피자")))
                .andExpect(jsonPath("$.orderItems[1].quantity", is(1)))
                .andExpect(jsonPath("$.orderItems[1].price", is(15000)))
                .andExpect(jsonPath("$.orderItems[1].imageUrl", is("http://example.com/pizza.jpg")))
                .andExpect(jsonPath("$.totalPrice", is(25000)))
                .andExpect(jsonPath("$.usedPoint", is(100)))
                .andExpect(jsonPath("$.earnedPoint", is(50)));
    }

    @Test
    void getOrders() throws Exception {
        given(orderService.getOrders(any())).willReturn(List.of(
                OrderResponseFixture.ORDER1_RESPONSE, OrderResponseFixture.ORDER2_RESPONSE
        ));
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].orderId", is(1)))
                .andExpect(jsonPath("$[0].createdAt", is("2023-05-31T10:00:00")))
                .andExpect(jsonPath("$[0].orderItems", hasSize(2)))
                .andExpect(jsonPath("$[0].orderItems[0].productId", is(1)))
                .andExpect(jsonPath("$[0].orderItems[0].productName", is("치킨")))
                .andExpect(jsonPath("$[0].orderItems[0].quantity", is(1)))
                .andExpect(jsonPath("$[0].orderItems[0].price", is(10000)))
                .andExpect(jsonPath("$[0].orderItems[0].imageUrl", is("http://example.com/chicken.jpg")))
                .andExpect(jsonPath("$[0].orderItems[1].productId", is(2)))
                .andExpect(jsonPath("$[0].orderItems[1].productName", is("피자")))
                .andExpect(jsonPath("$[0].orderItems[1].quantity", is(1)))
                .andExpect(jsonPath("$[0].orderItems[1].price", is(15000)))
                .andExpect(jsonPath("$[0].orderItems[1].imageUrl", is("http://example.com/pizza.jpg")))
                .andExpect(jsonPath("$[0].totalPrice", is(25000)))
                .andExpect(jsonPath("$[0].usedPoint", is(100)))
                .andExpect(jsonPath("$[0].earnedPoint", is(50)))

                .andExpect(jsonPath("$[1].orderId", is(2)))
                .andExpect(jsonPath("$[1].createdAt", is("2022-05-31T10:00:00")))
                .andExpect(jsonPath("$[1].orderItems", hasSize(3)))
                .andExpect(jsonPath("$[1].orderItems[0].productId", is(1)))
                .andExpect(jsonPath("$[1].orderItems[0].productName", is("치킨")))
                .andExpect(jsonPath("$[1].orderItems[0].quantity", is(5)))
                .andExpect(jsonPath("$[1].orderItems[0].price", is(10000)))
                .andExpect(jsonPath("$[1].orderItems[0].imageUrl", is("http://example.com/chicken.jpg")))
                .andExpect(jsonPath("$[1].orderItems[1].productId", is(2)))
                .andExpect(jsonPath("$[1].orderItems[1].productName", is("피자")))
                .andExpect(jsonPath("$[1].orderItems[1].quantity", is(7)))
                .andExpect(jsonPath("$[1].orderItems[1].price", is(15000)))
                .andExpect(jsonPath("$[1].orderItems[1].imageUrl", is("http://example.com/pizza.jpg")))
                .andExpect(jsonPath("$[1].orderItems[2].productId", is(3)))
                .andExpect(jsonPath("$[1].orderItems[2].productName", is("샐러드")))
                .andExpect(jsonPath("$[1].orderItems[2].quantity", is(10)))
                .andExpect(jsonPath("$[1].orderItems[2].price", is(20000)))
                .andExpect(jsonPath("$[1].orderItems[2].imageUrl", is("http://example.com/salad.jpg")))
                .andExpect(jsonPath("$[1].totalPrice", is(355000)))
                .andExpect(jsonPath("$[1].usedPoint", is(300)))
                .andExpect(jsonPath("$[1].earnedPoint", is(200)));
    }

    @Test
    void getOrderById() throws Exception {
        given(orderService.getOrderById(any(), any())).willReturn(OrderResponseFixture.ORDER1_RESPONSE);
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(1)))
                .andExpect(jsonPath("$.createdAt", is("2023-05-31T10:00:00")))
                .andExpect(jsonPath("$.orderItems", hasSize(2)))
                .andExpect(jsonPath("$.orderItems[0].productId", is(1)))
                .andExpect(jsonPath("$.orderItems[0].productName", is("치킨")))
                .andExpect(jsonPath("$.orderItems[0].quantity", is(1)))
                .andExpect(jsonPath("$.orderItems[0].price", is(10000)))
                .andExpect(jsonPath("$.orderItems[0].imageUrl", is("http://example.com/chicken.jpg")))
                .andExpect(jsonPath("$.orderItems[1].productId", is(2)))
                .andExpect(jsonPath("$.orderItems[1].productName", is("피자")))
                .andExpect(jsonPath("$.orderItems[1].quantity", is(1)))
                .andExpect(jsonPath("$.orderItems[1].price", is(15000)))
                .andExpect(jsonPath("$.orderItems[1].imageUrl", is("http://example.com/pizza.jpg")))
                .andExpect(jsonPath("$.totalPrice", is(25000)))
                .andExpect(jsonPath("$.usedPoint", is(100)))
                .andExpect(jsonPath("$.earnedPoint", is(50)));
    }
}

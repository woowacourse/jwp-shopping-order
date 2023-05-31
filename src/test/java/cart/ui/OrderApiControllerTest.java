package cart.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import cart.dao.MemberDao;
import cart.dto.OrderRequest;
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
    private MemberDao memberDao;

    @Test
    void createOrder() throws Exception {
        final String request = objectMapper.writeValueAsString(new OrderRequest(List.of(1L, 2L, 3L), 1_000, 15_000));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/orders/1"));
    }

    @Test
    void getOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].orderId", is(1)))
                .andExpect(jsonPath("$[0].createAt", is("2023-05-26")))
                .andExpect(jsonPath("$[0].orderItems", hasSize(2)))
                .andExpect(jsonPath("$[0].orderItems[0].productId", is(10)))
                .andExpect(jsonPath("$[0].orderItems[0].productName", is("새우깡")))
                .andExpect(jsonPath("$[0].orderItems[0].quantity", is(3)))
                .andExpect(jsonPath("$[0].orderItems[0].price", is(1500)))
                .andExpect(jsonPath("$[0].orderItems[0].imageUrl", is("http://example.com/dfdf")))
                .andExpect(jsonPath("$[0].orderItems[1].productId", is(22)))
                .andExpect(jsonPath("$[0].orderItems[1].productName", is("감자깡")))
                .andExpect(jsonPath("$[0].orderItems[1].quantity", is(1)))
                .andExpect(jsonPath("$[0].orderItems[1].price", is(1200)))
                .andExpect(jsonPath("$[0].orderItems[1].imageUrl", is("http://example.com/abcd")))
                .andExpect(jsonPath("$[0].totalPrice", is(15000)))
                .andExpect(jsonPath("$[0].usedPoint", is(1700)))
                .andExpect(jsonPath("$[0].earnedPoint", is(300)))
                .andExpect(jsonPath("$[1].orderId", is(3)))
                .andExpect(jsonPath("$[1].createAt", is("2023-05-25")))
                .andExpect(jsonPath("$[1].orderItems", hasSize(2)))
                .andExpect(jsonPath("$[1].orderItems[0].productId", is(10)))
                .andExpect(jsonPath("$[1].orderItems[0].productName", is("새우깡")))
                .andExpect(jsonPath("$[1].orderItems[0].quantity", is(3)))
                .andExpect(jsonPath("$[1].orderItems[0].price", is(1500)))
                .andExpect(jsonPath("$[1].orderItems[0].imageUrl", is("http://example.com/dfdf")))
                .andExpect(jsonPath("$[1].orderItems[1].productId", is(22)))
                .andExpect(jsonPath("$[1].orderItems[1].productName", is("감자깡")))
                .andExpect(jsonPath("$[1].orderItems[1].quantity", is(1)))
                .andExpect(jsonPath("$[1].orderItems[1].price", is(1200)))
                .andExpect(jsonPath("$[1].orderItems[1].imageUrl", is("http://example.com/abcd")))
                .andExpect(jsonPath("$[1].totalPrice", is(15000)))
                .andExpect(jsonPath("$[1].usedPoint", is(1700)))
                .andExpect(jsonPath("$[1].earnedPoint", is(200)));
    }

    @Test
    void getOrder() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(1)))
                .andExpect(jsonPath("$.createAt", is("2023-05-26")))
                .andExpect(jsonPath("$.orderItems", hasSize(2)))
                .andExpect(jsonPath("$.orderItems[0].productId", is(10)))
                .andExpect(jsonPath("$.orderItems[0].productName", is("새우깡")))
                .andExpect(jsonPath("$.orderItems[0].quantity", is(3)))
                .andExpect(jsonPath("$.orderItems[0].price", is(1500)))
                .andExpect(jsonPath("$.orderItems[0].imageUrl", is("http://example.com/dfdf")))
                .andExpect(jsonPath("$.orderItems[1].productId", is(22)))
                .andExpect(jsonPath("$.orderItems[1].productName", is("감자깡")))
                .andExpect(jsonPath("$.orderItems[1].quantity", is(1)))
                .andExpect(jsonPath("$.orderItems[1].price", is(1200)))
                .andExpect(jsonPath("$.orderItems[1].imageUrl", is("http://example.com/abcd")))
                .andExpect(jsonPath("$.totalPrice", is(15000)))
                .andExpect(jsonPath("$.usedPoint", is(1700)))
                .andExpect(jsonPath("$.earnedPoint", is(300)));
    }
}

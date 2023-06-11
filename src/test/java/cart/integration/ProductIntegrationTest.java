package cart.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import cart.application.dto.ProductRequest;

public class ProductIntegrationTest extends IntegrationTest {

    @Test
    public void getProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk());
    }

    @Test
    public void createProduct() throws Exception {
        ProductRequest product = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        String jsonRequest = new ObjectMapper().writeValueAsString(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
            .andExpect(status().isCreated());
    }

    @Test
    public void getCreatedProduct() throws Exception {
        // given
        ProductRequest product = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
        String jsonRequest = new ObjectMapper().writeValueAsString(product);

        // when
        String location = mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
            .andReturn()
            .getResponse()
            .getHeader("Location");

        // get product
        mockMvc.perform(MockMvcRequestBuilders.get(location))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("치킨"))
            .andExpect(jsonPath("$.price").value(10_000));
    }
}

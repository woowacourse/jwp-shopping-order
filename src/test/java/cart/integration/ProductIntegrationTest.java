package cart.integration;

import static cart.TestDataFixture.OBJECT_MAPPER;
import static cart.TestDataFixture.PRODUCT_1;
import static cart.TestDataFixture.PRODUCT_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.service.response.ProductResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductIntegrationTest extends IntegrationTest {

    @DisplayName("limit과 offset으로 product를 조회한다.")
    @Test
    void productPagination() throws Exception {
        for (int i = 0; i < 5; i++) {
            productRepository.insertProduct(PRODUCT_1);
        }
        for (int i = 0; i < 5; i++) {
            productRepository.insertProduct(PRODUCT_2);
        }
        final ProductResponse product2Response = ProductResponse.from(PRODUCT_2);

        final String responseJsonString = mockMvc
                .perform(get("/products")
                        .param("limit", "5")
                        .param("scroll-count", "1")
                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        final List<ProductResponse> responses = OBJECT_MAPPER.readValue(responseJsonString, new TypeReference<>() {
        });
        assertThat(responses)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(product2Response, product2Response, product2Response, product2Response,
                        product2Response));
    }
}

package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderDetailResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CartItemDao cartItemDao;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("주문 정보를 저장, 조회한다.")
    void createOrder() throws JsonProcessingException {
        Member member = memberDao.findByEmail("yis092521@gmail.com");
        final List<Product> products = productDao.findAll();
        final ArrayList<Long> cartItemIds = new ArrayList<>();
        products.forEach(product -> cartItemIds.add(cartItemDao.save(new CartItem(member, product))));
        final OrderCreateRequest request = new OrderCreateRequest(cartItemIds, "1234-1234-1234-1234", 345,
                300);
        final String location = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/orders")
                .then()
                .extract().header("Location");

        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get(location)
                .then()
                .extract();

        final OrderDetailResponse orderDetailResponse = response.jsonPath()
                .getObject(".", OrderDetailResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderDetailResponse.getUsedPoint()).isEqualTo(300),
                () -> assertThat(orderDetailResponse.getSavedPoint()).isEqualTo(
                        products.stream().mapToInt(Product::getPrice)
                                .sum() / 10)
        );
    }
}

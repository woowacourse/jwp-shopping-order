package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.Member;
import cart.domain.Point;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderItemResponse;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
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

    private Member member;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        member = MemberEntity.toDomain(memberDao.findByEmail("yis092521@gmail.com").get());
    }

    @Test
    @DisplayName("주문 정보를 저장, 조회한다.")
    void createOrder() throws JsonProcessingException {
        final List<ProductEntity> products = productDao.findAll();
        final List<Long> cartItemIds = products.stream()
                .map(product -> cartItemDao.save(new CartItemEntity(member.getId(), product.getId())))
                .collect(Collectors.toUnmodifiableList());

        final OrderCreateRequest request = new OrderCreateRequest(
                cartItemIds,
                "1234-1234-1234-1234",
                345,
                300
        );

        final String location = requestForCreateOrder(request).header("Location");

        final ExtractableResponse<Response> response = requestForFindOrderDetail(location);

        final OrderDetailResponse orderDetailResponse = response.jsonPath()
                .getObject(".", OrderDetailResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderDetailResponse.getUsedPoint()).isEqualTo(300),
                () -> assertThat(orderDetailResponse.getSavedPoint()).isEqualTo(calculateExpectedSavingPoint(products)),
                () -> assertThat(orderDetailResponse.getProducts()).extracting(OrderItemResponse::getProduct)
                        .usingRecursiveComparison()
                        .isEqualTo(products)
        );
    }

    @Test
    @DisplayName("사용자의 전체 주문 정보를 조회한다.")
    void findOrders() throws JsonProcessingException {
        final List<ProductEntity> products = productDao.findAll();

        final List<Long> cartItemIds = products.stream()
                .map(product -> cartItemDao.save(new CartItemEntity(member.getId(), product.getId())))
                .collect(Collectors.toUnmodifiableList());

        final List<ProductEntity> productsWithoutPizza = products.stream()
                .filter(product -> !(product.getName().equals("피자")))
                .collect(Collectors.toUnmodifiableList());

        final List<Long> cartItemsForAnotherOrder = productsWithoutPizza.stream()
                .map(product -> cartItemDao.save(new CartItemEntity(member.getId(), product.getId())))
                .collect(Collectors.toUnmodifiableList());

        final OrderCreateRequest request = new OrderCreateRequest(
                cartItemIds,
                "1234-1234-1234-1234",
                345,
                300
        );
        final OrderCreateRequest requestForAnotherMember = new OrderCreateRequest(
                cartItemsForAnotherOrder,
                "1234-1234-1234-1234",
                345,
                500
        );
        requestForCreateOrder(request);
        requestForCreateOrder(requestForAnotherMember);

        final var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .extract();

        final var orderDetailResponses = response.jsonPath().getList(".", OrderDetailResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(orderDetailResponses).hasSize(2),

                () -> assertThat(orderDetailResponses.get(0).getUsedPoint()).isEqualTo(300),
                () -> assertThat(orderDetailResponses.get(0).getSavedPoint()).isEqualTo(
                        calculateExpectedSavingPoint(products)),
                () -> assertThat(orderDetailResponses.get(0).getProducts()).extracting(OrderItemResponse::getProduct)
                        .usingRecursiveComparison()
                        .isEqualTo(products),

                () -> assertThat(orderDetailResponses.get(1).getUsedPoint()).isEqualTo(500),
                () -> assertThat(orderDetailResponses.get(1).getSavedPoint()).isEqualTo(
                        calculateExpectedSavingPoint(productsWithoutPizza)),
                () -> assertThat(orderDetailResponses.get(1).getProducts()).extracting(OrderItemResponse::getProduct)
                        .usingRecursiveComparison()
                        .isEqualTo(productsWithoutPizza)
        );
    }

    private ExtractableResponse<Response> requestForCreateOrder(final OrderCreateRequest request)
            throws JsonProcessingException {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/orders")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> requestForFindOrderDetail(final String location) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get(location)
                .then()
                .extract();
    }

    private int calculateExpectedSavingPoint(final List<ProductEntity> products) {
        return Point.fromPayment(
                products.stream()
                        .mapToInt(ProductEntity::getPrice)
                        .sum()
        ).getValue();
    }
}

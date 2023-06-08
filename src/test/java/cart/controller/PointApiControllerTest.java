package cart.controller;

import cart.dao.*;
import cart.dao.entity.OrderEntity;
import cart.dao.entity.OrderItemEntity;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.order.OrderItemDto;
import cart.dto.order.OrderRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class PointApiControllerTest extends IntegrationTest {

    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    private Member member1;
    private Product product;

    @BeforeEach
    void initial() {
        member1 = memberDao.getMemberById(1L).get();
        product = productDao.getProductById(1L).get();
        cartItemDao.save(new CartItem(1L, 1L, product, member1));
        Long orderId = orderDao.createOrder(new OrderEntity(member1.getId(), 0L, 60000L, 0L));
        orderItemDao.createOrderItem(new OrderItemEntity(product.getName(), product.getPrice(), product.getImageUrl(), 3L, product.getId(), orderId));
    }

    @DisplayName("회원의 포인트를 가져온다")
    @Test
    void getMemberPoint() {
        OrderItemDto orderItemDto1 = new OrderItemDto(product.getId(), 3L);
        OrderItemDto orderItemDto2 = new OrderItemDto(product.getId(), 3L);
        System.out.println(orderItemDto1.getCartItemId());
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        orderItemDtoList.add(orderItemDto1);
        orderItemDtoList.add(orderItemDto2);

        OrderRequest orderRequest = new OrderRequest(60000L, 0L, 0L, orderItemDtoList);
        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(orderRequest)
                .when().post("/orders")
                .then()
                .extract();

        ExtractableResponse<Response> response = given()
                .log().all()
                .auth().preemptive().basic(member1.getEmail(), member1.getPassword())
                .body(orderRequest)
                .when().get("/point")
                .then()
                .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getLong("usablePoint")).isEqualTo( 6000L);
    }

}

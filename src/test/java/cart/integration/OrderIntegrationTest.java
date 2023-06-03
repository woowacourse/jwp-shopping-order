package cart.integration;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.dao.ShoppingOrderDao;
import cart.domain.Member;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CartItemDao cartItemDao;

    @Autowired
    private ShoppingOrderDao shoppingOrderDao;

    private Long productId;
    private Long productId2;
    private Member member;
    private Member member2;

    @DisplayName("주문을 생성한다")
    @Test
    void createOrder() {
        member = memberDao.getMemberByEmail("kangsj9665@gmail.com").get();
        member2 = memberDao.getMemberByEmail("yis092521@gmail.com").get();

        memberDao.updatePoints(300L, member);

        createOrder(member, List.of(1), 300);
    }

    @DisplayName("사용자의 전체 주문 목록을 불러온다")
    @Test
    void getAllOrders() {
        member = memberDao.getMemberByEmail("kangsj9665@gmail.com").get();
        member2 = memberDao.getMemberByEmail("yis092521@gmail.com").get();


        memberDao.updatePoints(1000L, member);
        memberDao.updatePoints(300L, member2);

        createOrder(member, List.of(1, 2), 400);
        createOrder(member, List.of(4), 100);
        createOrder(member, List.of(3), 10);

        getOrders(member);
    }

    @DisplayName("특정 주문의 상세정보를 불러온다")
    @Test
    void getOrderDetail() {
        member = memberDao.getMemberByEmail("kangsj9665@gmail.com").get();

        getOrderDetails(member, 1L);
    }

    private void createOrder(Member member, List<Integer> cartItemIds, int point) {
        Map<String, Object> params = new HashMap<>();
        params.put("cartItemIds", cartItemIds);
        params.put("cardNumber", "4043-0304-1299-4949");
        params.put("cvc", 123);
        params.put("point", point);


        given().body(params)
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .post("/orders")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED);
    }

    private ExtractableResponse<Response> getOrders(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> getOrderDetails(Member member, Long id) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/" + id)
                .then()
                .log().all()
                .extract();
    }
}

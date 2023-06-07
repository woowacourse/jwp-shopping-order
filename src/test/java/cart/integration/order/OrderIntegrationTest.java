package cart.integration.order;

import cart.dao.cart.CartItemDao;
import cart.dao.member.MemberDao;
import cart.dao.product.ProductDao;
import cart.domain.member.Member;
import cart.domain.member.Rank;
import cart.dto.order.OrderRequest;
import cart.dto.order.OrderResponse;
import cart.entity.CartItemEntity;
import cart.entity.MemberEntity;
import cart.integration.IntegrationTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixture.MemberFixture.ako;
import static cart.fixture.ProductFixture.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private CartItemDao cartItemDao;

    private Member member;
    private List<Long> cartItemIds;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        Long chickenId = productDao.createProduct(chicken);
        Long forkId = productDao.createProduct(fork);
        chicken = generateChicken(chickenId); // 9500 상품할인
        fork = generateFork(forkId); // 13500 멤버할인


        Long akoId = memberDao.addMember(ako);
        MemberEntity updateMemberEntity = new MemberEntity(akoId, ako.getEmail(), ako.getPassword(), ako.getGrade(), ako.getTotalPurchaseAmount());
        memberDao.updateMember(updateMemberEntity);
        Long cartItem1 = cartItemDao.save(new CartItemEntity(1, akoId, chickenId));
        Long cartItem2 = cartItemDao.save(new CartItemEntity(1, akoId, forkId));

        cartItemIds = List.of(cartItem1, cartItem2);

        member = makeMember(updateMemberEntity);
    }

    private Member makeMember(final MemberEntity memberEntity) {
        return new Member(memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                Rank.valueOf(memberEntity.getGrade()),
                memberEntity.getTotalPurchaseAmount());
    }

    @Test
    @DisplayName("올바른 주문을 생성한다.")
    void create_order() {
        // given
        OrderRequest orderRequest = new OrderRequest(
                cartItemIds,
                500,
                1500,
                25000,
                23000,
                3000,
                26000);

        // when
        ExtractableResponse<Response> result = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();

        // then
        String location = result.header("Location");
        assertThat(location).isNotBlank();
    }

    @Test
    @DisplayName("주문 전체 조회를 한다.")
    void find_order_by_member() {
        // given
        OrderRequest orderRequest = new OrderRequest(
                cartItemIds,
                500,
                1500,
                25000,
                23000,
                3000,
                26000);

        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then();

        // when
        ExtractableResponse<Response> result = given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then().log().all()
                .extract();


        List<OrderResponse> response = result.jsonPath().getList(".", OrderResponse.class);

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getTotalItemDiscountAmount()).isEqualTo(orderRequest.getTotalItemDiscountAmount());
        assertThat(response.get(0).getTotalMemberDiscountAmount()).isEqualTo(orderRequest.getTotalMemberDiscountAmount());
        assertThat(response.get(0).getTotalItemPrice()).isEqualTo(orderRequest.getTotalItemPrice());
        assertThat(response.get(0).getDiscountedTotalItemPrice()).isEqualTo(orderRequest.getDiscountedTotalItemPrice());
        assertThat(response.get(0).getShippingFee()).isEqualTo(orderRequest.getShippingFee());
    }

    @Test
    @DisplayName("특정 주문 전체 조회를 한다.")
    void find_order_by_Id() {
        // given
        OrderRequest orderRequest = new OrderRequest(
                cartItemIds,
                500,
                1500,
                25000,
                23000,
                3000,
                26000);

        ExtractableResponse<Response> postResponse = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .extract();

        String createdLocation = postResponse.header("Location").split("/")[2];

        // when
        ExtractableResponse<Response> result = given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/" + createdLocation)
                .then().log().all()
                .extract();


        OrderResponse response = result.jsonPath().getObject(".", OrderResponse.class);

        assertThat(response.getTotalItemDiscountAmount()).isEqualTo(orderRequest.getTotalItemDiscountAmount());
        assertThat(response.getTotalMemberDiscountAmount()).isEqualTo(orderRequest.getTotalMemberDiscountAmount());
        assertThat(response.getTotalItemPrice()).isEqualTo(orderRequest.getTotalItemPrice());
        assertThat(response.getDiscountedTotalItemPrice()).isEqualTo(orderRequest.getDiscountedTotalItemPrice());
        assertThat(response.getShippingFee()).isEqualTo(orderRequest.getShippingFee());
    }
}

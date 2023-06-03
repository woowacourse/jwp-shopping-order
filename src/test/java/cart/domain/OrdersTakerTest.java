package cart.domain;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static cart.fixture.Fixture.TEST_MEMBER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrdersTakerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private OrdersTaker ordersTaker;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void takeOrder() {
        List<Long> cartIds = List.of(1L, 2L, 3L);
        List<Long> coupons = List.of(1L, 2L, 3L);
        Assertions.assertThat(ordersTaker.takeOrder(1L, cartIds, coupons)).isEqualTo(3L);
    }

    @Test
    void findOrdersWithOriginalPrice() {
        Assertions.assertThat(ordersTaker.findOrdersWithMember(TEST_MEMBER)).hasSize(2);
    }

    @Test
    void findOrdersWithId() {
        Assertions.assertThat(ordersTaker.findOrdersWithId(TEST_MEMBER, 1L).getOrderProducts().get(0).getProduct().getName()).isEqualTo("현루피");
    }
}

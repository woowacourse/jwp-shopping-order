package cart.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Test
    @DisplayName("멤버의 포인트를 조회한다.")
    void findPointOfMember() {
    }

    @Test
    void calculatePointByPayment() {
    }
}

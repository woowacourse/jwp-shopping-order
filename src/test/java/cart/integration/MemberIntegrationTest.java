package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

public class MemberIntegrationTest extends IntegrationTest {

    @BeforeEach
    @Sql("/clean-up.sql")
    void init() {
    }

    @DisplayName("초기 멤버를 생성하면 기본 포인트는 5,000원이다.")
    @Test
    void member_initial_point() {
        String email = "test@email.com";
        String password = "123";
        requestCreateMember(email, password);

        ExtractableResponse<Response> response = requestShowMemberPoint(email, password);
        int point = response.jsonPath().getInt("point");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(point).isEqualTo(5_000)
        );
    }
}

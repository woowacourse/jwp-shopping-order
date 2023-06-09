package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberIntegrationTest extends IntegrationTest {

    @Test
    void 전체_회원의_정보를_가져올_수_있다() {
        // given
        final var result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members")
                .then()
                .extract();

        // expect
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

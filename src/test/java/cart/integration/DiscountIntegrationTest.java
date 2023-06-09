package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DiscountIntegrationTest extends IntegrationTest {

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @Test
    void 할인_정보를_알_수_있다() {
        // given
        final int price = 10_000;
        final String grade = "silver";

        // when
        final var result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/discount?price={price}&memberGrade={grade}", price, grade)
                .then().log().all()
                .extract().body();

        // then
        assertThat(result.asString()).contains(
                "policyName", "memberGradeDiscount",
                "discountRate", "0.03",
                "discountPrice", "300",

                "policyName", "priceDiscount",
                "discountRate", "0.02",
                "discountPrice", "200"
        );
    }


}

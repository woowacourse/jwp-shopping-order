package cart.integration;

import cart.dto.PageRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static cart.common.step.ProductStep.readAllProducts;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class ExceptionIntegrationTest extends IntegrationTest {

    @Sql("classpath:testData.sql")
    @Test
    void 페이징_정보를_입력하지_않은_경우_예외를_던진다() {
        //given
        final PageRequest pageRequest = new PageRequest(null, null);

        //when
        final ExtractableResponse<Response> response = readAllProducts(pageRequest);

        //then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).isEqualTo("page 정보가 입력되지 않았습니다.");
        });
    }
}

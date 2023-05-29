package cart.acceptance;

import cart.dao.ProductDao;
import cart.domain.Product;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static cart.fixture.ProductFixtures.PRODUCT_REQUEST_CAMERA_EOS_M200;
import static cart.fixture.ProductFixtures.UPDATED_PRODUCT_REQUEST_CAMERA_EOS_M200;
import static cart.steps.ProductSteps.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

public class ProductAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ProductDao productDao;

    @Test
    void 제품을_추가할_수_있다() {
        final ExtractableResponse<Response> response = 제품_추가_요청(PRODUCT_REQUEST_CAMERA_EOS_M200);
        Long id = 추가된_제품_아이디_반환(response);
        Product savedProduct = productDao.getProductById(id);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(savedProduct.getId()).isEqualTo(id)
        );
    }

    @Test
    void 제품_정보를_업데이트할_수_있다() {
        final Long id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_CAMERA_EOS_M200);

        final ExtractableResponse<Response> response = 제품_정보_업데이트_요청(id, UPDATED_PRODUCT_REQUEST_CAMERA_EOS_M200);

        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 제품을_삭제할_수_있다() {
        final Long id = 제품_추가하고_아이디_반환(PRODUCT_REQUEST_CAMERA_EOS_M200);

        final ExtractableResponse<Response> response = 제품_삭제_요청(id);

        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }
}

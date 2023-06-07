package cart.common.step;

import cart.dto.PageRequest;
import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class ProductStep {

    public static ExtractableResponse<Response> addProduct(final ProductRequest product) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .post("/products")
                .then()
                .extract();
    }

    public static Long addProductAndGetId(final ProductRequest productRequest) {
        final ExtractableResponse<Response> response = addProduct(productRequest);
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    public static ExtractableResponse<Response> readAllProducts(final PageRequest pageRequest) {
        return given().log().all()
                .param("page", pageRequest.getPage())
                .param("size", pageRequest.getSize())
                .when()
                .get("/products")
                .then()
                .extract();
    }
}

package cart.application;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import cart.dto.request.ProductRequest;

class ProductServiceTest extends ServiceTest {
	@Test
	void 상품을_등록한다(){
		// given
		final ProductRequest productRequest = new ProductRequest("햄버거", 15000, "햄버거.jpg");

		// when
		final Long productId = productService.createProduct(productRequest);

		// then
		assertThat(productId).isEqualTo(4);
	}
}

package shop.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import shop.application.product.ProductService;
import shop.application.product.dto.ProductDto;
import shop.application.product.dto.ProductModificationDto;
import shop.exception.DatabaseException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceImplTest extends ServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createProductTest() {
        //given
        ProductModificationDto productCreateionDto =
                new ProductModificationDto("게토레이", 2400, "게토레이.com");

        //when
        Long productId = productService.createProduct(productCreateionDto);

        //then
        ProductDto product = productService.getProductById(productId);

        assertThat(product.getName()).isEqualTo("게토레이");
        assertThat(product.getPrice()).isEqualTo(2400);
        assertThat(product.getImageUrl()).isEqualTo("게토레이.com");
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void updateProductTest() {
        //given
        ProductModificationDto productCreationDto =
                new ProductModificationDto("게토레이", 2400, "게토레이.com");
        ProductModificationDto productModificationDto =
                new ProductModificationDto("파워에이드", 2000, "파워에이드.com");

        Long productId = productService.createProduct(productCreationDto);
        ProductDto productBeforeUpdate = productService.getProductById(productId);

        assertThat(productBeforeUpdate.getName()).isEqualTo("게토레이");
        assertThat(productBeforeUpdate.getPrice()).isEqualTo(2400);
        assertThat(productBeforeUpdate.getImageUrl()).isEqualTo("게토레이.com");

        //when
        productService.updateProduct(productId, productModificationDto);

        //then
        ProductDto product = productService.getProductById(productId);

        assertThat(product.getName()).isEqualTo("파워에이드");
        assertThat(product.getPrice()).isEqualTo(2000);
        assertThat(product.getImageUrl()).isEqualTo("파워에이드.com");
    }

    @DisplayName("상품을 삭제할 수 있다.")
    @Test
    void deleteProductTest() {
        //given
        ProductModificationDto productCreationDto =
                new ProductModificationDto("게토레이", 2400, "게토레이.com");

        Long productId = productService.createProduct(productCreationDto);

        //when
        productService.deleteProduct(productId);

        //then
        Assertions.assertThatThrownBy(() -> productService.getProductById(productId))
                .isInstanceOf(DatabaseException.IllegalDataException.class);
    }

    @DisplayName("모든 상품을 조회할 수 있다.")
    @Test
    void getAllProductTest() {
        List<ProductDto> allProducts = productService.getAllProducts();

        assertThat(allProducts.size()).isEqualTo(3); //initialization.sql
        assertThat(allProducts).extractingResultOf("getName")
                .containsExactlyInAnyOrder("치킨", "피자", "샐러드");
        assertThat(allProducts).extractingResultOf("getPrice")
                .containsExactlyInAnyOrder(10000, 20000, 13000);

    }
}

package cart.application;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품_등록_요청을_받아_저장한다() {
        // given
        ProductRequest 요청 = new ProductRequest("새로운상품", 10000, "새로운이미지");

        // when
        Long 저장된_상품_ID = productService.save(요청);
        Product 저장된_상품 = productRepository.findById(저장된_상품_ID);

        // then
        assertThat(저장된_상품).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(요청);
    }

    @Test
    void 모든_상품을_조회한다() {
        // given
        Product 저장된_첫번째_상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(저장할_상품을_생성한다("치킨", 20000, "치킨이미지"));
        Product 저장된_두번째_상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(저장할_상품을_생성한다("피자", 12000, "피자이미지"));

        // when
        List<ProductResponse> 조회한_상품들 = productService.findAll();

        // then
        assertThat(조회한_상품들).usingRecursiveComparison()
                .isEqualTo(List.of(저장된_첫번째_상품, 저장된_두번째_상품));
    }

    private Product 상품을_저장하고_ID가_있는_상품을_리턴한다(Product 저장할_상품) {
        Long 저장된_ID = productRepository.save(저장할_상품);
        return new Product(저장된_ID, 저장할_상품.getName(), 저장할_상품.getPrice(), 저장할_상품.getImageUrl());
    }

    private Product 저장할_상품을_생성한다(String 이름, int 가격, String 이미지_URL) {
        return new Product(null, 이름, 가격, 이미지_URL);
    }

    @Test
    void 상품을_ID로_조회한다() {
        // given
        Product 저장된_상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(저장할_상품을_생성한다("치킨", 20000, "치킨이미지"));

        // when
        ProductResponse 응답 = productService.findById(저장된_상품.getId());

        // then
        assertThat(응답).usingRecursiveComparison()
                .isEqualTo(저장된_상품);
    }

    @Test
    void 상품을_수정한다() {
        // given
        Product 저장된_상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(저장할_상품을_생성한다("치킨", 20000, "치킨이미지"));
        ProductRequest 변경_요청 = new ProductRequest("변경된치킨", 10000, "변경된이미지");

        // when
        productService.update(저장된_상품.getId(), 변경_요청);

        // then
        Product 변경된_상품 = productRepository.findById(저장된_상품.getId());
        assertThat(변경된_상품).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(변경_요청);
    }
}

package cart.dao;

import cart.domain.product.Product;
import cart.domain.product.ProductImageUrl;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import cart.exception.notfound.MemberNotFoundException;
import cart.exception.notfound.ProductNotFoundException;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Import({ProductRepository.class, ProductDao.class, CartItemDao.class})
public class ProductDaoTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDao productDao;

    final Product product = new Product("초콜릿", 500, "초콜릿URL");


    @DisplayName("상품을 추가하고 찾아온다.")
    @Test
    void createAndGetProduct() {
        // given
        final Long productId = productDao.insert(product);

        // when
        final Product findProduct = productDao.findById(productId).orElseThrow(ProductNotFoundException::new);

        // then
        assertAll(
                () -> assertThat(findProduct.getProductName()).isEqualTo(new ProductName("초콜릿")),
                () -> assertThat(findProduct.getProductPrice()).isEqualTo(new ProductPrice(500)),
                () -> assertThat(findProduct.getProductImageUrl()).isEqualTo(new ProductImageUrl("초콜릿URL"))
        );
    }

    @DisplayName("List<Long> productIds로 Product들을 찾아온다.")
    @Test
    void getProductsByIds() {
        // given
        final Product product1 = new Product("초콜릿1", 501, "초콜릿URL1");
        final Product product2 = new Product("초콜릿2", 502, "초콜릿URL2");
        final Product product3 = new Product("초콜릿3", 503, "초콜릿URL3");
        final Long productId1 = productDao.insert(product1);
        final Long productId2 = productDao.insert(product2);
        final Long productId3 = productDao.insert(product3);

        // when
        final List<Product> products = productDao.findAllByIds(List.of(productId1, productId2, productId3));

        // then
        assertAll(
                () -> assertThat(products).hasSize(3),
                () -> assertThat(products.get(0).getId()).isEqualTo(productId1),
                () -> assertThat(products.get(1).getId()).isEqualTo(productId2),
                () -> assertThat(products.get(2).getId()).isEqualTo(productId3)
        );
    }

    @DisplayName("상품을 수정할 수 있다.")
    @Test
    void updateProduct() {
        // given
        final Long productId = productDao.insert(product);
        final Product findProduct = productDao.findById(productId).orElseThrow(ProductNotFoundException::new);
        final Product updateProduct = new Product(findProduct.getId(), "초콜릿아님", findProduct.getPriceValue(), findProduct.getImageUrlValue());

        // when
        productDao.update(updateProduct);
        final Product updatedProduct = productDao.findById(productId).orElseThrow(ProductNotFoundException::new);

        // then
        assertThat(updatedProduct.getProductName()).isEqualTo(new ProductName("초콜릿아님"));
    }

    @DisplayName("상품을 삭제할 수 있다.")
    @Test
    void deleteProduct1() {
        // given
        final Long productId = productDao.insert(product);

        // when
        productDao.deleteById(productId);

        // then
        assertThat(productDao.findById(productId)).isEmpty();
    }

    @DisplayName("사용자가 장바구니에 추가한 상품을 삭제할 수 있다.")
    @Test
    void deleteProduct2() {
        // given
        final Product product = productDao.findById(1L).orElseThrow(MemberNotFoundException::new);

        // when
        productRepository.deleteProduct(product);

        // then
        assertThat(productDao.findById(product.getId()).isEmpty());
    }
}

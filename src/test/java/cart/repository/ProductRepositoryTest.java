package cart.repository;

import cart.dao.ProductDao;
import cart.dao.entity.ProductEntity;
import cart.domain.product.Product;
import cart.exception.ProductException;
import cart.fixture.ProductFixture;
import cart.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cart.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductRepository productRepository;

    @Test
    void 저장한다() {
        Long expectedProductId = 지구_엔티티.getId();
        when(productDao.save(지구_엔티티)).thenReturn(expectedProductId);

        Long createdProductId = productRepository.create(지구);

        assertThat(createdProductId).isEqualTo(expectedProductId);
    }

    @Test
    void 모두_조회한다() {
        List<ProductEntity> productEntities = List.of(지구_엔티티, 화성_엔티티);
        List<Product> expectedProducts = List.of(지구, 화성);
        when(productDao.findAll()).thenReturn(productEntities);

        List<Product> products = productRepository.findAll();

        assertThat(products).isEqualTo(expectedProducts);
    }

    @Test
    void 아이디로_조회한다() {
        Long productId = 지구.getId();
        when(productDao.findById(productId)).thenReturn(Optional.of(지구_엔티티));

        Product product = productRepository.findById(productId);

        assertThat(product).isEqualTo(지구);
    }

    @Test
    void 없는_아이디를_조회하면_예외를_반환한다() {
        Long productId = 1L;

        when(productDao.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productRepository.findById(productId)).isInstanceOf(ProductException.NoExist.class);
    }

    @Test
    void 수정한다() {
        ProductEntity productEntity = new ProductEntity(1L, "지구", 100, "지구사진");
        Product product = new Product(1L, "지구", 100, "지구사진");

        productRepository.update(product);

        verify(productDao).update(productEntity);
    }

    @Test
    void 삭제한다() {
        productRepository.delete(지구);

        verify(productDao).deleteById(지구.getId());
    }
}

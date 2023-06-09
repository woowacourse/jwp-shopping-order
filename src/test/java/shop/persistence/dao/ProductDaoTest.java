package shop.persistence.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import shop.exception.DatabaseException;
import shop.persistence.entity.ProductEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(ProductDao.class)
class ProductDaoTest extends DaoTest {
    @Autowired
    private ProductDao productDao;

    @DisplayName("상품을 추가할 수 있다.")
    @Test
    void insertProductTest() {
        //given
        ProductEntity pizza = new ProductEntity("피자", 40000, "피자.com");

        //when
        Long savedId = productDao.insert(pizza);

        //then
        ProductEntity findProduct = productDao.findById(savedId);

        assertThat(findProduct.getName()).isEqualTo(pizza.getName());
        assertThat(findProduct.getPrice()).isEqualTo(pizza.getPrice());
        assertThat(findProduct.getImageUrl()).isEqualTo(pizza.getImageUrl());
    }

    @DisplayName("상품의 정보를 수정할 수 있다")
    @Test
    void updateProductTest() {
        //given
        ProductEntity pizza = new ProductEntity("피자", 40000, "피자.com");
        ProductEntity chicken = new ProductEntity("치킨", 20000, "치킨.com");
        Long savedId = productDao.insert(pizza);

        //when
        productDao.updateProduct(savedId, chicken);

        //then
        ProductEntity findProduct = productDao.findById(savedId);
        assertThat(findProduct.getName()).isEqualTo(chicken.getName());
        assertThat(findProduct.getPrice()).isEqualTo(chicken.getPrice());
        assertThat(findProduct.getImageUrl()).isEqualTo(chicken.getImageUrl());
    }

    @DisplayName("상품의 정보를 삭제할 수 있다.")
    @Test
    void deleteProductTest() {
        //given
        ProductEntity pizza = new ProductEntity("피자", 40000, "피자.com");
        Long savedId = productDao.insert(pizza);

        //when
        productDao.deleteProduct(savedId);

        //then
        assertThatThrownBy(() -> productDao.findById(savedId))
                .isInstanceOf(DatabaseException.IllegalDataException.class);
    }
}

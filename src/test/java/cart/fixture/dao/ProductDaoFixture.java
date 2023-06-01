package cart.fixture.dao;

import cart.dao.ProductDao;
import cart.domain.product.Product;
import cart.dao.entity.ProductEntity;

import static cart.fixture.entity.ProductEntityFixture.상품_엔티티;

public class ProductDaoFixture {

    private final ProductDao productDao;

    public ProductDaoFixture(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product 상품을_등록한다(String 상품명, int 가격) {
        ProductEntity 상품_엔티티 = 상품_엔티티(상품명, 가격);
        Long 상품_식별자값 = productDao.insertProduct(상품_엔티티);

        return new Product(
                상품_식별자값,
                상품_엔티티.getName(),
                상품_엔티티.getMoney(),
                상품_엔티티.getImageUrl()
        );
    }
}

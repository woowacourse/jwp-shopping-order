package cart.fixture;

import cart.dao.entity.ProductEntity;
import cart.domain.product.Product;

public class ProductFixture {

    public static final ProductEntity 지구_엔티티 = new ProductEntity(1L, "지구", 1000, "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg");
    public static final Product 지구 = new Product(1L, "지구", 1000, "https://cdn.pixabay.com/photo/2011/12/13/14/28/earth-11009__480.jpg");
    public static final ProductEntity 화성_엔티티 = new ProductEntity(2L, "화성", 200000, "https://cdn.pixabay.com/photo/2011/12/13/14/30/mars-11012__480.jpg");
    public static final Product 화성 = new Product(2L, "화성", 200000, "https://cdn.pixabay.com/photo/2011/12/13/14/30/mars-11012__480.jpg");
    public static final ProductEntity 달_엔티티 = new ProductEntity(3L, "달", 300, "https://cdn.pixabay.com/photo/2016/04/02/19/40/moon-1303512__480.png");
    public static final Product 달 = new Product(3L, "달", 300, "https://cdn.pixabay.com/photo/2016/04/02/19/40/moon-1303512__480.png");
}

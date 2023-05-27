package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dao.ProductDao;
import cart.dto.AuthMember;
import cart.dto.ProductCartItemResponse;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final MemberDao memberDao;

    public ProductService(final ProductDao productDao, final CartItemDao cartItemDao,
                          final MemberDao memberDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.memberDao = memberDao;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productDao.getAllProducts();
        return products.stream().map(ProductResponse::from).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productDao.getProductById(productId);
        return ProductResponse.from(product);
    }

    public ProductCartItemResponse findProductCartItems(AuthMember authMember, Long productId) {
        Member findMember = memberDao.getMemberByEmail(authMember.getEmail());
        checkProductExist(productId);
        Product findProduct = productDao.getProductById(productId);
        Optional<CartItem> cartItem = cartItemDao.selectByMemberIdAndProductId(findMember.getId(), findProduct.getId());
        if (cartItem.isEmpty()) {
            return ProductCartItemResponse.createOnlyProduct(findProduct);
        }
        return ProductCartItemResponse.createContainsCartItem(findProduct, cartItem.get());
    }

    private void checkProductExist(Long id) {
        if (productDao.isNotExistById(id)) {
            throw new ProductNotFoundException("상품 ID에 해당하는 상품을 찾을 수 없습니다.");
        }
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}

package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.domain.product.Products;
import cart.dto.*;
import cart.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final int FIRST_PAGE_ID = 0;
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

    public ProductPagingResponse getAllPagingProductCartItems(AuthMember authMember,
                                                              Long lastProductId, int pageItemCount) {
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
        CartItems cartItems = new CartItems(cartItemDao.selectAllByMemberId(findMember.getId()));
        if (lastProductId == FIRST_PAGE_ID) {
            return getFirstPageProduct(pageItemCount, cartItems);
        }
        return getNotFirstPageProduct(lastProductId, pageItemCount, cartItems);
    }

    private ProductPagingResponse getFirstPageProduct(int pageItemCount, CartItems cartItems) {
        Products products = new Products(productDao.selectFirstProductsByLimit(pageItemCount));
        Product lastProduct = productDao.selectLastProduct();
        Boolean isLast = products.isContains(lastProduct);
        List<ProductCartItemResponse> productCartItems = products.getProductCartItems(cartItems);
        return new ProductPagingResponse(productCartItems, isLast);
    }

    private ProductPagingResponse getNotFirstPageProduct(Long lastProductId, int pageItemCount, CartItems cartItems) {
        Products products = new Products(productDao.selectProductsByIdAndLimit(lastProductId, pageItemCount));
        List<ProductCartItemResponse> productCartItems = products.getProductCartItems(cartItems);
        Product lastProduct = productDao.selectLastProduct();
        Boolean isLast = products.isContains(lastProduct);
        return new ProductPagingResponse(productCartItems, isLast);
    }

    public ProductCartItemResponse findProductCartItems(AuthMember authMember, Long productId) {
        Member findMember = memberDao.selectMemberByEmail(authMember.getEmail());
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
        return productDao.insertProduct(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        checkProductExist(productId);
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        checkProductExist(productId);
        productDao.deleteProduct(productId);
    }
}

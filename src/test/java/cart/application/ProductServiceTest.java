package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import cart.repository.mapper.MemberMapper;
import cart.test.ServiceTest;
import cart.ui.controller.dto.request.ProductRequest;
import cart.ui.controller.dto.response.ProductResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    private Product product;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        product = new Product("치킨", 10000, "http://chicken.com");
        Long productId = productRepository.createProduct(product);
        product.assignId(productId);
    }

    @Test
    @DisplayName("getAllProducts 메서드는 모든 상품 정보를 조회한다.")
    void getAllProducts() {
        Product otherProduct = new Product("피자", 13000, "http://pizza.com");
        Long otherProductId = productRepository.createProduct(otherProduct);
        otherProduct.assignId(otherProductId);

        List<ProductResponse> result = productService.getAllProducts();

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(ProductResponse.from(product)),
                () -> assertThat(result.get(1)).usingRecursiveComparison().isEqualTo(ProductResponse.from(otherProduct))
        );

    }

    @Test
    @DisplayName("getProductById 메서드 ID에 해당하는 상품 정보를 조회한다.")
    void getProductById() {
        ProductResponse result = productService.getProductById(1L);

        assertThat(result).usingRecursiveComparison().isEqualTo(ProductResponse.from(product));
    }

    @Test
    @DisplayName("updatedProduct 메서드는 상품을 업데이트한다.")
    void updateProduct() {
        ProductRequest request = new ProductRequest("피자", 13000, "http://pizza.com");

        productService.updateProduct(product.getId(), request);

        ProductResponse result = productService.getProductById(product.getId());
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(product.getId()),
                () -> assertThat(result.getName()).isEqualTo(request.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(request.getPrice()),
                () -> assertThat(result.getImageUrl()).isEqualTo(request.getImageUrl())
        );
    }

    @Nested
    @DisplayName("deleteProduct 메서드는 ")
    class DeleteProduct {

        @Test
        @DisplayName("상품의 장바구니 상품을 모두 삭제한다.")
        void deleteCartItems() {
            MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 10);
            Long memberId = memberDao.addMember(memberEntity);
            Member member = MemberMapper.toDomain(memberEntity);
            member.assignId(memberId);

            CartItem cartItem = new CartItem(member, product);
            cartItemRepository.save(cartItem);

            productService.deleteProduct(product.getId());

            List<Product> products = productRepository.getAllProducts();
            List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
            assertAll(
                    () -> assertThat(products).isEmpty(),
                    () -> assertThat(cartItems).isEmpty()
            );
        }

        @Test
        @DisplayName("상품을 삭제한다.")
        void deleteProduct() {
            productService.deleteProduct(product.getId());

            List<ProductResponse> result = productService.getAllProducts();
            assertThat(result).isEmpty();
        }
    }
}

package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.notfound.NotFoundException;
import cart.repository.mapper.MemberMapper;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@RepositoryTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("getAllProducts 메서드는 모든 상품을 조회한다.")
    void getAllProducts() {
        Product productA = new Product("치킨", 10000, "http://chicken.com");
        Product productB = new Product("샐러드", 20000, "http://salad.com");
        Product productC = new Product("피자", 13000, "http://pizza.com");
        Long productIdA = productRepository.createProduct(productA);
        Long productIdB = productRepository.createProduct(productB);
        Long productIdC = productRepository.createProduct(productC);
        productA.assignId(productIdA);
        productB.assignId(productIdB);
        productC.assignId(productIdC);

        List<Product> result = productRepository.getAllProducts();

        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(productA),
                () -> assertThat(result.get(1)).usingRecursiveComparison().isEqualTo(productB),
                () -> assertThat(result.get(2)).usingRecursiveComparison().isEqualTo(productC)
        );
    }

    @Test
    @DisplayName("updateProduct 메서드는 상품 정보를 수정한다.")
    void updateProduct() {
        Product product = new Product("치킨", 10000, "http://chicken.com");
        Long savedProductId = productRepository.createProduct(product);
        Product updateProduct = new Product(savedProductId, "피자", 13000, "http://pizza.com");

        productRepository.updateProduct(updateProduct);

        Product result = productRepository.getProductById(savedProductId);
        assertThat(result).usingRecursiveComparison().isEqualTo(updateProduct);
    }

    @Nested
    @DisplayName("deleteProduct 메서드는 ")
    class DeleteProduct {

        @Test
        @DisplayName("장바구니에 담겨 있는 상품이라면 예외를 던진다.")
        void existCartItemProduct() {
            MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 10);
            Long savedMemberId = memberDao.addMember(memberEntity);
            Member member = MemberMapper.toDomain(memberEntity);
            member.assignId(savedMemberId);

            Product product = new Product("치킨", 10000, "http://chicken.com");
            Long savedProductId = productRepository.createProduct(product);
            product.assignId(savedProductId);

            CartItem cartItem = new CartItem(member, product);
            cartItemRepository.save(cartItem);

            assertThatThrownBy(() -> productRepository.deleteProduct(product.getId()))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        @DisplayName("상품을 삭제한다.")
        void deleteProduct() {
            Product product = new Product("치킨", 10000, "http://chicken.com");
            Long savedProductId = productRepository.createProduct(product);

            productRepository.deleteProduct(savedProductId);

            List<Product> result = productRepository.getAllProducts();
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getProductById 메서드는 ")
    class GetProductById {

        @Test
        @DisplayName("ID의 상품이 존재하지 않으면 예외를 던진다.")
        void emptyProduct() {
            assertThatThrownBy(() -> productRepository.getProductById(-1L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("해당 상품이 존재하지 않습니다. 요청 상품 ID: " + -1);
        }

        @Test
        @DisplayName("ID의 상품이 존재하면 상품을 반환한다.")
        void getProduct() {
            Product product = new Product("치킨", 10000, "http://chicken.com");
            Long savedProductId = productRepository.createProduct(product);
            product.assignId(savedProductId);

            Product result = productRepository.getProductById(savedProductId);

            assertThat(result).usingRecursiveComparison().isEqualTo(product);
        }
    }
}

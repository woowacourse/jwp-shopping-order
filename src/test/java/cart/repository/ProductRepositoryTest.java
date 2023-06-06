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
    @DisplayName("findAll 메서드는 모든 상품을 조회한다.")
    void findAll() {
        Product productA = new Product("치킨", 10000, "http://chicken.com");
        Product productB = new Product("샐러드", 20000, "http://salad.com");
        Product productC = new Product("피자", 13000, "http://pizza.com");
        Long productIdA = productRepository.save(productA);
        Long productIdB = productRepository.save(productB);
        Long productIdC = productRepository.save(productC);
        productA.assignId(productIdA);
        productB.assignId(productIdB);
        productC.assignId(productIdC);

        List<Product> result = productRepository.findAll();

        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(productA),
                () -> assertThat(result.get(1)).usingRecursiveComparison().isEqualTo(productB),
                () -> assertThat(result.get(2)).usingRecursiveComparison().isEqualTo(productC)
        );
    }

    @Test
    @DisplayName("update 메서드는 상품 정보를 수정한다.")
    void update() {
        Product product = new Product("치킨", 10000, "http://chicken.com");
        Long savedProductId = productRepository.save(product);
        Product updateProduct = new Product(savedProductId, "피자", 13000, "http://pizza.com");

        productRepository.update(updateProduct);

        Product result = productRepository.findById(savedProductId);
        assertThat(result).usingRecursiveComparison().isEqualTo(updateProduct);
    }

    @Nested
    @DisplayName("delete 메서드는 ")
    class Delete {

        @Test
        @DisplayName("장바구니에 담겨 있는 상품이라면 예외를 던진다.")
        void existCartItemProduct() {
            MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 10);
            Long savedMemberId = memberDao.save(memberEntity);
            Member member = MemberMapper.toDomain(memberEntity);
            member.assignId(savedMemberId);

            Product product = new Product("치킨", 10000, "http://chicken.com");
            Long savedProductId = productRepository.save(product);
            product.assignId(savedProductId);

            CartItem cartItem = new CartItem(member, product);
            cartItemRepository.save(cartItem);

            assertThatThrownBy(() -> productRepository.delete(product.getId()))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        @DisplayName("상품을 삭제한다.")
        void deleteProduct() {
            Product product = new Product("치킨", 10000, "http://chicken.com");
            Long savedProductId = productRepository.save(product);

            productRepository.delete(savedProductId);

            List<Product> result = productRepository.findAll();
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findById 메서드는 ")
    class FindById {

        @Test
        @DisplayName("ID의 상품이 존재하지 않으면 예외를 던진다.")
        void emptyProduct() {
            assertThatThrownBy(() -> productRepository.findById(-1L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("해당 상품이 존재하지 않습니다. 요청 상품 ID: " + -1);
        }

        @Test
        @DisplayName("ID의 상품이 존재하면 상품을 반환한다.")
        void getProduct() {
            Product product = new Product("치킨", 10000, "http://chicken.com");
            Long savedProductId = productRepository.save(product);
            product.assignId(savedProductId);

            Product result = productRepository.findById(savedProductId);

            assertThat(result).usingRecursiveComparison().isEqualTo(product);
        }
    }
}

package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.CartItemException;
import cart.repository.mapper.MemberMapper;
import cart.test.RepositoryTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class CartItemRepositoryTest {

    private Member member;
    private Product product;
    private CartItem cartItem;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 10);
        Long memberId = memberDao.addMember(memberEntity);
        member = MemberMapper.toDomain(memberEntity);
        member.assignId(memberId);

        product = new Product("치킨", 10000, "http://chicken.com");
        Long productId = productRepository.createProduct(product);
        product.assignId(productId);

        cartItem = new CartItem(member, product);
        Long cartItemId = cartItemRepository.save(cartItem);
        cartItem.assignId(cartItemId);
    }

    @Test
    @DisplayName("findByMemberId 메서드는 멤버의 장바구니 상품 목록을 조회한다.")
    void findByMemberId() {
        Product otherProduct = new Product("피자", 20000, "http://pizza.com");
        Long otherProductId = productRepository.createProduct(otherProduct);
        otherProduct.assignId(otherProductId);

        MemberEntity otherMember = new MemberEntity("b@b.com", "password2", 20);
        Long otherMemberId = memberDao.addMember(otherMember);
        otherMember = otherMember.assignId(otherMemberId);

        CartItem myCartItem = new CartItem(member, otherProduct);
        Long myCartItemId = cartItemRepository.save(myCartItem);
        myCartItem.assignId(myCartItemId);
        CartItem otherCartItem = new CartItem(MemberMapper.toDomain(otherMember), otherProduct);
        cartItemRepository.save(otherCartItem);

        List<CartItem> result = cartItemRepository.findByMemberId(member.getId());

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0)).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(cartItem),
                () -> assertThat(result.get(1)).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(myCartItem)
        );
    }

    @Test
    @DisplayName("save 메서드는 장바구니 상품을 저장한다.")
    void save() {
        Product newProduct = new Product("피자", 20000, "http://pizza.com");
        Long newProductId = productRepository.createProduct(newProduct);
        newProduct.assignId(newProductId);
        CartItem newCartItem = new CartItem(member, newProduct);

        Long newCartItemId = cartItemRepository.save(newCartItem);

        newCartItem.assignId(newCartItemId);
        CartItem result = cartItemRepository.findById(newCartItemId);
        assertThat(result).usingRecursiveComparison().isEqualTo(newCartItem);
    }

    @Test
    @DisplayName("updateQuantity 메서드는 장바구니 상품 수량을 수정한다.")
    void updateQuantity() {
        CartItem updateCartItem = cartItem.changeQuantity(5);

        cartItemRepository.updateQuantity(updateCartItem);

        CartItem result = cartItemRepository.findById(cartItem.getId());
        assertThat(result).usingRecursiveComparison().isEqualTo(updateCartItem);
    }

    @Test
    @DisplayName("deleteAll 메서드는 장바구니 상품 목록을 삭제한다.")
    void deleteAll() {
        CartItem newCartItem = new CartItem(member, product);
        Long newCartItemId = cartItemRepository.save(newCartItem);
        newCartItem.assignId(newCartItemId);

        cartItemRepository.deleteAll(List.of(cartItem, newCartItem));

        List<CartItem> result = cartItemRepository.findByMemberId(member.getId());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("deleteAllByProductId 메서드는 상품 ID에 해당하는 모든 장바구니 상품을 삭제한다.")
    void deleteAllByProductId() {
        MemberEntity newMemberEntity = new MemberEntity("b@b.com", "password2", 50);
        Long newMemberId = memberDao.addMember(newMemberEntity);
        Member newMember = MemberMapper.toDomain(newMemberEntity);
        newMember.assignId(newMemberId);

        CartItem newCartItem = new CartItem(newMember, product);
        cartItemRepository.save(newCartItem);

        cartItemRepository.deleteAllByProductId(product.getId());

        List<CartItem> cartItemA = cartItemRepository.findByMemberId(member.getId());
        List<CartItem> cartItemB = cartItemRepository.findByMemberId(newMember.getId());
        assertAll(
                () -> assertThat(cartItemA).isEmpty(),
                () -> assertThat(cartItemB).isEmpty()
        );
    }

    @Test
    @DisplayName("deleteById 메서드는 장바구니 상품을 삭제한다.")
    void deleteById() {
        cartItemRepository.deleteById(cartItem.getId());

        List<CartItem> result = cartItemRepository.findByMemberId(member.getId());
        assertThat(result).isEmpty();
    }

    @Nested
    @DisplayName("findAllInIds 메서드는 ")
    class FindAllInIds {

        @Test
        @DisplayName("중복되는 ID가 존재하면 예외를 던진다.")
        void duplicateId() {
            assertThatThrownBy(() -> cartItemRepository.findAllInIds(List.of(cartItem.getId(), cartItem.getId())))
                    .isInstanceOf(CartItemException.class)
                    .hasMessage("중복된 장바구니 상품 ID가 존재합니다.");
        }

        @Test
        @DisplayName("장바구니 상품에 존재하지 않는 ID가 있으면 예외를 던진다.")
        void notExistId() {
            assertThatThrownBy(() -> cartItemRepository.findAllInIds(List.of(cartItem.getId(), -1L)))
                    .isInstanceOf(CartItemException.class)
                    .hasMessage("해당 장바구니 상품이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("ID 목록에 해당하는 모든 장바구니 상품을 조회한다.")
        void findAll() {
            CartItem newCartItem = new CartItem(member, product);
            Long newCartItemId = cartItemRepository.save(newCartItem);
            newCartItem.assignId(newCartItemId);

            List<CartItem> result = cartItemRepository.findAllInIds(List.of(cartItem.getId(), newCartItemId));

            assertAll(
                    () -> assertThat(result).hasSize(2),
                    () -> assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(cartItem),
                    () -> assertThat(result.get(1)).usingRecursiveComparison().isEqualTo(newCartItem)
            );
        }
    }

    @Nested
    @DisplayName("findByMemberIdAndProductId 메서드는 ")
    class FindByMemberIdAndProductId {

        @Test
        @DisplayName("멤버 ID는 일치하지 않고, 상품 ID는 일치하지 않으면 빈 값을 반환한다.")
        void notMatchMemberAndProduct() {
            Optional<CartItem> result = cartItemRepository.findByMemberIdAndProductId(-1L, -1L);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("멤버 ID는 일치하지 않고, 상품 ID는 일치한다면 빈 값을 반환한다.")
        void notMatchMember() {
            Optional<CartItem> result = cartItemRepository.findByMemberIdAndProductId(-1L, product.getId());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("멤버 ID는 일치하고, 상품 ID는 일치하지 않으면 빈 값을 반환한다.")
        void notMatchProduct() {
            Optional<CartItem> result = cartItemRepository.findByMemberIdAndProductId(member.getId(), -1L);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("멤버 ID, 상품 ID가 일치한다면 장바구니 상품을 조회한다.")
        void matchMemberAndProduct() {
            Optional<CartItem> result = cartItemRepository.findByMemberIdAndProductId(member.getId(), product.getId());

            assertAll(
                    () -> assertThat(result).isNotEmpty(),
                    () -> assertThat(result.get()).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(cartItem)
            );
        }
    }

    @Nested
    @DisplayName("findById 메서드는 ")
    class FindById {

        @Test
        @DisplayName("ID에 해당하는 장바구니 상품이 존재하지 않으면 예외를 던진다.")
        void notExistId() {
            assertThatThrownBy(() -> cartItemRepository.findById(-1L))
                    .isInstanceOf(CartItemException.class)
                    .hasMessage("해당 장바구니 상품이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("ID에 해당하는 장바구니 상품이 존재하면 장바구니 상품을 조회한다.")
        void findById() {
            CartItem result = cartItemRepository.findById(cartItem.getId());

            assertThat(result).usingRecursiveComparison().isEqualTo(cartItem);
        }
    }
}

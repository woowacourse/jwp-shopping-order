package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.exception.badrequest.BadRequestException;
import cart.repository.CartItemRepository;
import cart.repository.ProductRepository;
import cart.repository.mapper.MemberMapper;
import cart.test.ServiceTest;
import cart.ui.controller.dto.request.CartItemQuantityUpdateRequest;
import cart.ui.controller.dto.request.CartItemRemoveRequest;
import cart.ui.controller.dto.request.CartItemRequest;
import cart.ui.controller.dto.response.CartItemPriceResponse;
import cart.ui.controller.dto.response.CartItemResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class CartItemServiceTest {

    private Member member;
    private Product product;
    private CartItem cartItem;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        MemberEntity memberEntity = new MemberEntity("a@a.com", "password1", 10);
        Long memberId = memberDao.addMember(memberEntity);
        member = MemberMapper.toDomain(memberEntity);
        member.assignId(memberId);

        product = new Product("치킨", 13000, "http://chicken.com");
        Long productId = productRepository.createProduct(product);
        product.assignId(productId);

        cartItem = new CartItem(member, product);
        Long cartItemId = cartItemRepository.save(cartItem);
        cartItem.assignId(cartItemId);
    }

    @Test
    @DisplayName("findByMember 메서드는 멤버의 장바구니 상품 목록을 조회한다.")
    void findByMember() {
        Product newProduct = new Product("피자", 20000, "http://pizza.com");
        Long newProductId = productRepository.createProduct(newProduct);
        newProduct.assignId(newProductId);

        CartItem newCartItem = new CartItem(member, newProduct);
        Long newCartItemId = cartItemRepository.save(newCartItem);
        newCartItem.assignId(newCartItemId);

        List<CartItemResponse> result = cartItemService.findByMember(member);

        assertThat(result).usingRecursiveComparison().isEqualTo(
                List.of(
                        CartItemResponse.from(cartItem),
                        CartItemResponse.from(newCartItem)
                )
        );
    }

    @Nested
    @DisplayName("getTotalPriceWithDeliveryFee 메서드는 ")
    class GetTotalPriceWithDeliveryFee {

        @Test
        @DisplayName("멤버 장바구니 상품이 아니라면 예외를 던진다.")
        void notOwner() {
            MemberEntity otherMemberEntity = new MemberEntity("b@b.com", "password2", 10);
            Long otherMemberId = memberDao.addMember(otherMemberEntity);
            Member otherMember = MemberMapper.toDomain(otherMemberEntity);
            otherMember.assignId(otherMemberId);

            assertThatThrownBy(() -> cartItemService.getTotalPriceWithDeliveryFee(otherMember, List.of(cartItem.getId())))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("장바구니 상품을 관리할 수 있는 멤버가 아닙니다.");
        }

        @ParameterizedTest
        @CsvSource(value = {"2,39000,3000", "3,52000,0"})
        @DisplayName("장바구니 상품 목록의 총 가격과 배송료를 응답한다.")
        void getTotalPriceWithDeliveryFee(int quantity, int totalPrice, int deliveryFee) {
            CartItem newCartItem = new CartItem(quantity, member, product);
            Long newCartItemId = cartItemRepository.save(newCartItem);

            CartItemPriceResponse result =
                    cartItemService.getTotalPriceWithDeliveryFee(member, List.of(cartItem.getId(), newCartItemId));

            assertAll(
                    () -> assertThat(result.getTotalPrice()).isEqualTo(totalPrice),
                    () -> assertThat(result.getDeliveryFee()).isEqualTo(deliveryFee)
            );
        }
    }

    @Nested
    @DisplayName("add 메서드는 ")
    class Add {

        @Test
        @DisplayName("장바구니에 이미 존재하는 상품이라면 수량이 1개 증가한다.")
        void existCartItem() {
            Long cartItemId = cartItemService.add(member, new CartItemRequest(product.getId()));

            CartItem result = cartItemRepository.findById(cartItemId);
            assertThat(result.getQuantity()).isEqualTo(2);
        }

        @Test
        @DisplayName("장바구니에 존재하지 않는 상품이라면 수량이 1개로 새로 추가된다.")
        void newCartItem() {
            Product newProduct = new Product("피자", 20000, "http://pizza.com");
            Long newProductId = productRepository.createProduct(newProduct);

            Long cartItemId = cartItemService.add(member, new CartItemRequest(newProductId));

            CartItem result = cartItemRepository.findById(cartItemId);
            assertThat(result.getQuantity()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("updateQuantity 메서드는 ")
    class UpdateQuantity {

        @Test
        @DisplayName("장바구니 상품에 대한 권한이 있는 멤버가 아니라면 예외를 던진다.")
        void notOwner() {
            MemberEntity newMemberEntity = new MemberEntity("b@b.com", "password2", 0);
            Long newMemberId = memberDao.addMember(newMemberEntity);
            Member newMember = MemberMapper.toDomain(newMemberEntity);
            newMember.assignId(newMemberId);

            assertThatThrownBy(
                    () -> cartItemService.updateQuantity(newMember, cartItem.getId(), new CartItemQuantityUpdateRequest(10)))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("장바구니 상품을 관리할 수 있는 멤버가 아닙니다.");
        }

        @Test
        @DisplayName("업데이트 수량 개수가 0이라면 장바구니 상품을 삭제한다.")
        void zeroQuantity() {
            cartItemService.updateQuantity(member, cartItem.getId(), new CartItemQuantityUpdateRequest(0));

            List<CartItemResponse> result = cartItemService.findByMember(member);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("업데이트 수량 정보가 0보다 작은 경우 예외를 던진다.")
        void negativeQuantity() {
            assertThatThrownBy(
                    () -> cartItemService.updateQuantity(member, cartItem.getId(), new CartItemQuantityUpdateRequest(-1)))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("장바구니 상품 수량은 최소 1개부터 가능합니다. 현재 개수: " + -1);
        }

        @Test
        @DisplayName("업데이트 수량 정보가 유효하다면 장바구니 상품 수량을 수정한다.")
        void validQuantity() {
            cartItemService.updateQuantity(member, cartItem.getId(), new CartItemQuantityUpdateRequest(100));

            CartItem result = cartItemRepository.findById(cartItem.getId());
            assertThat(result.getQuantity()).isEqualTo(100);
        }
    }

    @Nested
    @DisplayName("removeCartItems 메서드는 ")
    class RemoveCartItems {

        @Test
        @DisplayName("멤버 장바구니 상품이 아니라면 예외를 던진다.")
        void notOwner() {
            MemberEntity newMemberEntity = new MemberEntity("b@b.com", "password2", 0);
            Long newMemberId = memberDao.addMember(newMemberEntity);
            Member newMember = MemberMapper.toDomain(newMemberEntity);
            newMember.assignId(newMemberId);

            assertThatThrownBy(
                    () -> cartItemService.removeCartItems(newMember, new CartItemRemoveRequest(List.of(cartItem.getId()))))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("장바구니 상품을 관리할 수 있는 멤버가 아닙니다.");
        }

        @Test
        @DisplayName("장바구니 상품 목록을 삭제한다.")
        void removeCartItems() {
            CartItem newCartItem = new CartItem(member, product);
            Long newCartItemId = cartItemRepository.save(newCartItem);

            cartItemService.removeCartItems(member, new CartItemRemoveRequest(List.of(cartItem.getId(), newCartItemId)));

            List<CartItemResponse> result = cartItemService.findByMember(member);
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("remove 메서드는 ")
    class Remove {

        @Test
        @DisplayName("장바구니 상품에 대한 권한이 있는 멤버가 아니라면 예외를 던진다.")
        void notOwner() {
            MemberEntity newMemberEntity = new MemberEntity("b@b.com", "password2", 0);
            Long newMemberId = memberDao.addMember(newMemberEntity);
            Member newMember = MemberMapper.toDomain(newMemberEntity);
            newMember.assignId(newMemberId);

            assertThatThrownBy(() -> cartItemService.remove(newMember, cartItem.getId()))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("장바구니 상품을 관리할 수 있는 멤버가 아닙니다.");
        }

        @Test
        @DisplayName("장바구니 상품에 대한 권한이 있는 멤버라면 장바구니 상품을 삭제한다.")
        void remove() {
            cartItemService.remove(member, cartItem.getId());

            List<CartItemResponse> result = cartItemService.findByMember(member);
            assertThat(result).isEmpty();
        }
    }
}

package cart.order.domain;

import static cart.order.exception.OrderExceptionType.MISMATCH_PRODUCT;
import static cart.order.exception.OrderExceptionType.NO_AUTHORITY_ORDER_ITEM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cart.cartitem.domain.CartItem;
import cart.common.execption.BaseExceptionType;
import cart.member.domain.Member;
import cart.order.exception.OrderException;
import cart.product.domain.Product;
import cart.product.domain.ProductRepository;
import cart.product.fixture.FakeProductRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderValidator 은(는)")
class OrderValidatorTest {

    private final ProductRepository productRepository = new FakeProductRepository();
    private final OrderValidator orderValidator = new OrderValidator(productRepository);

    @Test
    void 상품_이름이_변경되면_오류() {
        // given
        Member member = new Member(1L, "email", "1234");
        Product origin = new Product("말랑", 10000, "image");
        Long id = productRepository.save(origin);
        CartItem cartItem = new CartItem(productRepository.findById(id), member);

        Product updated = new Product(id, "코코닥", 10000, "image");
        productRepository.update(updated);

        // when
        BaseExceptionType baseExceptionType = assertThrows(OrderException.class, () ->
                orderValidator.validate(1L, List.of(cartItem))
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(MISMATCH_PRODUCT);
    }

    @Test
    void 상품_가격이_변경되면_오류() {
        // given
        Member member = new Member(1L, "email", "1234");
        Product origin = new Product("말랑", 10000, "image");
        Long id = productRepository.save(origin);
        CartItem cartItem = new CartItem(productRepository.findById(id), member);

        Product updated = new Product(id, "말랑", 1000, "image");
        productRepository.update(updated);

        // when
        BaseExceptionType baseExceptionType = assertThrows(OrderException.class, () ->
                orderValidator.validate(1L, List.of(cartItem))
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(MISMATCH_PRODUCT);
    }

    @Test
    void 상품_이미지가_변경되면_오류() {
        // given
        Member member = new Member(1L, "email", "1234");
        Product origin = new Product("말랑", 10000, "image1");
        Long id = productRepository.save(origin);
        CartItem cartItem = new CartItem(productRepository.findById(id), member);

        Product updated = new Product(id, "말랑", 10000, "image2");
        productRepository.update(updated);

        // when
        BaseExceptionType baseExceptionType = assertThrows(OrderException.class, () ->
                orderValidator.validate(1L, List.of(cartItem))
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(MISMATCH_PRODUCT);
    }

    @Test
    void 자신의_장바구니에_들어있지_않은_상품의_경우_예외() {
        // given
        Member member = new Member(1L, "email", "1234");
        Member member2 = new Member(2L, "email2", "1234");
        Product origin = new Product("말랑", 10000, "image1");
        Long id = productRepository.save(origin);
        CartItem cartItem = new CartItem(productRepository.findById(id), member);

        // when
        BaseExceptionType baseExceptionType = assertThrows(OrderException.class, () ->
                orderValidator.validate(member2.getId(), List.of(cartItem))
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(NO_AUTHORITY_ORDER_ITEM);
    }

    @Test
    void 상품이_변경되지_않았으면_정상() {
        // given
        Member member = new Member(1L, "email", "1234");
        Product origin = new Product("말랑", 10000, "image");
        Long id = productRepository.save(origin);
        CartItem cartItem = new CartItem(productRepository.findById(id), member);

        Product updated = new Product(id, "말랑", 10000, "image");
        productRepository.update(updated);

        // when & then
        assertDoesNotThrow(() ->
                orderValidator.validate(1L, List.of(cartItem)));
    }
}

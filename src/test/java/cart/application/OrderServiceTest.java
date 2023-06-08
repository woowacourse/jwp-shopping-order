package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderProduct;
import cart.domain.Product;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.ErrorMessage;
import cart.exception.OrderException;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Sql("/truncate.sql")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    void 주문을_저장한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 20000));
        Product 상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        CartItem 장바구니_상품 = 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(new CartItem(null, 10, 상품, 멤버));
        OrderRequest 요청 = new OrderRequest(List.of(장바구니_상품.getId()), 2000);

        // when
        Long 저장된_주문_ID = orderService.save(멤버, 요청);

        // then
        Order 저장된_주문 = orderRepository.findById(저장된_주문_ID, 멤버);
        Member 저장된_멤버 = memberRepository.findById(멤버.getId());
        assertAll(
                () -> assertThat(저장된_주문.getMember()).isEqualTo(멤버),
                () -> assertThat(저장된_주문.getDeliveryFee()).isEqualTo(0),
                () -> assertThat(저장된_주문.getTotalPrice()).isEqualTo(100000),
                () -> assertThat(저장된_주문.getOrderProducts()).extracting(OrderProduct::getProduct)
                        .contains(상품),
                () -> assertThat(저장된_멤버.getPoint()).isEqualTo(20000 - 2000 + (100000 / 10))
        );
    }

    private Member 멤버를_저장하고_ID가_있는_멤버를_리턴한다(Member 멤버) {
        Long 저장된_멤버_ID = memberRepository.save(멤버);

        return new Member(저장된_멤버_ID, 멤버.getEmail(), 멤버.getPassword(), 멤버.getPoint());
    }

    private Product 상품을_저장하고_ID가_있는_상품을_리턴한다(Product 상품) {
        Long 저장된_상품_ID = productRepository.save(상품);

        return new Product(저장된_상품_ID, 상품.getName(), 상품.getPrice(), 상품.getImageUrl());
    }

    private CartItem 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(CartItem 장바구니_상품) {
        Long 저장된_장바구니_ID = cartItemRepository.save(장바구니_상품);

        return new CartItem(저장된_장바구니_ID, 장바구니_상품.getQuantity(), 장바구니_상품.getProduct(), 장바구니_상품.getMember());
    }

    @Test
    void 다른_멤버의_장바구니_상품으로_주문하면_예외를_반환한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 20000));
        Member 다른_멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("other@com", "다른비밀번호", 200000));
        Product 상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        CartItem 장바구니_상품 = 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(new CartItem(null, 10, 상품, 멤버));
        OrderRequest 요청 = new OrderRequest(List.of(장바구니_상품.getId()), 2000);

        // then
        assertThatThrownBy(() -> orderService.save(다른_멤버, 요청))
                .isInstanceOf(OrderException.class)
                .hasMessage(ErrorMessage.INVALID_CART_ITEM_OWNER.getMessage());
    }

    @Test
    void 주문에서_사용할_포인트가_멤버의_포인트보다_많으면_예외를_반환한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 100));
        Product 상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        CartItem 장바구니_상품 = 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(new CartItem(null, 10, 상품, 멤버));
        OrderRequest 요청 = new OrderRequest(List.of(장바구니_상품.getId()), 2000);

        // then
        assertThatThrownBy(() -> orderService.save(멤버, 요청))
                .isInstanceOf(OrderException.class)
                .hasMessage(ErrorMessage.INVALID_MEMBER_POINT_LESS_THAN_USED_POINT.getMessage());
    }

    @Test
    void 주문에서_사용할_포인트가_상품_총_가격보다_많으면_예외를_반환한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 3000000));
        Product 상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        CartItem 장바구니_상품 = 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(new CartItem(null, 10, 상품, 멤버));
        OrderRequest 요청 = new OrderRequest(List.of(장바구니_상품.getId()), 200000);

        // then
        assertThatThrownBy(() -> orderService.save(멤버, 요청))
                .isInstanceOf(OrderException.class)
                .hasMessage(ErrorMessage.INVALID_POINT_MORE_THAN_PRICE.getMessage());
    }

    @Test
    void 멤버의_모든_주문_내역을_조회한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID가_있는_멤버를_리턴한다(new Member("vero@com", "비밀번호", 20000));
        Product 상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("치킨", 10000, "이미지"));
        Product 두번째_상품 = 상품을_저장하고_ID가_있는_상품을_리턴한다(new Product("피자", 20000, "피자이미지"));

        CartItem 장바구니_상품 = 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(new CartItem(null, 10, 상품, 멤버));

        OrderRequest 요청 = new OrderRequest(List.of(장바구니_상품.getId()), 2000);
        Long 저장된_주문_ID = orderService.save(멤버, 요청);

        CartItem 두번째_주문_장바구니_상품 = 장바구니_상품을_저장하고_ID가_있는_장바구니_상품을_리턴한다(new CartItem(null, 800, 두번째_상품, 멤버));
        OrderRequest 두번째_요청 = new OrderRequest(List.of(두번째_주문_장바구니_상품.getId()), 2000);
        Long 저장된_두번째_주문_ID = orderService.save(멤버, 두번째_요청);

        // when
        List<OrderResponse> 응답 = orderService.findByMember(멤버);

        // then
        assertThat(응답).hasSize(2);
        assertThat(응답).extracting(OrderResponse::getOrderId).contains(저장된_주문_ID, 저장된_두번째_주문_ID);
    }
}

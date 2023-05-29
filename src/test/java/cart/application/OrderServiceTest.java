package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.orderproduct.Order;
import cart.domain.orderproduct.OrderProduct;
import cart.domain.product.Product;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import cart.dto.OrderRequest;
import cart.exception.MemberNotFoundException;
import cart.exception.OrderNotFoundException;
import cart.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderProductDao orderProductDao;

    @Nested
    @DisplayName("상품을 주문할 때에는")
    class DescribeOrderMethodTest1 {
        private final Member member = memberDao.findById(1L).orElseThrow(MemberNotFoundException::new);
        private final Product product1 = productDao.findById(1L).orElseThrow(ProductNotFoundException::new);
        private final Product product2 = productDao.findById(2L).orElseThrow(ProductNotFoundException::new);

        @Nested
        @DisplayName("만약 총 주문 가격이 5이상 초과이라면")
        class ContextWithDiscountDeliveryFee {
            private final int quantity1 = 1;
            private final int quantity2 = 2;
            private final int usedPoint = 1000;

            @BeforeEach
            void setUp() {
                final Long cartItemId1 = cartItemDao.insert(new CartItem(member, product1, quantity1));
                final Long cartItemId2 = cartItemDao.insert(new CartItem(member, product2, quantity2));
                final OrderRequest orderRequest = new OrderRequest(List.of(cartItemId1, cartItemId2), usedPoint);
                final Long orderId = orderService.order(member, orderRequest);
            }

            @DisplayName("배송비 3천원을 할인해준다.")
            @Test
            void it_returns_discounted_delivery_fee() {
                final Member updatedMember = memberDao.findById(1L).orElseThrow(MemberNotFoundException::new);

                int remainPoint = member.getPointValue() - usedPoint;
                final int cartItemPrice1 = product1.getPriceValue() * quantity1;
                final int cartItemPrice2 = product2.getPriceValue() * quantity2;
                remainPoint += (cartItemPrice1 + cartItemPrice2 - CartItems.SHIPPING_FEE) * 0.1;

                assertThat(updatedMember.getPoint()).isEqualTo(new MemberPoint(remainPoint));
            }
        }

        @Nested
        @DisplayName("만약 총 주문 가격이 5만원 미만이라면")
        class ContextWithNonDiscountDeliveryFee {
            private final int quantity1 = 1;
            private final int quantity2 = 1;
            private final int usedPoint = 1000;

            @BeforeEach
            void setUp() {
                final Long cartItemId1 = cartItemDao.insert(new CartItem(member, product1, quantity1));
                final Long cartItemId2 = cartItemDao.insert(new CartItem(member, product2, quantity2));
                final OrderRequest orderRequest = new OrderRequest(List.of(cartItemId1, cartItemId2), usedPoint);
                orderService.order(member, orderRequest);
            }

            @DisplayName("배송비 3천원을 할인해주지 않는다.")
            @Test
            void it_returns_discounted_delivery_fee() {
                final Member updatedMember = memberDao.findById(1L).orElseThrow(MemberNotFoundException::new);

                int remainPoint = member.getPointValue() - usedPoint;
                final int cartItemPrice1 = product1.getPriceValue() * quantity1;
                final int cartItemPrice2 = product2.getPriceValue() * quantity2;
                remainPoint += (cartItemPrice1 + cartItemPrice2) * 0.1;

                assertThat(updatedMember.getPoint()).isEqualTo(new MemberPoint(remainPoint));
            }
        }

        @Nested
        @DisplayName("주문이 완료되면 ")
        class ContextWithOrderProductTest {
            private Order order;

            @BeforeEach
            void setUp() {
                final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L), 1000);
                final Long orderId = orderService.order(member, orderRequest);
                order = orderDao.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
            }

            @DisplayName("주문 내역을 볼 수 있다.")
            @Test
            void it_returns_orderProducts() {
                final List<OrderProduct> orderProducts = orderProductDao.findAllByMemberId(member.getId());

                assertAll(
                        () -> assertThat(orderProducts).hasSize(2),

                        () -> assertThat(orderProducts.get(0).getOrder()).isEqualTo(order),
                        () -> assertThat(orderProducts.get(0).getProduct()).isEqualTo(product1),
                        () -> assertThat(orderProducts.get(0).getProductName()).isEqualTo(new ProductName("치킨")),
                        () -> assertThat(orderProducts.get(0).getProductPrice()).isEqualTo(new ProductPrice(10000)),
                        () -> assertThat(orderProducts.get(0).getProductImageUrlValue()).startsWith("https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec"),
                        () -> assertThat(orderProducts.get(0).getQuantity()).isEqualTo(new Quantity(2)),

                        () -> assertThat(orderProducts.get(1).getOrder()).isEqualTo(order),
                        () -> assertThat(orderProducts.get(1).getProduct()).isEqualTo(product2),
                        () -> assertThat(orderProducts.get(1).getProductName()).isEqualTo(new ProductName("샐러드")),
                        () -> assertThat(orderProducts.get(1).getProductPrice()).isEqualTo(new ProductPrice(20000)),
                        () -> assertThat(orderProducts.get(1).getProductImageUrlValue()).startsWith("https://images.unsplash.com/photo-1512621776951-a57141f2eefd"),
                        () -> assertThat(orderProducts.get(1).getQuantity()).isEqualTo(new Quantity(4))
                );
            }

            @DisplayName("장바구니에 있었던 상품은 제거된다.")
            @Test
            void cart_items_are_deleted() {
                // given
                final List<CartItem> cartItems = cartItemDao.findAllByMemberId(member.getId());

                // when, then
                assertThat(cartItems).doesNotContain(
                        new CartItem(1L, null, null, 0),
                        new CartItem(2L, null, null, 0)
                );
            }
        }
    }
}

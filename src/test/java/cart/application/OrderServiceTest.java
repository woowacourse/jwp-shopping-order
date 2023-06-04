package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.Quantity;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderProduct;
import cart.domain.product.Product;
import cart.domain.product.ProductName;
import cart.domain.product.ProductPrice;
import cart.exception.business.point.InvalidPointUseException;
import cart.exception.notfound.MemberNotFoundException;
import cart.exception.notfound.OrderNotFoundException;
import cart.exception.notfound.ProductNotFoundException;
import cart.ui.dto.order.OrderDetailResponse;
import cart.ui.dto.order.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
            private final Quantity quantity1 = new Quantity(1);
            private final Quantity quantity2 = new Quantity(2);
            private final int usedPoint = 1000;
            private Long orderId;

            @BeforeEach
            void setUp() {
                final Long cartItemId1 = cartItemDao.insert(new CartItem(member, product1, quantity1));
                final Long cartItemId2 = cartItemDao.insert(new CartItem(member, product2, quantity2));
                final OrderRequest orderRequest = new OrderRequest(List.of(cartItemId1, cartItemId2), usedPoint);
                orderId = orderService.order(member, orderRequest);
            }

            @DisplayName("배송비 3천원을 추가하지 않는다.")
            @Test
            void it_returns_discounted_delivery_fee() {
                final Member updatedMember = memberDao.findById(1L).orElseThrow(MemberNotFoundException::new);
                final Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);

                int remainPoint = member.getPointValue() - usedPoint;
                final int cartItemPrice1 = product1.getPriceValue() * quantity1.getQuantity();
                final int cartItemPrice2 = product2.getPriceValue() * quantity2.getQuantity();
                final int updatedPoint = (int) (remainPoint + (cartItemPrice1 + cartItemPrice2) * 0.1);

                assertAll(
                        () -> assertThat(updatedMember.getPoint()).isEqualTo(new MemberPoint(updatedPoint)),
                        () -> assertThat(order.getDeliveryFee()).isEqualTo(new DeliveryFee(0)),
                        () -> assertThat(order.getUsedPoint()).isEqualTo(new MemberPoint(1000))
                );
            }
        }

        @Nested
        @DisplayName("만약 총 주문 가격이 5만원 미만이라면")
        class ContextWithNonDiscountDeliveryFee {
            private final Quantity quantity1 = new Quantity(1);
            private final Quantity quantity2 = new Quantity(1);
            private final int usedPoint = 1000;
            private Long orderId;


            @BeforeEach
            void setUp() {
                final Long cartItemId1 = cartItemDao.insert(new CartItem(member, product1, quantity1));
                final Long cartItemId2 = cartItemDao.insert(new CartItem(member, product2, quantity2));
                final OrderRequest orderRequest = new OrderRequest(List.of(cartItemId1, cartItemId2), usedPoint);
                orderId = orderService.order(member, orderRequest);
            }

            @DisplayName("배송비 3천원 추가한다.")
            @Test
            void it_returns_discounted_delivery_fee() {
                final Member updatedMember = memberDao.findById(1L).orElseThrow(MemberNotFoundException::new);
                final Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);

                int remainPoint = member.getPointValue() - usedPoint;
                final int cartItemPrice1 = product1.getPriceValue() * quantity1.getQuantity();
                final int cartItemPrice2 = product2.getPriceValue() * quantity2.getQuantity();
                final int updatedPoint = (int) (remainPoint + (cartItemPrice1 + cartItemPrice2) * 0.1);
                assertAll(
                        () -> assertThat(updatedMember.getPoint()).isEqualTo(new MemberPoint(updatedPoint)),
                        () -> assertThat(order.getDeliveryFee()).isEqualTo(new DeliveryFee(3000)),
                        () -> assertThat(order.getUsedPoint()).isEqualTo(new MemberPoint(1000))
                );
            }
        }

        @Nested
        @DisplayName("주문이 완료되면 ")
        class ContextWithOrderProductTest1 {
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
                        () -> assertThat(orderProducts.get(0).getProductId()).isEqualTo(product1.getId()),
                        () -> assertThat(orderProducts.get(0).getProductName()).isEqualTo(new ProductName("치킨")),
                        () -> assertThat(orderProducts.get(0).getProductPrice()).isEqualTo(new ProductPrice(10000)),
                        () -> assertThat(orderProducts.get(0).getProductImageUrlValue()).startsWith("https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec"),
                        () -> assertThat(orderProducts.get(0).getQuantity()).isEqualTo(new Quantity(2)),

                        () -> assertThat(orderProducts.get(1).getOrder()).isEqualTo(order),
                        () -> assertThat(orderProducts.get(1).getProductId()).isEqualTo(product2.getId()),
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
                        new CartItem(1L, null, null, new Quantity(0)),
                        new CartItem(2L, null, null, new Quantity(0))
                );
            }
        }

        @Nested
        @DisplayName("상품의 총 가격이 ")
        class ContextWithOrderProductTest2 {

            @DisplayName("5만원 미만이라면 배송비를 포함한 가격까지 포인트를 사용할 수 있다.")
            @Test
            void pointTest1() {
                // given
                final OrderRequest orderRequest = new OrderRequest(List.of(6L), 3500);
                final Long orderId = orderService.order(member, orderRequest);

                // when
                final Order order = orderDao.findById(orderId).orElseThrow(OrderNotFoundException::new);

                // then
                assertAll(
                        () -> assertThat(order.getDeliveryFee()).isEqualTo(new DeliveryFee(3000)),
                        () -> assertThat(order.getUsedPoint()).isEqualTo(new MemberPoint(3500)),
                        () -> assertThat(order.getSavedPoint()).isEqualTo(new MemberPoint(50))
                );
            }

            @DisplayName("총 가격과 배송비를 포함한 가격을 초과하는 포인트는 사용할 수 없다.")
            @Test
            void pointTest2() {
                // given
                final OrderRequest orderRequest = new OrderRequest(List.of(6L), 3501);

                // when, then
                assertThatThrownBy(() -> orderService.order(member, orderRequest))
                        .isInstanceOf(InvalidPointUseException.class);
            }
        }

        @DisplayName("주문 id의 리스트로 해당 주문들을 삭제할 수 있다.")
        @Test
        void deleteByIds() {
            // given
            final OrderRequest orderRequest1 = new OrderRequest(List.of(1L), 0);
            final OrderRequest orderRequest2 = new OrderRequest(List.of(2L), 0);
            final OrderRequest orderRequest3 = new OrderRequest(List.of(3L), 0);
            final Long orderId1 = orderService.order(member, orderRequest1);
            final Long orderId2 = orderService.order(member, orderRequest2);
            final Long orderId3 = orderService.order(member, orderRequest3);

            // when
            orderService.deleteByIds(member, List.of(orderId1, orderId2));
            final List<OrderDetailResponse> responses = orderService.getAllOrderDetails(member);

            // then
            assertAll(
                    () -> assertThat(responses).hasSize(1),
                    () -> assertThat(responses.get(0).getOrderId()).isEqualTo(orderId3)
            );
        }

        @DisplayName("사용자의 모든 주문을 삭제한다")
        @Test
        void asdf() {
            // given
            final OrderRequest orderRequest1 = new OrderRequest(List.of(1L), 0);
            final OrderRequest orderRequest2 = new OrderRequest(List.of(2L), 0);
            final OrderRequest orderRequest3 = new OrderRequest(List.of(3L), 0);
            final Long orderId1 = orderService.order(member, orderRequest1);
            final Long orderId2 = orderService.order(member, orderRequest2);
            final Long orderId3 = orderService.order(member, orderRequest3);

            // when
            orderService.deleteAll(member);
            final List<OrderDetailResponse> responses = orderService.getAllOrderDetails(member);

            // then
            assertThat(responses).hasSize(0);
        }
    }
}

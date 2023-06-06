package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import cart.dao.CartItemDao;
import cart.dao.CouponDao;
import cart.dao.OrderDao;
import cart.dao.ProductOrderDao;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import cart.domain.vo.Amount;
import cart.exception.BusinessException;
import cart.ui.dto.request.OrderProductRequest;
import cart.ui.dto.request.OrderRequest;
import cart.ui.dto.response.OrderListResponse;
import cart.ui.dto.response.OrderProductResponse;
import cart.ui.dto.response.OrderResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private final Member member = new Member(1L, "test@test.com", "password");
    private final Product product1 = new Product(1L, "product1", Amount.of(10_000), "imageUrl1");
    private final Product product2 = new Product(2L, "product2", Amount.of(20_000), "imageUrl2");
    private final CartItem cartItem1 = new CartItem(1L, 5, product1, member);
    private final CartItem cartItem2 = new CartItem(2L, 10, product2, member);
    private final Coupon coupon = new Coupon(1L, "name", Amount.of(1_000), Amount.of(10_000), false);
    private final List<OrderProductRequest> products = List.of(new OrderProductRequest(1L, 5),
        new OrderProductRequest(2L, 10));
    private final Order order = new Order(1L, new Products(List.of(product1, product2)),
        Amount.of(product1.getAmount().getValue() + product2.getAmount().getValue()), Amount.of(29_000),
        Amount.of(3_000), "address");

    @Mock
    private OrderDao orderDao;
    @Mock
    private CouponDao couponDao;
    @Mock
    private CartItemDao cartItemDao;
    @Mock
    private ProductOrderDao productOrderDao;
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("총 금액과 실제 상품 금액의 합이 같을 때 주문을 진행한다.")
    void testOrder() {
        //given
        final OrderRequest request = new OrderRequest(products, 250_000, 3_000, "address", 1L);

        given(cartItemDao.findByMemberId(anyLong()))
            .willReturn(new CartItems(List.of(cartItem1, cartItem2)));
        given(couponDao.findByCouponIdAndMemberId(anyLong(), anyLong()))
            .willReturn(Optional.of(coupon));
        given(orderDao.save(any(Order.class), anyLong()))
            .willReturn(order);

        //when
        final OrderResponse response = orderService.order(request, member);

        //then
        final OrderProductResponse orderProductResponse1 = new OrderProductResponse(product1.getId(),
            product1.getName(), product1.getAmount().getValue(), product1.getImageUrl(), 5);
        final OrderProductResponse orderProductResponse2 = new OrderProductResponse(product2.getId(),
            product2.getName(), product2.getAmount().getValue(), product2.getImageUrl(), 10);
        final OrderResponse expectedResponse = new OrderResponse(order.getId(), request.getTotalProductAmount(),
            order.getDiscountedAmount().getValue(), order.getDeliveryAmount().getValue(), order.getAddress(),
            List.of(orderProductResponse1, orderProductResponse2));

        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("총 금액과 실제 상품 금액의 합이 맞지 않을때 예외가 발생한다.")
    void testOrderWhenTotalProductAmountNotMatch() {
        //given
        final OrderRequest request = new OrderRequest(products, 25_000, 3_000, "address", 1L);
        given(cartItemDao.findByMemberId(anyLong()))
            .willReturn(new CartItems(List.of(cartItem1, cartItem2)));

        //when
        //then
        assertThatThrownBy(() -> orderService.order(request, member))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("쿠폰이 존재하지 않을 때 예외가 발생한다.")
    void testOrderWhenCouponNotExist() {
        //given
        final OrderRequest request = new OrderRequest(products, 250_000, 3_000, "address", 1L);
        given(cartItemDao.findByMemberId(anyLong()))
            .willReturn(new CartItems(List.of(cartItem1, cartItem2)));

        //when
        //then
        assertThatThrownBy(() -> orderService.order(request, member))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("쿠폰을 적용하지 않고 주문을 한다.")
    void testOrderWhenCouponNotUse() {
        //given
        final OrderRequest request = new OrderRequest(products, 250_000, 3_000, "address", null);
        final Order order = new Order(1L, new Products(List.of(product1, product2)),
            Amount.of(product1.getAmount().getValue() + product2.getAmount().getValue()),
            Amount.of(product1.getAmount().getValue() + product2.getAmount().getValue()),
            Amount.of(3_000), "address");
        given(cartItemDao.findByMemberId(anyLong()))
            .willReturn(new CartItems(List.of(cartItem1, cartItem2)));
        given(orderDao.save(any(Order.class), anyLong()))
            .willReturn(order);

        //when
        final OrderResponse response = orderService.order(request, member);

        //then
        final OrderProductResponse orderProductResponse1 = new OrderProductResponse(product1.getId(),
            product1.getName(), product1.getAmount().getValue(), product1.getImageUrl(), 5);
        final OrderProductResponse orderProductResponse2 = new OrderProductResponse(product2.getId(),
            product2.getName(), product2.getAmount().getValue(), product2.getImageUrl(), 10);
        final OrderResponse expectedResponse = new OrderResponse(order.getId(), request.getTotalProductAmount(),
            order.getDiscountedAmount().getValue(), order.getDeliveryAmount().getValue(), order.getAddress(),
            List.of(orderProductResponse1, orderProductResponse2));

        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void testFindOrder() {
        //given
        given(orderDao.findById(anyLong()))
            .willReturn(Optional.of(order));
        given(productOrderDao.count(anyLong(), anyLong()))
            .willReturn(3);

        //when
        final OrderResponse response = orderService.findOrder(1L);

        //then
        assertThat(response.getId()).isEqualTo(order.getId());
        assertThat(response.getAddress()).isEqualTo(order.getAddress());
        assertThat(response.getDeliveryAmount()).isEqualTo(order.getDeliveryAmount().getValue());
        assertThat(response.getTotalProductAmount()).isEqualTo(order.getTotalAmount().getValue());
        final List<OrderProductResponse> orderProductResponses = response.getProducts();
        assertThat(orderProductResponses.get(0).getId()).isEqualTo(product1.getId());
        assertThat(orderProductResponses.get(1).getId()).isEqualTo(product2.getId());
    }

    @Test
    @DisplayName("주문이 존재하지 않을 때, 조회한다.")
    void testFindOrderWhenOrderNotExists() {
        //given
        given(orderDao.findById(anyLong()))
            .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> orderService.findOrder(3L))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("회원 별 주문 목록 조회 구현")
    void findOrderByMember() {
        //given
        final Order order2 = new Order(2L, new Products(List.of(product1, product2)),
            Amount.of(product1.getAmount().getValue() + product2.getAmount().getValue()), Amount.of(29_000),
            Amount.of(3_000), "address");
        final List<Order> orders = List.of(order, order2);
        given(orderDao.findByMember(any(Member.class)))
            .willReturn(orders);

        //when
        final List<OrderListResponse> responses = orderService.findOrder(member);

        //then
        final List<OrderListResponse> expectedResponse = orders.stream()
            .map(order -> {
                final List<OrderProductResponse> orderProductResponses = makeOrderProductResponses(order);
                return new OrderListResponse(order.getId(), orderProductResponses);
            })
            .collect(Collectors.toUnmodifiableList());
        assertThat(responses).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    private List<OrderProductResponse> makeOrderProductResponses(final Order order) {
        final List<Product> products = order.getProducts().getValue();
        return products.stream()
            .map(it -> {
                final int quantity = productOrderDao.count(it.getId(), order.getId());
                return new OrderProductResponse(it.getId(), it.getName(), it.getAmount().getValue(), it.getImageUrl(),
                    quantity);
            })
            .collect(Collectors.toUnmodifiableList());
    }
}

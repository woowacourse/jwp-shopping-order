package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import cart.dao.CouponDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import cart.domain.vo.Amount;
import cart.dto.CartItemRequest;
import cart.dto.OrderProductResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.BusinessException;
import java.util.List;
import java.util.Optional;
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
    private final Coupon coupon = new Coupon(1L, "name", Amount.of(1_000), Amount.of(10_000), false);
    private final List<CartItemRequest> products = List.of(new CartItemRequest(1L, 5), new CartItemRequest(2L, 10));
    private final Order order = new Order(1L, new Products(List.of(product1, product2)), coupon, Amount.of(3_000),
        "address");

    @Mock
    private OrderDao orderDao;
    @Mock
    private ProductDao productDao;
    @Mock
    private CouponDao couponDao;
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("총 금액과 실제 상품 금액의 합이 같을 때 주문을 진행한다.")
    void testOrder() {
        //given
        final OrderRequest request = new OrderRequest(products, 250_000, 3_000, "address", 1L);

        given(productDao.getProductById(anyLong()))
            .willReturn(product1, product2);
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
        final OrderResponse expectedResponse = new OrderResponse(order.getId(), request.getTotalAmount(),
            order.getDeliveryAmount().getValue(), order.discountProductAmount().getValue(), order.getAddress(),
            List.of(orderProductResponse1, orderProductResponse2));

        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("총 금액과 실제 상품 금액의 합이 맞지 않을때 예외가 발생한다.")
    void testOrderWhenTotalProductAmountNotMatch() {
        //given
        final OrderRequest request = new OrderRequest(products, 25_000, 3_000, "address", 1L);
        given(productDao.getProductById(anyLong()))
            .willReturn(product1, product2);

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
        given(productDao.getProductById(anyLong()))
            .willReturn(product1, product2);
        given(couponDao.findByCouponIdAndMemberId(anyLong(), anyLong()))
            .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> orderService.order(request, member))
            .isInstanceOf(BusinessException.class);
    }
}

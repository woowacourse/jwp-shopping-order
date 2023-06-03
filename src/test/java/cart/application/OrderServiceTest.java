package cart.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.dao.ProductOrderDao;
import cart.domain.Member;
import cart.dto.CartItemRequest;
import cart.dto.OrderProductResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.factory.CouponFactory;
import cart.factory.ProductFactory;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private ProductDao productDao;

  @Mock
  private CouponDao couponDao;

  @Mock
  private OrderDao orderDao;

  @Mock
  private ProductOrderDao productOrderDao;

  @Mock
  private MemberCouponDao memberCouponDao;

  @InjectMocks
  private OrderService orderService;

  @Test
  void order() {
    final OrderProductResponse chicken = new OrderProductResponse(1L, "치킨", 20000, "chicken", 2);
    final OrderProductResponse pizza = new OrderProductResponse(2L, "피자", 15000, "pizza", 1);
    final List<OrderProductResponse> orderProductResponses = List.of(chicken, pizza);
    final int totalProductAmount = chicken.getPrice() * chicken.getQuantity() + pizza.getPrice() * pizza.getQuantity();
    final int deliveryAmount = 3000;
    final long orderId = 1L;
    final String address = "address";
    final int discountAmount = 1000;
    final OrderResponse expect = new OrderResponse(orderId, totalProductAmount, deliveryAmount, totalProductAmount - discountAmount,
        address, orderProductResponses);
    final Member member = new Member(1L, "email", "password");
    final long couponId = 1L;
    final OrderRequest orderRequest = new OrderRequest(
        List.of(new CartItemRequest(chicken.getId(), chicken.getQuantity()),
            new CartItemRequest(pizza.getId(), pizza.getQuantity())),
        totalProductAmount, deliveryAmount, address, couponId);
    given(productDao.getProductById(chicken.getId()))
        .willReturn(Optional.of(ProductFactory.createProduct(chicken.getId(), chicken.getName(), chicken.getPrice(),
            chicken.getImageUrl())));
    given(productDao.getProductById(pizza.getId()))
        .willReturn(Optional.of(
            ProductFactory.createProduct(pizza.getId(), pizza.getName(), pizza.getPrice(), pizza.getImageUrl())));
    given(couponDao.findById(couponId))
        .willReturn(Optional.of(CouponFactory.createCoupon(couponId, "1000원 할인", discountAmount, 15000)));
    given(orderDao.createOrder(any())).willReturn(orderId);

    final OrderResponse actual = orderService.order(member, orderRequest);

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expect);
  }
}

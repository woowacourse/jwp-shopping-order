package cart.application;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.OrderDao;
import cart.dao.ProductDao;
import cart.dao.ProductOrderDao;
import cart.domain.Amount;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.ProductOrder;
import cart.dto.OrderProductRequest;
import cart.dto.OrderProductResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.exception.BusinessException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final ProductDao productDao;
  private final CouponDao couponDao;
  private final OrderDao orderDao;
  private final ProductOrderDao productOrderDao;
  private final MemberCouponDao memberCouponDao;

  public OrderService(ProductDao productDao, CouponDao couponDao, OrderDao orderDao, ProductOrderDao productOrderDao,
      MemberCouponDao memberCouponDao) {
    this.productDao = productDao;
    this.couponDao = couponDao;
    this.orderDao = orderDao;
    this.productOrderDao = productOrderDao;
    this.memberCouponDao = memberCouponDao;
  }

  public OrderResponse order(final Member member, final OrderRequest orderRequest) {
    final List<ProductOrder> productOrders = findProductOrders(orderRequest.getProducts());
    final Coupon coupon = findCoupon(orderRequest.getCouponId());
    useIfSelectCoupon(member, coupon);

    final Order order = new Order(productOrders, coupon, new Amount(orderRequest.getDeliveryAmount()),
        orderRequest.getAddress(), member);
    final Long orderId = orderDao.createOrder(order);
    saveProductOrders(productOrders, orderId);
    return mapToOrderResponse(order);
  }

  private List<ProductOrder> findProductOrders(List<OrderProductRequest> requestProducts) {
    List<ProductOrder> productOrders = new ArrayList<>();

    for (final OrderProductRequest orderProductRequest : requestProducts) {
      final Product product = productDao.getProductById(orderProductRequest.getId())
          .orElseThrow(() -> new BusinessException("찾는 상품이 존재하지 않습니다."));
      productOrders.add(new ProductOrder(product, orderProductRequest.getQuantity()));
    }
    return productOrders;
  }

  private Coupon findCoupon(Long couponId) {
    return couponDao.findById(couponId)
        .orElse(Coupon.empty());
  }

  private void saveProductOrders(List<ProductOrder> productOrders, Long orderId) {
    for (ProductOrder productOrder : productOrders) {
      productOrderDao.createProductOrder(productOrder, orderId);
    }
  }

  private void useIfSelectCoupon(Member member, Coupon coupon) {
    if (!coupon.isEmpty()) {
      memberCouponDao.updateIsUsed(member.getId(), coupon.getId());
    }
  }

  private OrderResponse mapToOrderResponse(Order order) {
    return new OrderResponse(order.getId(), order.calculateTotalProductAmount().getValue(),
        order.getDeliveryAmount().getValue(), order.calculateDiscountedAmount().getValue(),
        order.getAddress(), makeOrderProductResponses(order));
  }

  private List<OrderProductResponse> makeOrderProductResponses(Order order) {
    return order.getProducts()
        .stream().map(productOrder -> mapToOrderProductResponse(productOrder, order.getId()))
        .collect(Collectors.toList());
  }

  private OrderProductResponse mapToOrderProductResponse(ProductOrder productOrder, Long orderId) {
    final Product product = productOrder.getProduct();
    return new OrderProductResponse(orderId, product.getName(), product.getPrice().getValue(), product.getImageUrl(),
        productOrder.getQuantity());
  }

  public List<OrderResponse> getAllOrders(final Member member) {
    final List<Order> orders = orderDao.findAllOrdersByMemberId(member.getId());

    return orders.stream()
        .map(this::mapToOrderResponse)
        .collect(Collectors.toList());
  }

  public OrderResponse getOrder(final Member member, final Long orderId) {
    final Order order = orderDao.findOrderByOrderId(orderId)
        .orElseThrow(() -> new BusinessException("해당 주문이 없습니다"));
    order.checkOwner(member);

    return mapToOrderResponse(order);
  }
}

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
import cart.domain.Products;
import cart.dto.CartItemRequest;
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
    final List<CartItemRequest> requestProducts = orderRequest.getProducts();
    final List<Product> products = findProducts(requestProducts);
    final Coupon coupon = findCoupon(orderRequest);

    final Order order = new Order(new Products(products), coupon, new Amount(orderRequest.getDeliveryAmount()),
        orderRequest.getAddress(), member);
    final Long orderId = orderDao.createOrder(order);
    final List<Integer> quantities = saveProductOrder(requestProducts, products, orderId);

    memberCouponDao.use(member.getId(), coupon.getId());

    final List<OrderProductResponse> orderProductResponses = makeOrderProductResponses(products, quantities);
    
    return new OrderResponse(orderId, order.calculateTotalProductAmount(quantities).getValue(),
        order.getDeliveryAmount().getValue(), order.calculateDiscountedAmount(quantities).getValue(),
        order.getAddress(), orderProductResponses);
  }

  private List<Integer> saveProductOrder(List<CartItemRequest> requestProducts, List<Product> products, Long orderId) {
    final List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
    final List<Integer> quantities = requestProducts.stream().map(CartItemRequest::getQuantity)
        .collect(Collectors.toList());
    for (int index = 0; index < productIds.size(); index++) {
      productOrderDao.createProductOrder(productIds.get(index), orderId, quantities.get(index));
    }
    return quantities;
  }

  private Coupon findCoupon(OrderRequest orderRequest) {
    return couponDao.findById(orderRequest.getCouponId())
        .orElseThrow(() -> new BusinessException("찾는 쿠폰이 존재하지 않습니다"));
  }

  private List<Product> findProducts(List<CartItemRequest> requestProducts) {
    final List<Product> products = requestProducts.stream()
        .map(product -> productDao.getProductById(product.getProductId())
            .orElseThrow(() -> new BusinessException("찾는 상품이 존재하지 않습니다.")))
        .collect(Collectors.toList());
    return products;
  }

  private List<OrderProductResponse> makeOrderProductResponses(final List<Product> products,
      final List<Integer> quantities) {
    final List<OrderProductResponse> orderProductResponses = new ArrayList<>();
    for (int index = 0; index < products.size(); index++) {
      final Product product = products.get(index);
      final OrderProductResponse orderProductResponse = new OrderProductResponse(product.getId(),
          product.getName(), product.getPrice().getValue(),
          product.getImageUrl(), quantities.get(index));
      orderProductResponses.add(orderProductResponse);
    }
    return orderProductResponses;
  }
}

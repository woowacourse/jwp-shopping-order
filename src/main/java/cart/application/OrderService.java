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
import cart.domain.Products;
import cart.dto.AllOrderResponse;
import cart.dto.CartItemRequest;
import cart.dto.OrderProductResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.SingleOrderResponse;
import cart.exception.BusinessException;
import java.util.ArrayList;
import java.util.HashMap;
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

    useIfSelectCoupon(member, coupon);

    final List<OrderProductResponse> orderProductResponses = makeOrderProductResponses(products, quantities);
    
    return new OrderResponse(orderId, order.calculateTotalProductAmount(quantities).getValue(),
        order.getDeliveryAmount().getValue(), order.calculateDiscountedAmount(quantities).getValue(),
        order.getAddress(), orderProductResponses);
  }

  private void useIfSelectCoupon(Member member, Coupon coupon) {
    if (coupon.getId() != null) {
      memberCouponDao.use(member.getId(), coupon.getId());
    }
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
        .orElse(Coupon.empty());
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

  public List<AllOrderResponse> getAllOrders(final Member member) {
    final List<ProductOrder> productOrders = orderDao.findAllOrdersByMemberId(member.getId());

    final HashMap<Long, List<OrderProductResponse>> productsByOrderId = sortProductsByOrder(productOrders);

    return productOrders.stream()
        .map(productOrder -> {
          final Long orderId = productOrder.getOrder().getId();
          return new AllOrderResponse(orderId, productsByOrderId.get(orderId));
        }).collect(Collectors.toList());
  }

  private HashMap<Long, List<OrderProductResponse>> sortProductsByOrder(final List<ProductOrder> productOrders) {
    final HashMap<Long, List<OrderProductResponse>> productsByOrderId = new HashMap<>();
    for (ProductOrder productOrder : productOrders) {
      final Long key = productOrder.getOrder().getId();
      final Product product = productOrder.getProduct();
      final List<OrderProductResponse> responses = productsByOrderId.getOrDefault(key, new ArrayList<>());
      responses.add(new OrderProductResponse(product.getId(), product.getName(), product.getPrice().getValue(),
          product.getImageUrl(), productOrder.getQuantity()));
      productsByOrderId.put(key, responses);
    }
    return productsByOrderId;
  }

  public SingleOrderResponse getOrder(final Member member, final Long orderId) {
    final List<ProductOrder> productOrders = orderDao.findOrderByOrderId(member.getId(), orderId);

    final List<OrderProductResponse> orderProductResponses = makeOrderProductResponses(productOrders);

    if (productOrders.isEmpty()) {
      return null;
    }
    final List<Integer> quantities = productOrders.stream().map(ProductOrder::getQuantity).collect(Collectors.toList());
    final Order order = productOrders.get(0).getOrder();

    return new SingleOrderResponse(orderId, orderProductResponses,
        order.calculateTotalProductAmount(quantities).getValue(),
        order.getDeliveryAmount().getValue(), order.calculateDiscountedAmount(quantities).getValue(),
        order.getAddress());
  }

  private static List<OrderProductResponse> makeOrderProductResponses(List<ProductOrder> productOrders) {
    return productOrders.stream()
        .map(productOrder -> {
          final Product product = productOrder.getProduct();
          return new OrderProductResponse(product.getId(), product.getName(), product.getPrice().getValue(),
              product.getImageUrl(), productOrder.getQuantity());
        }).collect(Collectors.toList());
  }
}

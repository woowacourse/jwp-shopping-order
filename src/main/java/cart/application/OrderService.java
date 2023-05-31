package cart.application;

import cart.domain.*;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.dto.order.OrderRequest;
import cart.persistence.dao.CartItemDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemDao cartItemDao; // TODO repository 생성
    private final CouponRepository couponRepository;

    public OrderService(OrderRepository orderRepository, CartItemDao cartItemDao, CouponRepository couponRepository) {
        this.orderRepository = orderRepository;
        this.cartItemDao = cartItemDao;
        this.couponRepository = couponRepository;
    }

    // 주문 신청
    public Long addOrder(Member member, OrderRequest orderRequest) {
        // TODO selectCartId & couponId가 존재하지 않을 경우 예외 처리
        // 주문한 장바구니 상품 리스트 조회
        List<OrderProduct> orderProducts = getOrderProducts(orderRequest.getSelectCartIds());

        // 총가격, 할인가 계산
        int totalPrice = calculateTotalPriceOfProducts(orderProducts);
        Coupon coupon = couponRepository.findById(orderRequest.getCouponId());
        Order order = new Order(totalPrice, coupon.discount(totalPrice), orderProducts, coupon, member);

        // 주문한 상품을 장바구니에서 제거
        orderRequest.getSelectCartIds().stream()
                .forEach(cartItemDao::deleteById);

        return orderRepository.add(order);
    }

    public List<OrderProduct> getOrderProducts(List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(cartId -> {
                    CartItem cartItem = cartItemDao.findById(cartId);
                    Product product = cartItem.getProduct();
                    return new OrderProduct(
                            product.getName(),
                            product.getPrice(),
                            product.getImageUrl(),
                            cartItem.getQuantity()
                    );
                }).collect(Collectors.toList());
    }

    public int calculateTotalPriceOfProducts(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(OrderProduct::getPrice)
                .reduce(Integer::sum)
                .get();
    }


    // 주문 리스트 조회

    // 주문 상세 조회

    // 주문 취소

    // 주문 확정

}

/*

 * [ ] 주문 확정
 * [ ] 주문 확정시 사용 가능한 쿠폰을 유저에게 발급

 * [ ] 주문 리스트 조회
 * [ ] 주문 상세 조회

 * [ ] 주문 취소
 * [ ] 주문시 사용했던 쿠폰 재발급
 */

package cart.application;

import cart.domain.Member;
import cart.domain.Orders;
import cart.domain.PriceValidator;
import cart.domain.couponissuer.CouponIssuer;
import cart.domain.couponissuer.Issuer;
import cart.dto.CouponResponse;
import cart.dto.OrdersRequest;
import cart.dto.OrdersResponse;
import cart.repository.CouponRepository;
import cart.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final PriceValidator priceValidator;
    private final CouponRepository couponRepository;

    public OrdersService(OrdersRepository ordersRepository, PriceValidator priceValidator, CouponRepository couponRepository) {
        this.ordersRepository = ordersRepository;
        this.priceValidator = priceValidator;
        this.couponRepository = couponRepository;
    }

    public Long takeOrders(Member member, final OrdersRequest ordersRequest) {
        final List<Long> cartIds = ordersRequest.getCartProductIds();
        final int originalPrice = ordersRequest.getOriginalPrice();
        final int discountPrice = ordersRequest.getDiscountPrice();
        final List<Long> coupons = List.of(ordersRequest.getCouponId());
        priceValidator.validateOrders(cartIds,discountPrice,coupons);
        return ordersRepository.takeOrders(member.getId(), cartIds, originalPrice, discountPrice, coupons);
    }

    public List<OrdersResponse> findMembersAllOrders(final Member member) {
        return ordersRepository.findAllOrdersByMember(member).stream()
                .map(OrdersResponse::of).collect(Collectors.toList());
    }

    public OrdersResponse findOrdersById(final Member member, final long id) {
        return OrdersResponse.ofDetail(ordersRepository.findOrdersById(member, id));
    }

    public CouponResponse confirmOrders(Member member, long id) {
        CouponIssuer couponIssuer = new Issuer(couponRepository);
        Orders orders =  ordersRepository.confirmOrdersCreateCoupon(member,id);
        return CouponResponse.of(couponIssuer.issue(orders));
    }

    public void deleteOrders(final long id) {
        ordersRepository.deleteOrders(id);
    }
}

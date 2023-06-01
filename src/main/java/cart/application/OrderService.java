package cart.application;

import cart.dao.CartItemDao;
import cart.domain.cartItem.CartItem;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupons;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.product.Product;
import cart.dto.order.OrderProductRequest;
import cart.dto.order.OrderRequest;
import cart.exception.MemberCouponNotFoundException;
import cart.exception.OrderCartMismatchException;
import cart.repository.MemberCouponRepository;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final CartItemDao cartItemDao;

    public OrderService(final OrderRepository orderRepository, final MemberCouponRepository memberCouponRepository, final CartItemDao cartItemDao) {
        this.orderRepository = orderRepository;
        this.memberCouponRepository = memberCouponRepository;
        this.cartItemDao = cartItemDao;
    }

    @Transactional
    public Long createOrder(List<OrderRequest> orderRequests, Member member) {
        clearCartItems(orderRequests, member.getId());
        List<OrderItem> orderItems = requestToOrderItems(orderRequests, member);
        return orderRepository.create(orderItems, member.getId());
    }

    private List<OrderItem> requestToOrderItems(final List<OrderRequest> orderRequests, final Member member) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequest orderRequest : orderRequests) {
            MemberCoupons requestCoupons = usedCoupon(member, orderRequest);

            OrderProductRequest productRequest = orderRequest.getProductRequest();
            orderItems.add(new OrderItem(productRequest.toProduct(), orderRequest.getQuantity(), requestCoupons.getCoupons()));
        }
        return orderItems;
    }

    private MemberCoupons usedCoupon(final Member member, final OrderRequest orderRequest) {
        MemberCoupons requestCoupons = memberCouponRepository.findByIds(orderRequest.getMemberCouponIds());
        MemberCoupons memberCoupons = memberCouponRepository.findByMemberId(member.getId()).getUnUsedCoupons();

        System.out.println(requestCoupons);
        System.out.println(memberCoupons);

        if (memberCoupons.isNotContains(requestCoupons)) {
            throw new MemberCouponNotFoundException();
        }

        memberCouponRepository.updateCoupons(requestCoupons.useCoupons(), member);
        return requestCoupons;
    }

    private void clearCartItems(final List<OrderRequest> orderRequests, final Long memberId) {
        List<Product> memberCartProducts = cartItemDao.findByMemberId(memberId)
                .stream()
                .map(CartItem::getProduct)
                .collect(Collectors.toList());

        List<Product> products = orderRequests.stream()
                .map(orderRequest -> orderRequest.getProductRequest().toProduct())
                .collect(Collectors.toList());

        if (!memberCartProducts.containsAll(products)) {
            throw new OrderCartMismatchException();
        }

        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        cartItemDao.delete(memberId, productIds);
    }

    public List<Order> findAll(Long memberId) {
        return orderRepository.findAllByMemberId(memberId);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id);
    }
}

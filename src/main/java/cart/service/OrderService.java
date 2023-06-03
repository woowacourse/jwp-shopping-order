package cart.service;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.domain.coupon.MemberCoupon;
import cart.dto.OrderSaveRequest;
import cart.dto.OrdersDto;
import cart.exception.MemberNotFoundException;
import cart.repository.CartItemRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;

    public OrderService(final CartItemRepository cartItemRepository, final OrderRepository orderRepository,
                        final MemberRepository memberRepository, final MemberCouponRepository memberCouponRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public Long order(final Long memberId, final OrderSaveRequest request) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        MemberCoupon memberCoupon = getMemberCoupon(member, request);
        final List<CartItem> findCartItems = getCartItems(memberId, request);
        final List<OrderItem> orderItems = getOrderItems(findCartItems);

        final Order newOrder = new Order(memberCoupon, member, orderItems);

        final Long savedOrderId = orderRepository.save(newOrder).getId();

        for (CartItem findCartItem : findCartItems) {
            cartItemRepository.deleteById(findCartItem.getId(), member.getId());
        }
        memberCouponRepository.useMemberCoupon(memberId, memberCoupon.getId());
        return savedOrderId;
    }

    private MemberCoupon getMemberCoupon(final Member member, final OrderSaveRequest request) {
        if (Objects.isNull(request.getCouponId())) {
            return MemberCoupon.makeNonMemberCoupon();
        }
        return memberCouponRepository.findByIdAndMemberId(request.getCouponId(), member.getId());
    }

    private List<OrderItem> getOrderItems(final List<CartItem> findCartItems) {
        return findCartItems.stream()
                .map(it -> {
                    String productName = it.getProduct().getName();
                    long price = it.getProduct().getPrice();
                    String imageUrl = it.getProduct().getImageUrl();
                    Integer quantity = it.getQuantity();

                    return new OrderItem(productName, price, imageUrl, quantity);
                })
                .collect(Collectors.toList());
    }

    private List<CartItem> getCartItems(final Long memberId, final OrderSaveRequest request) {
        return cartItemRepository.findAllByCartItemIds(memberId, request.getOrderItems());
    }

    @Transactional(readOnly = true)
    public OrdersDto findByOrderId(final Long memberId, final Long orderId) {
        return new OrdersDto(orderRepository.findByOrderIdAndMemberId(orderId, memberId));
    }

    @Transactional(readOnly = true)
    public List<OrdersDto> findAllByMemberId(final Long memberId) {
        return orderRepository.findAllByMemberId(memberId).stream()
                .map(OrdersDto::new)
                .collect(Collectors.toList());
    }
}

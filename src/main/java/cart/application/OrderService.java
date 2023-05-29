package cart.application;

import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.OrderRepository;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.dto.OrderRequest;
import cart.exception.PointExceedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository,
                        MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.memberRepository = memberRepository;
    }

    public void issue(Member member, OrderRequest request) {
        List<CartItem> cartItems = request.getOrder().stream()
                .map(cartItemRepository::findById)
                .collect(Collectors.toList());

        Order order = new Order(cartItems, request.getOriginalPrice(),
                request.getUsedPoint(), request.getPointToAdd());
        orderRepository.insert(order);
        subtractPoint(member, request.getUsedPoint());
    }

    private void subtractPoint(Member member, int usedPoint) {
        if (member.getPoint() - usedPoint < 0) {
            throw new PointExceedException();
        }

        Member updatedMember = new Member(member.getId(), member.getEmail(),
                member.getPassword(), member.getPoint() - usedPoint);
        memberRepository.update(updatedMember);
    }
}

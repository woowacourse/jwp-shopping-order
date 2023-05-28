package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.dto.OrderRequest;
import cart.exception.point.InvalidPointUseException;
import cart.exception.point.PointAbusedException;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;
    private final OrderRepository orderRepository;

    public OrderService(final MemberDao memberDao, final CartItemDao cartItemDao, final OrderRepository orderRepository) {
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
        this.orderRepository = orderRepository;
    }


    @Transactional
    public Long order(final Member member, final OrderRequest request) {
        final Member findMember = memberDao.getMemberByEmail(member.getEmailValue());
        if (findMember.getPointValue() < request.getPoint()) {
            throw new PointAbusedException(member.getPointValue(), request.getPoint());
        }
        final CartItems cartItems = new CartItems(cartItemDao.getCartItemsByIds(request.getCartItemIds()));
        final int totalPrice = cartItems.calculateTotalPrice();
        if (totalPrice < request.getPoint()) {
            throw new InvalidPointUseException(totalPrice, request.getPoint());
        }
        final Member updatedMember = findMember.updatePoint(new MemberPoint(request.getPoint()), totalPrice);
        memberDao.updateMember(updatedMember);

        return orderRepository.save(cartItems, updatedMember, new MemberPoint(request.getPoint()));
    }
}

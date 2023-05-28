package cart.application;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderProductDao;
import cart.dao.ProductDao;
import cart.domain.cartitem.CartItems;
import cart.domain.member.Member;
import cart.domain.member.MemberPoint;
import cart.domain.orderproduct.Order;
import cart.domain.orderproduct.OrderProduct;
import cart.domain.product.Product;
import cart.dto.OrderRequest;
import cart.exception.point.InvalidPointUseException;
import cart.exception.point.PointAbusedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MemberDao memberDao;
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final OrderDao orderDao;
    private final OrderProductDao orderProductDao;

    public OrderService(final MemberDao memberDao, final CartItemDao cartItemDao, final ProductDao productDao,
                        final OrderDao orderDao, final OrderProductDao orderProductDao) {
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.orderDao = orderDao;
        this.orderProductDao = orderProductDao;
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

        final Long orderId = orderDao.save(new Order(updatedMember, new MemberPoint(request.getPoint())));
        final List<Product> products = productDao.getProductsById(cartItems.getProductIds());
        final List<OrderProduct> orderProducts = cartItems.toOrderProducts(orderId, products);

        orderProductDao.save(orderProducts);

        return orderId;
    }
}

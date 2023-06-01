package cart.repository;

import cart.dao.OrdersCartItemDao;
import cart.dao.OrdersDao;
import cart.dao.ProductDao;
import cart.dao.entity.OrdersEntity;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Orders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrdersRepository {
    private final OrdersDao ordersDao;
    private final OrdersCartItemDao ordersCartItemDao;
    private final ProductDao productDao;

    public OrdersRepository(OrdersDao ordersDao, OrdersCartItemDao ordersCartItemDao, ProductDao productDao) {
        this.ordersDao = ordersDao;
        this.ordersCartItemDao = ordersCartItemDao;
        this.productDao = productDao;
    }

    public long takeOrders(final Long memberId, final int discountPrice) {
        return ordersDao.createOrders(memberId, discountPrice);
    }

    public List<Orders> findAllOrdersByMember(final Member member) {
        return ordersDao.findAllByMemberId(member.getId()).stream()
                .map(this::rendering)
                .collect(Collectors.toList());
    }

    public Optional<Orders> findOrdersById(final Member member, final long id) {
        return rendering(ordersDao.findById(id));
    }
    public List<CartItem> findCartItemByOrdersIds(final Member member,final long id){
        return ordersCartItemDao.findAllByOrdersId(id).stream()
                .map(entry -> new CartItem(
                        member,
                        productDao.findById(entry.getProductId())
                ))
                .collect(Collectors.toList());
    }
    public Optional<Orders>  confirmOrdersCreateCoupon(Member member,final long id) {
        ordersDao.updateConfirmById(id);
        return findOrdersById(member,id);
    }

    public void deleteOrders(final long id) {

        ordersDao.deleteById(id);
    }

    private Optional<Orders> rendering(Optional<OrdersEntity> orders){
        if(orders.isEmpty()){
            return Optional.empty();
        }
        return Optional.ofNullable(new Orders(
                orders.get().getId(),
                orders.get().getPrice(),
                orders.get().getConfirmState()
        ));
    }
    private Orders rendering(OrdersEntity orders){
        return new Orders(
                orders.getId(),
                orders.getPrice(),
                orders.getConfirmState()
        );
    }
}

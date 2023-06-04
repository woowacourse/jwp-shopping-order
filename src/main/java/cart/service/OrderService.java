package cart.service;

import cart.dao.CouponBoxDao;
import cart.dao.CouponDao;
import cart.domain.*;
import cart.dto.OrderReqeust;
import cart.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final CartItemService cartItemService;
    private final OrderRepository orderRepository;
    private final CouponBoxDao couponBoxDao;

    public OrderService(CartItemService cartItemService, OrderRepository orderRepository, CouponBoxDao couponBoxDao) {
        this.cartItemService = cartItemService;
        this.orderRepository = orderRepository;
        this.couponBoxDao = couponBoxDao;
    }

    public Long order(Member member, OrderReqeust orderReqeust) {
        Order order = cartItemService.order(member, orderReqeust);
        Long id = orderRepository.insert(member, order);

        for (Long cartItemId : orderReqeust.getCartItemIds()) {
            cartItemService.remove(member, cartItemId);
        }

        for (Long couponId : orderReqeust.getCouponIds()) {
            couponBoxDao.delete(member.getId(), couponId);
        }

        return id;
    }
}



package com.woowahan.techcourse.order.service;

import com.woowahan.techcourse.order.db.OrderDao;
import com.woowahan.techcourse.order.domain.Order;
import com.woowahan.techcourse.order.exception.OrderNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderDao orderDao;

    public OrderQueryService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public List<Order> findAllByMemberId(long memberId) {
        return orderDao.findAllByMemberId(memberId);
    }

    public Order findById(long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }
}

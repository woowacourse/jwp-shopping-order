package com.woowahan.techcourse.order.service;

import com.woowahan.techcourse.order.db.OrderDao;
import com.woowahan.techcourse.order.domain.ActualPriceCalculator;
import com.woowahan.techcourse.order.domain.Order;
import com.woowahan.techcourse.order.domain.OrderResult;
import com.woowahan.techcourse.order.service.dto.request.CreateOrderRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderCommandService {

    private final OrderDao orderDao;
    private final ActualPriceCalculator actualPriceCalculator;

    public OrderCommandService(OrderDao orderDao, ActualPriceCalculator actualPriceCalculator) {
        this.orderDao = orderDao;
        this.actualPriceCalculator = actualPriceCalculator;
    }

    public Long createOrder(long memberId, CreateOrderRequestDto requestDto) {
        Order order = requestDto.toOrder(memberId);
        OrderResult orderResult = new OrderResult(order.calculateOriginalPrice(),
                actualPriceCalculator.calculate(order), order);
        return orderDao.insert(orderResult);
    }

}

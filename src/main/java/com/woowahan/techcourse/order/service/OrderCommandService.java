package com.woowahan.techcourse.order.service;

import com.woowahan.techcourse.order.db.OrderDao;
import com.woowahan.techcourse.order.domain.ActualPriceCalculator;
import com.woowahan.techcourse.order.domain.CartItemRemover;
import com.woowahan.techcourse.order.domain.CouponExpire;
import com.woowahan.techcourse.order.domain.Order;
import com.woowahan.techcourse.order.domain.OrderCoupon;
import com.woowahan.techcourse.order.domain.OrderItem;
import com.woowahan.techcourse.order.service.dto.request.CreateOrderRequest;
import com.woowahan.techcourse.order.service.dto.request.CreateOrderRequest.CreateOrderCartItemRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderCommandService {

    private final OrderDao orderDao;
    private final ActualPriceCalculator actualPriceCalculator;
    private final CartItemRemover cartItemRemover;
    private final CouponExpire couponExpire;

    public OrderCommandService(OrderDao orderDao, ActualPriceCalculator actualPriceCalculator,
            CartItemRemover cartItemRemover, CouponExpire couponExpire) {
        this.orderDao = orderDao;
        this.actualPriceCalculator = actualPriceCalculator;
        this.cartItemRemover = cartItemRemover;
        this.couponExpire = couponExpire;
    }

    public Long createOrder(long memberId, CreateOrderRequest requestDto) {
        Order order = toOrder(memberId, requestDto);
        couponExpire.makeExpired(order);
        cartItemRemover.removeCartItem(order);
        return orderDao.insert(order);
    }

    private Order toOrder(long memberId, CreateOrderRequest requestDto) {
        List<OrderItem> orderItems = toOrderItems(requestDto.getCartItems());
        List<OrderCoupon> orderCoupons = toOrderCoupons(requestDto.getCouponIds());
        return new Order(memberId, orderItems, orderCoupons, actualPriceCalculator);
    }

    private List<OrderItem> toOrderItems(List<CreateOrderCartItemRequest> requestDtos) {
        return requestDtos.stream()
                .map(this::toOrderItem)
                .collect(Collectors.toList());
    }

    private OrderItem toOrderItem(CreateOrderCartItemRequest requestDto) {
        return new OrderItem(requestDto.getQuantity(),
                requestDto.getProduct().getId(),
                requestDto.getProduct().getPrice(),
                requestDto.getProduct().getName(),
                requestDto.getProduct().getImageUrl());
    }

    private List<OrderCoupon> toOrderCoupons(List<Long> couponIds) {
        return couponIds.stream()
                .map(OrderCoupon::new)
                .collect(Collectors.toList());
    }
}

package shop.ui.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.application.order.OrderService;
import shop.application.order.dto.OrderDetailDto;
import shop.application.order.dto.OrderCreationDto;
import shop.application.order.dto.OrderProductDto;
import shop.domain.coupon.Coupon;
import shop.domain.member.Member;
import shop.domain.order.Order;
import shop.domain.order.OrderItem;
import shop.domain.product.Product;
import shop.ui.order.dto.request.OrderCreationRequest;
import shop.ui.order.dto.response.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(Member member, @RequestBody OrderCreationRequest request) {
        List<OrderProductDto> orderProductDtos = request.getItems().stream()
                .map(item -> new OrderProductDto(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList());
        OrderCreationDto orderCreationDto = new OrderCreationDto(orderProductDtos, request.getCouponId());

        Long orderId = orderService.order(member, orderCreationDto);

        return ResponseEntity.created(URI.create("/orders/" + orderId)).build();
    }

    @GetMapping
    public ResponseEntity<List<OrderHistoryResponse>> getAllOrderHistory(Member member) {
        List<Order> orders = orderService.getAllOrderHistoryOfMember(member);

        List<OrderHistoryResponse> orderHistoryResponses = orders.stream()
                .map(this::toOrderHistoryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderHistoryResponses);
    }

    private OrderHistoryResponse toOrderHistoryResponse(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();

        List<OrderProductResponse> orderProductResponses = orderItems.stream()
                .map(this::toOrderProductResponse)
                .collect(Collectors.toList());

        return new OrderHistoryResponse(order.getId(), orderProductResponses, order.getOrderedAt());
    }

    private OrderProductResponse toOrderProductResponse(OrderItem orderItem) {
        return new OrderProductResponse(
                toProductDetailResponse(orderItem.getProduct()),
                orderItem.getQuantity()
        );
    }

    private OrderProductDetailResponse toProductDetailResponse(Product product) {
        return new OrderProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetails(Member member, @PathVariable Long orderId) {
        OrderDetailDto orderDetailDto = orderService.getOrderDetailsOfMember(member, orderId);
        Order order = orderDetailDto.getOrder();
        Coupon coupon = orderDetailDto.getCoupon();

        UsingCouponResponse usingCouponResponse =
                new UsingCouponResponse(coupon.getName(), coupon.getDiscountRate());
        Long totalPrice = order.getOrderPrice();
        int discountRate = coupon.getDiscountRate();
        Long discountedTotalPrice = totalPrice * (100 + discountRate) / 100;
        Long discountPrice = totalPrice - discountedTotalPrice;
        Integer deliveryPrice = order.getDeliveryPrice();
        LocalDateTime orderedAt = order.getOrderedAt();

        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(
                orderId,
                usingCouponResponse,
                order.getOrderItems().stream()
                        .map(this::toOrderProductResponse)
                        .collect(Collectors.toList()),
                totalPrice,
                discountedTotalPrice,
                discountPrice,
                deliveryPrice,
                orderedAt
        );

        return ResponseEntity.ok(orderDetailResponse);
    }
}

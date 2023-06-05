package cart.dao;

import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import cart.domain.vo.Amount;
import cart.exception.BusinessException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderConverter {

    public static Optional<Order> convertToOrder(final List<OrderProductJoinDto> orderProductJoinDtos) {
        final List<Order> orders = removeDuplicate(orderProductJoinDtos);
        if (orders.size() == 1) {
            return Optional.of(orders.get(0));
        }
        return Optional.empty();
    }

    public static List<Order> convertToOrders(final List<OrderProductJoinDto> orderProductJoinDtos) {
        return removeDuplicate(orderProductJoinDtos);
    }

    private static List<Order> removeDuplicate(final List<OrderProductJoinDto> dtos) {
        final Map<Long, Set<OrderProductJoinDto>> orderBoard = collectDtosByOrderId(dtos);
        final List<Order> orders = new ArrayList<>();
        for (final Long id : orderBoard.keySet()) {
            final Set<OrderProductJoinDto> sameOrderKeyDtos = orderBoard.get(id);
            final Order line = makeOrder(sameOrderKeyDtos);
            orders.add(line);
        }
        return orders;
    }

    private static Map<Long, Set<OrderProductJoinDto>> collectDtosByOrderId(
        final List<OrderProductJoinDto> dtos) {
        final Map<Long, Set<OrderProductJoinDto>> orderBoard = new HashMap<>();
        for (final OrderProductJoinDto dto : dtos) {
            if (orderBoard.containsKey(dto.getId())) {
                orderBoard.get(dto.getId()).add(dto);
                continue;
            }
            orderBoard.put(dto.getId(), new HashSet<>(List.of(dto)));
        }
        return orderBoard;
    }

    private static Order makeOrder(final Set<OrderProductJoinDto> sameOrderKeyDtos) {
        final List<Product> products = sameOrderKeyDtos.stream()
            .map(it -> new Product(it.getProductId(), it.getProductName(), Amount.of(it.getProductAmount()),
                it.getProductImageUrl()))
            .collect(Collectors.toList());
        final OrderProductJoinDto dto = sameOrderKeyDtos.stream()
            .findAny()
            .orElseThrow(() -> new BusinessException("매핑 실패"));
        return new Order(dto.getId(), new Products(products), Amount.of(dto.getTotalAmount()),
            Amount.of(dto.getDiscountedAmount()), Amount.of(dto.getDeliveryAmount()),
            dto.getAddress());
    }
}

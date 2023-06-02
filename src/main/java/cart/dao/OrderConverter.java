package cart.dao;

import cart.domain.Order;
import cart.domain.Product;
import cart.domain.Products;
import cart.domain.vo.Amount;
import cart.exception.BusinessException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderConverter {

    public static Order convertToOrder(final List<OrderProductJoinDto> orderProductJoinDtos) {
        final List<Order> orders = removeDuplicate(orderProductJoinDtos);
        if (orders.size() == 1) {
            return orders.get(0);
        }
        throw new BusinessException("매핑 실패");
    }

    private static List<Order> removeDuplicate(final List<OrderProductJoinDto> dtos) {
        final Map<Long, List<OrderProductJoinDto>> orderBoard = collectDtosByOrderId(dtos);
        final List<Order> lines = new ArrayList<>();
        for (final Long id : orderBoard.keySet()) {
            final List<OrderProductJoinDto> sameOrderKeyDtos = orderBoard.get(id);
            final Order line = makeOrder(sameOrderKeyDtos);
            lines.add(line);
        }
        return lines;
    }

    private static Map<Long, List<OrderProductJoinDto>> collectDtosByOrderId(
        final List<OrderProductJoinDto> dtos) {
        final Map<Long, List<OrderProductJoinDto>> orderBoard = new HashMap<>();
        for (final OrderProductJoinDto dto : dtos) {
            if (orderBoard.containsKey(dto.getId())) {
                orderBoard.get(dto.getId()).add(dto);
                continue;
            }
            orderBoard.put(dto.getId(), new ArrayList<>(List.of(dto)));
        }
        return orderBoard;
    }

    private static Order makeOrder(final List<OrderProductJoinDto> sameOrderKeyDtos) {
        final List<Product> products = sameOrderKeyDtos.stream()
            .map(it -> new Product(it.getProductId(), it.getProductName(), Amount.of(it.getProductAmount()),
                it.getProductImageUrl()))
            .collect(Collectors.toList());
        final OrderProductJoinDto dto = sameOrderKeyDtos.get(0);
        return new Order(dto.getId(), new Products(products), null,
            Amount.of(dto.getDeliveryAmount()), Amount.of(dto.getTotalAmount()), dto.getAddress());
    }

}

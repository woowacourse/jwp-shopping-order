package cart.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Orders {
    private final List<Order> orders;

    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders of(List<Order> orders) {
        Map<List<Object>, List<Order>> groupedOrder = orders.stream()
                .collect(Collectors.groupingBy(order -> Arrays.asList(order.getId(), order.getMember(), order.getOrderedAt(), order.getUsedPoint())));

        List<Order> groupedOrders = groupedOrder.values().stream()
                .map(group -> {
                    Order firstOrder = group.get(0);
                    OrderedItems mergedItems = group.stream()
                            .map(Order::getOrderedItems)
                            .reduce(new OrderedItems(new ArrayList<>()), OrderedItems::merge);

                    return new Order(firstOrder.getId(), firstOrder.getMember(), firstOrder.getOrderedAt(), firstOrder.getUsedPoint(), mergedItems);
                })
                .sorted(Comparator.comparing(Order::getId))
                .collect(Collectors.toList());
        return new Orders(groupedOrders);
    }

    public void calculateSavedPoint() {
        this.orders.forEach(Order::calculateSavedPoint);
    }

    public List<Order> getOrders() {
        return List.copyOf(orders);
    }
}

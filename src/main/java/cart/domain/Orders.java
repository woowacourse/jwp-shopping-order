package cart.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Orders {
    private final List<Order> orders;

    private Orders(List<Order> orders) {
        this.orders = orders;
    }

    public static Orders of(List<Order> orders) {
        Map<List<Object>, List<Order>> groupedOrder = groupOrders(orders);

        List<Order> groupedOrders = groupedOrder.values().stream()
                .map(Orders::createGroupedOrder)
                .sorted(Comparator.comparing(Order::getId))
                .collect(Collectors.toList());
        return new Orders(groupedOrders);
    }

    private static Map<List<Object>, List<Order>> groupOrders(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(order -> Arrays.asList(order.getId(), order.getMember(), order.getOrderedAt(), order.getUsedPoint())));
    }

    private static Order createGroupedOrder(List<Order> group) {
        Order firstOrder = group.get(0);
        OrderedItems mergedItems = mergeOrderedItems(group);

        return new Order(firstOrder.getId(), firstOrder.getMember(), firstOrder.getOrderedAt(), firstOrder.getUsedPoint(), mergedItems);
    }

    private static OrderedItems mergeOrderedItems(List<Order> group) {
        return group.stream()
                .map(Order::getOrderedItems)
                .reduce(new OrderedItems(new ArrayList<>()), OrderedItems::merge);
    }

    public void calculateSavedPoint() {
        this.orders.forEach(Order::calculateSavedPoint);
    }

    public List<Order> getOrders() {
        return List.copyOf(orders);
    }
}

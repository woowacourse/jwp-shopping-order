package cart.domain.order;

import cart.exception.DiscountOverPriceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderPrice {
    private static final int MIN_ORDER_PRICE = 1;
    private final Map<OrderProduct, Integer> priceOfProduct;
    private final int globalDiscount;
    private final DeliveryFee deliveryFee;

    private OrderPrice(final Map<OrderProduct, Integer> priceOfProduct, final int globalDiscount, final DeliveryFee deliveryFee) {
        this.priceOfProduct = priceOfProduct;
        this.globalDiscount = globalDiscount;
        this.deliveryFee = deliveryFee;
    }

    public static OrderPrice of(final List<OrderProduct> products, final DeliveryFee deliveryFee) {
        return new OrderPrice(setProductPrice(products), 0, deliveryFee);
    }

    private static Map<OrderProduct, Integer> setProductPrice(final List<OrderProduct> products) {
        final HashMap<OrderProduct, Integer> productPrice = new HashMap<>();
        for (final OrderProduct product : products) {
            productPrice.put(product, product.price());
        }
        return productPrice;
    }

    public OrderPrice discountGlobal(final int discount) {
        if (calculatePrice() - discount < MIN_ORDER_PRICE) {
            throw new DiscountOverPriceException("주문 가격 할인이 주문 가격을 넘었습니다.");
        }
        return new OrderPrice(priceOfProduct, globalDiscount + discount, deliveryFee);
    }

    public int calculatePrice() {
        final Integer productPrice = priceOfProduct.values().stream().reduce(Integer::sum)
                .orElseThrow(() -> new IllegalStateException("상품 가격 계산 과정에서 오류가 발생했습니다."));
        return productPrice + deliveryFee.getDeliveryFee() - globalDiscount;
    }
}

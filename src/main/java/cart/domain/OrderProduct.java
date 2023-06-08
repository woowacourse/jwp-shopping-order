package cart.domain;

import java.util.List;

public class OrderProduct {

    private static final int MAIN_PRODUCT_INDEX = 0;

    private final OrderInfo orderInfo;
    private final List<Product> products;

    public OrderProduct(final OrderInfo orderInfo, final List<Product> products) {
        this.orderInfo = orderInfo;
        this.products = products;
    }

    public Product getMainProduct() {
        return products.get(MAIN_PRODUCT_INDEX);
    }

    public int getProductCount() {
        return products.size();
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public List<Product> getProducts() {
        return products;
    }
}

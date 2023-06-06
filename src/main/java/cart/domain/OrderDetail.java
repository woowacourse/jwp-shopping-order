package cart.domain;

import java.util.Objects;

public class OrderDetail {

    private Long id;
    private final Orders orders;
    private final Product product;
    private final String productName;
    private final PriceVO productPriceVo;
    private final String productImageUrl;
    private final QuantityVO orderQuantityVO;

    public OrderDetail(Orders orders, Product product, String productName, int productPrice, String productImageUrl, int orderQuantity) {
        this(null, orders, product, productName, productPrice, productImageUrl, orderQuantity);
    }

    public OrderDetail(Long id, Orders orders, Product product, String productName, int productPrice, String productImageUrl, int orderQuantity) {
        this.id = id;
        this.orders = orders;
        this.product = product;
        this.productName = productName;
        this.productPriceVo = new PriceVO(productPrice);
        this.productImageUrl = productImageUrl;
        this.orderQuantityVO = new QuantityVO(orderQuantity);
    }

    public Long getId() {
        return id;
    }

    public Orders getOrders() {
        return orders;
    }

    public Product getProduct() {
        return product;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPriceVo.getPrice();
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public int getOrderQuantity() {
        return orderQuantityVO.getQuantity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetail)) return false;
        OrderDetail that = (OrderDetail) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

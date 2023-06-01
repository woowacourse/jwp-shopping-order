package cart.dto;

import cart.domain.Item;
import cart.domain.Product;
import java.math.BigDecimal;

public class OrderProductResponse {
    private Long id;
    private BigDecimal price;
    private String name;
    private String imageUrl;
    private int quantity;

    public OrderProductResponse() {
    }

    public OrderProductResponse(Long id, BigDecimal price, String name, String imageUrl, int quantity) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public static OrderProductResponse from(Item item) {
        Product product = item.getProduct();
        return new OrderProductResponse(product.getId(), product.getPrice().getValue(), product.getName(),
                product.getImageUrl(), item.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }
}

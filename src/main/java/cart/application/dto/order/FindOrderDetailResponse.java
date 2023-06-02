package cart.application.dto.order;

import java.util.List;

public class FindOrderDetailResponse {

    private final long id;
    private final List<FindOrderDetailProductResponse> products;
    private final int totalProductPrice;
    private final int discountPrice;
    private final int shippingFee;

    public FindOrderDetailResponse(final long id,
            final List<FindOrderDetailProductResponse> products, final int totalProductPrice, final int discountPrice,
            final int shippingFee) {
        this.id = id;
        this.products = products;
        this.totalProductPrice = totalProductPrice;
        this.discountPrice = discountPrice;
        this.shippingFee = shippingFee;
    }

    public long getId() {
        return id;
    }

    public List<FindOrderDetailProductResponse> getProducts() {
        return products;
    }

    public int getTotalProductPrice() {
        return totalProductPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getShippingFee() {
        return shippingFee;
    }
}

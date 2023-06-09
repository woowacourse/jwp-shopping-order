package cart.dto;

import java.util.List;

public class ProductResponses {
    private PageInfo pageInfo;
    private List<ProductResponse> products;

    public ProductResponses() {
    }

    public ProductResponses(final PageInfo pageInfo, final List<ProductResponse> products) {
        this.pageInfo = pageInfo;
        this.products = products;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }
}

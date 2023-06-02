package cart.dto;

public class OrderedItemResponse {

    private int quantity;
    //TODO: OrderedItemsResponse? OrderedItemResponse로 변경(quantity필드 안에 넣기)
    //TODO: Cost Controller... 삭제
    private ProductResponse productResponse;

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public OrderedItemResponse(int quantity, ProductResponse productResponse) {
        this.quantity = quantity;
        this.productResponse = productResponse;
    }

    public int getQuantity() {
        return quantity;
    }
}

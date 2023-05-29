package cart.application.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class PostOrderRequest {

    @Min(value = 0, message = "0이상의 값을 입력해야 합니다")
    private Integer usedPoint;

    @Size(min = 1, message = "하나 이상의 상품이 포함되어야 합니다")
    private List<@Valid SingleKindProductRequest> products;

    private PostOrderRequest() {
    }

    public PostOrderRequest(Integer usedPoint, List<SingleKindProductRequest> products) {
        this.usedPoint = usedPoint;
        this.products = products;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public List<SingleKindProductRequest> getProducts() {
        return products;
    }
}

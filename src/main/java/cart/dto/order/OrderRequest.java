package cart.dto.order;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

public class OrderRequest {

    @Size(min = 1, message = "주문 아이템은 1개 이상이어야합니다.")
    private List<@Positive(message = "장바구니 아이디가 잘못되었습니다.") Long> cartIds;
    @PositiveOrZero(message = "포인트는 0원 이상 사용할 수 있습니다.")
    private Long point;
    @PositiveOrZero(message = "아이템을 1개 이상 주문해주세요.")
    private Long totalPrice;

    public OrderRequest() {

    }

    public OrderRequest(List<Long> cartIds, Long point, Long totalPrice) {
        this.cartIds = cartIds;
        this.point = point;
        this.totalPrice = totalPrice;
    }

    public List<Long> getCartIds() {
        return cartIds;
    }

    public Long getPoint() {
        return point;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }
}

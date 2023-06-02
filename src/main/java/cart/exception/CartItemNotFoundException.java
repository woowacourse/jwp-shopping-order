package cart.exception;

import java.util.List;
import java.util.stream.Collectors;

public class CartItemNotFoundException extends RuntimeException{

    public CartItemNotFoundException(Long entityId) {
        super(String.format("존재하지 않는 CartItemEntity에 접근하였습니다. (접근 id값 : %d)", entityId));
    }

    public CartItemNotFoundException(List<Long> entityId) {
        super(String.format("존재하지 않는 CartItemEntity에 접근하였습니다. (접근 id값 : %s)",
                entityId.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","))
        ));
    }
}

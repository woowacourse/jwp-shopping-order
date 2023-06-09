package cart.exception;

import java.util.List;

public class NoSuchIdsException extends RuntimeException {

    public NoSuchIdsException(List<Long> ids) {
        super("존재하지 않는 id가 포함되어있습니다. ids=" + ids);
    }
}

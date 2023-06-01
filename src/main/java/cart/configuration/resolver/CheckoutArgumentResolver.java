package cart.configuration.resolver;

import cart.dto.request.CheckoutRequestParameter;
import cart.exception.CartItemException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CheckoutArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String CHECKOUT_PARAMETER_NAME = "ids";
    private static final String IDS_DELIMITER = ",";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Checkout.class)
                && parameter.getParameterType().equals(CheckoutRequestParameter.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        String ids = webRequest.getParameter(CHECKOUT_PARAMETER_NAME);

        validateIdsFormat(ids);

        return new CheckoutRequestParameter(mapToIds(ids));
    }

    private Long mapToId(String splitId) {
        try {
            return Long.parseLong(splitId);
        } catch (NumberFormatException e) {
            throw new CartItemException.InvalidIdsFormat("주문할 장바구니 상품 ID는 숫자여야 합니다.");
        }
    }

    private List<Long> mapToIds(String ids) {
        String[] splitIds = ids.split(IDS_DELIMITER);
        Set<Long> checkedCartItemIds = new LinkedHashSet<>();

        for (String splitId : splitIds) {
            checkedCartItemIds.add(mapToId(splitId));
        }

        validateDuplicateIds(splitIds, checkedCartItemIds);

        return new ArrayList<>(checkedCartItemIds);
    }

    private void validateDuplicateIds(String[] splitIds, Set<Long> checkedCartItemIds) {
        if (splitIds.length != checkedCartItemIds.size()) {
            throw new CartItemException.DuplicateIds();
        }
    }

    private void validateIdsFormat(String ids) {
        if (ids == null || ids.isEmpty() || ids.isBlank()) {
            throw new CartItemException.InvalidIdsFormat("주문할 장바구니 상품 ID를 입력해주세요.");
        }
    }
}

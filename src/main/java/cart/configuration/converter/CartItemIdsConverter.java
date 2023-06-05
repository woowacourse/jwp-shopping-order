package cart.configuration.converter;

import cart.exception.CartItemException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CartItemIdsConverter implements Converter<String, List<Long>> {

    private static final String IDS_DELIMITER = ",";

    @Override
    public List<Long> convert(String sourceIds) {
        validateIdsFormat(sourceIds);

        return mapToIds(sourceIds);
    }

    private void validateIdsFormat(String ids) {
        if (ids == null || ids.isEmpty() || ids.isBlank()) {
            throw new CartItemException.EmptyIds();
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

    private Long mapToId(String splitId) {
        try {
            return Long.parseLong(splitId);
        } catch (NumberFormatException e) {
            throw new CartItemException.InvalidIdsFormat();
        }
    }

    private void validateDuplicateIds(String[] splitIds, Set<Long> checkedCartItemIds) {
        if (splitIds.length != checkedCartItemIds.size()) {
            throw new CartItemException.DuplicateIds();
        }
    }
}

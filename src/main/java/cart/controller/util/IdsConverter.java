package cart.controller.util;

import cart.exception.IdTypeException;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IdsConverter {

    public static List<Long> convert(final List<String> ids) {
        return ids.stream()
                .map(parseLongWithExceptionHandling)
                .collect(Collectors.toUnmodifiableList());
    }

    private static final Function<String, Long> parseLongWithExceptionHandling = input -> {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new IdTypeException();
        }
    };
}

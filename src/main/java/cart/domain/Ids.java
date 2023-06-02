package cart.domain;

import cart.exception.IdTypeException;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Ids {

    private static final String DELIMITER = ",";
    private static final Function<String, Long> parseLongWithExceptionHandling = input -> {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new IdTypeException();
        }
    };

    private final List<Long> ids;

    private Ids(final List<Long> ids) {
        this.ids = ids;
    }

    public static Ids from(final String idsParameter) {
        final List<String> splitResult = List.of(idsParameter.split(DELIMITER));
        final List<Long> ids = splitResult.stream()
                .map(parseLongWithExceptionHandling)
                .collect(Collectors.toUnmodifiableList());
        return new Ids(ids);
    }

    public List<Long> getIds() {
        return ids;
    }
}

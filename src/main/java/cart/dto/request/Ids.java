package cart.dto.request;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Ids {
    private static final String DELIMITER = ",";

    private final List<Long> ids;

    private Ids(final List<Long> ids) {
        this.ids = ids;
    }

    public static Ids from(final String source) {
        final List<Long> ids = Arrays.stream(source.split(DELIMITER, -1))
                .mapToLong(Long::parseLong)
                .boxed()
                .collect(Collectors.toList());

        return new Ids(ids);
    }

    public boolean isEmpty() {
        return this.ids.isEmpty();
    }

    public List<Long> getIds() {
        return ids;
    }
}

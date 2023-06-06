package cart.ui;

import cart.dto.request.Ids;
import org.springframework.core.convert.converter.Converter;

public class IdsConverter implements Converter<String, Ids> {

    @Override
    public Ids convert(final String source) {
        return Ids.from(source);
    }
}

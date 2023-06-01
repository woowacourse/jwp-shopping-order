package common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;

public class TestUtils {

    public static String toBasicAuthHeaderValue(String from) {
        return "Basic " + Base64.encodeBase64String(from.getBytes());
    }

    public static String toJson(Object object) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

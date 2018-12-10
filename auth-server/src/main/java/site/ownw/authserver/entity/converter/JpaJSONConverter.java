package site.ownw.authserver.entity.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import java.io.IOException;

/**
 * @author sofior
 * @date 2018/10/26 11:21
 */
@Slf4j
public class JpaJSONConverter implements AttributeConverter<Object, String> {

    private final ObjectMapper objectMapper;

    public JpaJSONConverter() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        try {
            return StringUtils.isEmpty(dbData) ? null : objectMapper.readValue(dbData, Object.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

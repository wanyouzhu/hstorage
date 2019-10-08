package ltd.highsoft.framework.hstorage;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;

public class AggregateMarshaller {

    private final ObjectMapper mapper;

    public AggregateMarshaller(AggregateMapping mapping) {
        this.mapper = createMapper();
        mapping.entries().forEach(x -> mapper.addMixIn(x.modelClass(), x.mappingClass()));
    }

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParameterNamesModule());
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

    public String marshal(Object aggregate) {
        try {
            return mapper.writeValueAsString(aggregate);
        } catch (JsonProcessingException e) {
            throw new MappingException("Mapping error:", e);
        }
    }

    public <T> T unmarshal(String value, Class<T> clazz) {
        try {
            return mapper.readValue(value, clazz);
        } catch (JsonMappingException e) {
            throw new MappingException("Type '" + clazz.getName() + "' is not constructable!", e);
        } catch (IOException e) {
            throw new MalformedDataException("Malformed state data!", e);
        }
    }

}

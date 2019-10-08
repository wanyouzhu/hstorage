package ltd.highsoft.framework.hstorage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;
import java.util.Collection;

public class AggregateMarshaller {

    private final ObjectMapper mapper;

    public AggregateMarshaller(ModelMapping mapping) {
        this.mapper = createMapper(mapping.entries());
    }

    private ObjectMapper createMapper(Collection<MappingEntry> entries) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParameterNamesModule());
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        entries.forEach(x -> mapper.addMixIn(x.modelClass(), x.mappingClass()));
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

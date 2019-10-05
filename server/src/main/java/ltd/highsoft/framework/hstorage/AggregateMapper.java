package ltd.highsoft.framework.hstorage;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;

public class AggregateMapper {

    private final ObjectMapper mapper;
    private final TimeService timeService;

    public AggregateMapper(TimeService timeService) {
        this.timeService = timeService;
        this.mapper = createMapper();
    }

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParameterNamesModule());
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

    <T> T mapToAggregate(AggregateState state, Class<T> clazz) {
        try {
            return mapper.readValue(state.content(), clazz);
        } catch (JsonMappingException e) {
            throw new MappingException("Type '" + clazz.getName() + "' is not constructable!", e);
        } catch (IOException e) {
            throw new MalformedDataException("Malformed state data!", e);
        }
    }

    AggregateState mapToState(Object aggregate) {
        return new AggregateState(extractId(aggregate), asStateContent(aggregate), timeService.now());
    }

    private String asStateContent(Object aggregate) {
        try {
            return mapper.writeValueAsString(aggregate);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Mapping error: ", e);
        }
    }

    private String extractId(Object aggregate) {
        Field field = ReflectionUtils.findField(aggregate.getClass(), "id", String.class);
        if (field == null)
            throw new MappingException("Missing 'id' field in type '" + aggregate.getClass().getName() + "'!");
        ReflectionUtils.makeAccessible(field);
        return (String) ReflectionUtils.getField(field, aggregate);
    }

}
package ltd.highsoft.framework.hstore;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    <T> T mapToAggregate(Class<T> clazz, AggregateState state) {
        try {
            return mapper.readValue(state.content(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    AggregateState createAggregateState(Object aggregate) {
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
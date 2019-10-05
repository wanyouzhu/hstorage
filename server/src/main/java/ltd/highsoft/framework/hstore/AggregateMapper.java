package ltd.highsoft.framework.hstore;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;

public class AggregateMapper {

    ObjectMapper mapper;

    public AggregateMapper() {
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

}
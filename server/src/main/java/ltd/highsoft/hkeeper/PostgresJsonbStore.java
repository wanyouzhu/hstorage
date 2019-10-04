package ltd.highsoft.hkeeper;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

public class PostgresJsonbStore extends Store {

    private final JdbcOperations jdbcTemplate;
    private final ObjectMapper mapper;
    private final TimeService timeService;

    PostgresJsonbStore(JdbcOperations jdbcTemplate, TimeService timeService) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = createMapper();
        this.timeService = timeService;
    }

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParameterNamesModule());
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

    @Override
    public void save(Object aggregate) {
        saveSate(createAggregateState(aggregate));
    }

    @Override
    public <T> T load(String id, Class<T> clazz) {
        AggregateState state = loadState(id, clazz);
        try {
            return mapper.readValue(state.content(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AggregateState loadState(String id, Class<?> clazz) {
        try {
            String sql = "select id, state, timestamp from entities where id = ?";
            return jdbcTemplate.queryForObject(sql, new AggregateStateMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new AggregateNotFoundException("Aggregate '" + id + "' of type '" + clazz.getName() + "' does not exist!");
        }
    }

    private AggregateState createAggregateState(Object aggregate) {
        return new AggregateState(extractId(aggregate), asStateContent(aggregate), timeService.now());
    }

    private String extractId(Object aggregate) {
        Field field = ReflectionUtils.findField(aggregate.getClass(), "id", String.class);
        if (field == null)
            throw new MappingException("Missing 'id' field in type '" + aggregate.getClass().getName() + "'!");
        ReflectionUtils.makeAccessible(field);
        return (String) ReflectionUtils.getField(field, aggregate);
    }

    private String asStateContent(Object aggregate) {
        try {
            return mapper.writeValueAsString(aggregate);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Mapping error: ", e);
        }
    }

    private void saveSate(AggregateState state) {
        String sql = "insert into entities (id, state, timestamp) values (?, ?, ?)";
        Object[] args = {state.id(), state.content(), Timestamp.from(state.timestamp())};
        int[] types = {Types.CHAR, Types.OTHER, Types.TIMESTAMP};
        jdbcTemplate.update(sql, args, types);
    }

}

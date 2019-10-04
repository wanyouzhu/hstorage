package ltd.highsoft.hkeeper;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.*;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Types;

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
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

    @Override
    public void save(Object entity) {
        saveSate(createEntityState(entity));
    }

    private EntityState createEntityState(Object entity) {
        return new EntityState(extractId(entity), asContent(entity), timeService.now());
    }

    private Object extractId(Object entity) {
        Field field = ReflectionUtils.findField(entity.getClass(), "id");
        if (field == null) throw new MappingException("Missing 'id' field in type '" + entity.getClass().getName() + "'!");
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, entity);
    }

    private String asContent(Object entity) {
        try {
            return mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Mapping error: ", e);
        }
    }

    private void saveSate(EntityState state) {
        String sql = "insert into entities (id, state, timestamp) values (?, ?, ?)";
        jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setObject(1, state.id());
            ps.setObject(2, state.content(), Types.OTHER);
            ps.setObject(3, state.timestamp());
            return ps.execute();
        });
    }

}
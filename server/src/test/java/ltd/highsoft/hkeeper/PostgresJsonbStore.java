package ltd.highsoft.hkeeper;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;

import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

public class PostgresJsonbStore extends Store {
    private final JdbcOperations jdbcTemplate;
    private final ObjectMapper mapper;

    PostgresJsonbStore(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = createMapper();
    }

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

    @Override
    public void save(Object entity) {
        try {
            String json = mapper.writeValueAsString(entity);
            String id = UUID.randomUUID().toString();
            EntityState state = new EntityState(id, json, Instant.now());
            saveSate(state);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // FIXME
        }
    }

    private void saveSate(EntityState state) {
        String sql = "insert into entities (id, state, timestamp) values (?, ?, ?)";
        jdbcTemplate.execute(sql, (PreparedStatementCallback<Boolean>) ps -> {
            ps.setString(1, state.id());
            ps.setObject(2, state.content(), Types.OTHER);
            ps.setObject(3, state.timestamp());
            return ps.execute();
        });
    }

}

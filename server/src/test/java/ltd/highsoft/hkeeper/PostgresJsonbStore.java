package ltd.highsoft.hkeeper;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class PostgresJsonbStore extends Store {
    private final JdbcOperations jdbcTemplate;
    private final ObjectMapper mapper;

    PostgresJsonbStore(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        this.mapper = mapper;
    }

    @Override
    public void save(Object entity) {
        try {
            String json = mapper.writeValueAsString(entity);
            String id = UUID.randomUUID().toString();
            jdbcTemplate.execute("insert into entities (id, state, timestamp) values (?, ?, ?)", new PreparedStatementCallback<Void>() {
                @Override
                public Void doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1, id);
                    ps.setObject(2, json, Types.OTHER);
                    ps.setObject(3, OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
                    ps.execute();
                    return null;
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // FIXME
        }
    }
}

package ltd.highsoft.framework.hstorage;

import com.google.common.collect.ImmutableMap;
import org.springframework.dao.*;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.*;

import java.sql.*;

public class JdbcStatePersister implements StatePersister {

    private final NamedParameterJdbcOperations jdbcTemplate;

    public JdbcStatePersister(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public void saveState(AggregateState state) {
        String sql = "insert into entities (id, state, timestamp) values (:id, :state, :timestamp) on conflict(id) do update set state = :state, timestamp = :timestamp";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", state.id());
        parameterSource.addValue("state", state.content(), Types.OTHER);
        parameterSource.addValue("timestamp", Timestamp.from(state.timestamp()));
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public AggregateState loadState(String id, Class<?> clazz) {
        try {
            String sql = "select id, state, timestamp from entities where id = :id";
            return jdbcTemplate.queryForObject(sql, ImmutableMap.of("id", id), new AggregateStateRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new AggregateNotFoundException("Aggregate '" + id + "' of type '" + clazz.getName() + "' does not exist!");
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new MalformedDataException("Multiple rows associated to the key '" + id + "'!", e);
        }
    }

}
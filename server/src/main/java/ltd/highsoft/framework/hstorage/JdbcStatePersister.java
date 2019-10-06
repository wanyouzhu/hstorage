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
        jdbcTemplate.update(getSaveCommand(), getSaveArgs(state));
    }

    private String getSaveCommand() {
        return ("" +
            "insert into entities (id, state, timestamp) values (:id, :state, :timestamp) " +
            "on conflict(id) do update set state = :state, timestamp = :timestamp"
        );
    }

    private MapSqlParameterSource getSaveArgs(AggregateState state) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", state.id());
        parameterSource.addValue("state", state.content(), Types.OTHER);
        parameterSource.addValue("timestamp", Timestamp.from(state.timestamp()));
        return parameterSource;
    }

    @Override
    public AggregateState loadState(String collection, String id, Class<?> clazz) {
        try {
            return jdbcTemplate.queryForObject(getLoadCommand(), getLoadArgs(id), new AggregateStateRowMapper(collection));
        } catch (EmptyResultDataAccessException e) {
            throw new AggregateNotFoundException("Aggregate '" + id + "' of type '" + clazz.getName() + "' does not exist!");
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new MalformedDataException("Multiple rows associated to the key '" + id + "'!", e);
        }
    }

    private String getLoadCommand() {
        return "select id, state, timestamp from entities where id = :id";
    }

    private ImmutableMap<String, String> getLoadArgs(String id) {
        return ImmutableMap.of("id", id);
    }

}
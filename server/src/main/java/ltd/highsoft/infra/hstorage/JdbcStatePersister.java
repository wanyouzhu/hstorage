package ltd.highsoft.infra.hstorage;

import com.google.common.collect.ImmutableMap;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Timestamp;
import java.sql.Types;

public class JdbcStatePersister implements StatePersister {

    private final NamedParameterJdbcOperations jdbcTemplate;

    public JdbcStatePersister(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public void saveState(AggregateState state) {
        jdbcTemplate.update(getSaveCommand(state), getSaveArgs(state));
    }

    private String getSaveCommand(AggregateState state) {
        return ("" +
            "insert into " + state.collection() + " (id, state, timestamp) values (:id, :state, :timestamp) "
            + "on conflict(id) do update set state = :state, timestamp = :timestamp"
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
    public AggregateState loadState(String collection, String id) {
        try {
            return jdbcTemplate.queryForObject(getLoadCommand(collection), getLoadArgs(id), getRowMapper(collection));
        } catch (EmptyResultDataAccessException e) {
            throw new AggregateNotFoundException(
                "Aggregate '" + id + "' not found in collection '" + collection + "'!"
            );
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new MalformedDataException("Multiple rows associated with the key '" + id + "'!", e);
        }
    }

    private String getLoadCommand(String collection) {
        return "select id, state, timestamp from " + collection + " where id = :id";
    }

    private ImmutableMap<String, String> getLoadArgs(String id) {
        return ImmutableMap.of("id", id);
    }

    private AggregateStateRowMapper getRowMapper(String collection) {
        return new AggregateStateRowMapper(collection);
    }

}

package ltd.highsoft.framework.hstorage;

import org.springframework.dao.*;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.*;

public class JdbcStatePersister implements StatePersister {

    private final JdbcOperations jdbcTemplate;

    public JdbcStatePersister(JdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveState(AggregateState state) {
        String sql = "insert into entities (id, state, timestamp) values (?, ?, ?)";
        Object[] args = {state.id(), state.content(), Timestamp.from(state.timestamp())};
        int[] types = {Types.CHAR, Types.OTHER, Types.TIMESTAMP};
        jdbcTemplate.update(sql, args, types);
    }

    @Override
    public AggregateState loadState(String id, Class<?> clazz) {
        try {
            String sql = "select id, state, timestamp from entities where id = ?";
            return jdbcTemplate.queryForObject(sql, new AggregateStateRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new AggregateNotFoundException("Aggregate '" + id + "' of type '" + clazz.getName() + "' does not exist!");
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new MalformedDataException("Multiple rows associated to the key '" + id + "'!", e);
        }
    }

}
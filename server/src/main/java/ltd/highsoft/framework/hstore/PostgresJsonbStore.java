package ltd.highsoft.framework.hstore;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.*;

public class PostgresJsonbStore extends Store {

    private final JdbcOperations jdbcTemplate;
    private final TimeService timeService;
    private final AggregateMapper aggregateMapper;

    PostgresJsonbStore(JdbcOperations jdbcTemplate, TimeService timeService) {
        this.jdbcTemplate = jdbcTemplate;
        this.aggregateMapper = new AggregateMapper(timeService);
        this.timeService = timeService;
    }

    @Override
    public void save(Object aggregate) {
        saveState(aggregateMapper.createAggregateState(aggregate));
    }

    private void saveState(AggregateState state) {
        String sql = "insert into entities (id, state, timestamp) values (?, ?, ?)";
        Object[] args = {state.id(), state.content(), Timestamp.from(state.timestamp())};
        int[] types = {Types.CHAR, Types.OTHER, Types.TIMESTAMP};
        jdbcTemplate.update(sql, args, types);
    }

    private AggregateState loadState(String id, Class<?> clazz) {
        try {
            String sql = "select id, state, timestamp from entities where id = ?";
            return jdbcTemplate.queryForObject(sql, new AggregateStateRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new AggregateNotFoundException("Aggregate '" + id + "' of type '" + clazz.getName() + "' does not exist!");
        }
    }

    @Override
    public <T> T load(String id, Class<T> clazz) {
        return aggregateMapper.mapToAggregate(clazz, loadState(id, clazz));
    }

}

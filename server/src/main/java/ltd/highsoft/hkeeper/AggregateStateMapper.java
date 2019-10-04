package ltd.highsoft.hkeeper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.time.Instant;

public class AggregateStateMapper implements RowMapper<AggregateState> {

    @Override
    public AggregateState mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        String state = rs.getString("state");
        Instant timestamp = rs.getTimestamp("timestamp").toInstant();
        return new AggregateState(id, state, timestamp);
    }

}

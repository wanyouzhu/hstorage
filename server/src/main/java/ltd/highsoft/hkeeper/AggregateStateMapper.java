package ltd.highsoft.hkeeper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.time.Instant;

public class AggregateStateMapper implements RowMapper<EntityState> {

    @Override
    public EntityState mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("id");
        String state = rs.getString("state");
        Instant timestamp = rs.getTimestamp("timestamp").toInstant();
        return new EntityState(id, state, timestamp);
    }

}

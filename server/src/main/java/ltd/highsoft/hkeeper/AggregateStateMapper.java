package ltd.highsoft.hkeeper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.*;

public class AggregateStateMapper implements RowMapper<AggregateState> {

    @Override
    public AggregateState mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AggregateState(rs.getString("id"), rs.getString("state"), rs.getTimestamp("timestamp").toInstant());
    }

}

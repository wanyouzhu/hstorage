package ltd.highsoft.framework.hstorage;

import org.springframework.jdbc.core.RowMapper;

import java.sql.*;

public class AggregateStateRowMapper implements RowMapper<AggregateState> {

    @Override
    public AggregateState mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AggregateState(rs.getString("id"), rs.getString("state"), rs.getTimestamp("timestamp").toInstant());
    }

}

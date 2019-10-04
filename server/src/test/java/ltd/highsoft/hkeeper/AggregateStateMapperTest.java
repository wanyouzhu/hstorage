package ltd.highsoft.hkeeper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.time.Instant;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

class AggregateStateMapperTest {

    private static final String ID = "0001";
    private static final String STATE = "{\"id\":\"0001\",\"name\":\"Van\"}";
    private static final Timestamp TIMESTAMP = Timestamp.from(Instant.EPOCH);

    @Test
    void should_map_row_to_aggregate_state_correctly() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getString("id")).thenReturn(ID);
        when(resultSet.getString("state")).thenReturn(STATE);
        when(resultSet.getTimestamp("timestamp")).thenReturn(TIMESTAMP);
        AggregateStateMapper mapper = new AggregateStateMapper();
        AggregateState aggregateState = mapper.mapRow(resultSet, 1);
        assertThat(aggregateState).isEqualToComparingFieldByField(new AggregateState(ID, STATE, TIMESTAMP.toInstant()));
    }

}
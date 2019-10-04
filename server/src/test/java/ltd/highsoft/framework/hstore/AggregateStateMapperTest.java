package ltd.highsoft.framework.hstore;

import ltd.highsoft.framework.hstore.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.sql.*;
import java.time.Instant;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

class AggregateStateMapperTest {

    private static final String ID = "0001";
    private static final String STATE = "{\"id\":\"0001\",\"name\":\"Van\"}";
    private static final Timestamp TIMESTAMP = Timestamp.from(Instant.EPOCH);
    private ResultSet resultSet;
    private AggregateStateMapper mapper;

    @BeforeEach
    void setUp() throws SQLException {
        resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getString("id")).thenReturn(ID);
        when(resultSet.getString("state")).thenReturn(STATE);
        when(resultSet.getTimestamp("timestamp")).thenReturn(TIMESTAMP);
        mapper = new AggregateStateMapper();
    }

    @Test
    void should_map_row_to_aggregate_state_correctly() throws SQLException {
        AggregateState aggregateState = mapper.mapRow(resultSet, 1);
        assertThat(aggregateState).isEqualToComparingFieldByField(new AggregateState(ID, STATE, TIMESTAMP.toInstant()));
    }

}
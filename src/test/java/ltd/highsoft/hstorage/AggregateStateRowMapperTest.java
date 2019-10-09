package ltd.highsoft.hstorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

class AggregateStateRowMapperTest {

    private static final String ID = "0001";
    private static final String STATE = "{\"id\":\"0001\",\"name\":\"Van\"}";
    private static final Timestamp TIMESTAMP = Timestamp.from(Instant.EPOCH);
    private ResultSet resultSet;
    private AggregateStateRowMapper mapper;

    @BeforeEach
    void setUp() throws SQLException {
        resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getString("id")).thenReturn(ID);
        when(resultSet.getString("state")).thenReturn(STATE);
        when(resultSet.getTimestamp("timestamp")).thenReturn(TIMESTAMP);
        mapper = new AggregateStateRowMapper("entities");
    }

    @Test
    void should_be_able_to_map_row_to_aggregate_state() throws SQLException {
        AggregateState aggregateState = mapper.mapRow(resultSet, 1);
        AggregateState expect = new AggregateState("entities", ID, STATE, TIMESTAMP.toInstant());
        assertThat(aggregateState).isEqualToComparingFieldByField(expect);
    }

}
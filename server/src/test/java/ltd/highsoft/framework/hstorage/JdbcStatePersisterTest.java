package ltd.highsoft.framework.hstorage;

import org.junit.jupiter.api.*;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class JdbcStatePersisterTest {

    private TestDatabase testDatabase;
    private StatePersister persister;

    @BeforeEach
    void setUp() {
        testDatabase = new TestDatabase();
        testDatabase.recreateCollectionTable();
        persister = new JdbcStatePersister(testDatabase.jdbcTemplate());
    }

    @Test
    void should_be_able_to_save_state_to_database() {
        AggregateState state = new AggregateState("one", "{\"id\": \"one\"}", Instant.EPOCH);
        persister.saveState(state);
        Map<String, Object> loaded = testDatabase.getSavedAggregateState();
        assertThat(loaded.get("id")).isEqualTo("one");
        assertThat(loaded.get("state").toString()).isEqualTo("{\"id\": \"one\"}");
        assertThat(loaded.get("timestamp")).isEqualTo(Timestamp.from(Instant.EPOCH));
    }

    @Test
    void should_be_able_to_load_state_from_database() {
        AggregateState state = new AggregateState("one", "{\"id\": \"one\"}", Instant.EPOCH);
        persister.saveState(state);
        AggregateState loaded = persister.loadState("one", TestAggregate.class);
        assertThat(loaded).isEqualToComparingFieldByField(state);
    }

    @Test
    void should_update_state_if_state_exists_in_database_during_saving() {
        AggregateState state = new AggregateState("one", "{\"id\": \"one\"}", Instant.EPOCH);
        persister.saveState(state);
        AggregateState newState = new AggregateState("one", "{\"id\": \"two\"}", Instant.parse("2019-01-01T11:22:33Z"));
        persister.saveState(newState);
        AggregateState loaded = persister.loadState("one", TestAggregate.class);
        assertThat(loaded).isEqualToComparingFieldByField(newState);
    }

    @Test
    void should_fail_if_aggregate_state_not_found_in_database() {
        Throwable thrown = catchThrowable(() -> persister.loadState("nothing", TestAggregate.class));
        assertThat(thrown).isInstanceOf(AggregateNotFoundException.class);
        assertThat(thrown).hasMessage("Aggregate 'nothing' of type 'ltd.highsoft.framework.hstorage.TestAggregate' does not exist!");
    }

    @Test
    void should_reject_multiple_rows_from_database_during_loading() {
        testDatabase.recreateCollectionTableWithPrimaryKey();
        testDatabase.addTestData("one", "{\"id\": \"one\"}", Instant.EPOCH);
        testDatabase.addTestData("one", "{\"id\": \"one\"}", Instant.EPOCH);
        Throwable thrown = catchThrowable(() -> persister.loadState("one", TestAggregate.class));
        assertThat(thrown).isInstanceOf(MalformedDataException.class);
        assertThat(thrown).hasMessage("Multiple rows associated to the key 'one'!");
        assertThat(thrown).hasCauseInstanceOf(IncorrectResultSizeDataAccessException.class);
    }

}
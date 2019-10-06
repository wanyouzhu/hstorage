package ltd.highsoft.framework.hstorage;

import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class StorageTest {

    private TestDatabase testDatabase;
    private JdbcOperations jdbcTemplate;
    private TimeService timeService;
    private Storage storage;

    @BeforeEach
    void setUp() {
        testDatabase = new TestDatabase();
        testDatabase.recreateCollectionTable();
        timeService = new FixedTimeService(Instant.now());
        storage = new Storage(new JdbcStatePersister(testDatabase.jdbcTemplate()), timeService);
    }

    @Test
    void should_be_able_to_save_aggregate_state_into_database() {
        TestAggregate testAggregate = new TestAggregate("0001", "Van");
        storage.save(testAggregate);
        Map<String, Object> loaded = testDatabase.getSavedAggregateState();
        assertThat(loaded.get("id")).isEqualTo("0001");
        assertThat(loaded.get("state").toString()).isEqualTo("{\"id\": \"0001\", \"name\": \"Van\"}");
        assertThat(loaded.get("timestamp")).isEqualTo(Timestamp.from(timeService.now()));
    }

    @Test
    void should_be_able_to_load_aggregate_state_from_database() {
        TestAggregate testAggregate = new TestAggregate("0001", "Van");
        storage.save(testAggregate);
        TestAggregate loaded = storage.load("0001", TestAggregate.class);
        assertThat(loaded).isEqualToComparingFieldByField(testAggregate);
    }

    @Test
    void should_fail_if_aggregate_not_found_during_loading() {
        Throwable thrown = catchThrowable(() -> storage.load("non-existing-aggregate", TestAggregate.class));
        assertThat(thrown).isInstanceOf(AggregateNotFoundException.class);
        assertThat(thrown).hasMessage("Aggregate 'non-existing-aggregate' of type 'ltd.highsoft.framework.hstorage.TestAggregate' does not exist!");
    }

}

package ltd.highsoft.framework.hstorage;

import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class StorageTest {

    private TestDatabase testDatabase;
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

}

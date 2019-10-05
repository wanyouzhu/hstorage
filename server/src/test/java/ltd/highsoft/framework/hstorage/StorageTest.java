package ltd.highsoft.framework.hstorage;

import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

class StorageTest {

    private JdbcOperations jdbcTemplate;
    private TimeService timeService;
    private Storage storage;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(createTestDataSource());
        timeService = new FixedTimeService(Instant.now());
        storage = new Storage(new JdbcStatePersister(jdbcTemplate), timeService);
        recreateCollectionTable();
    }

    private DataSource createTestDataSource() {
        return new SingleConnectionDataSource(
            "jdbc:postgresql://localhost:5432/hkeeper",
            "postgres", "postgres", false
        );
    }

    @Test
    void should_be_able_to_save_aggregate_state_into_database() {
        TestAggregate testAggregate = new TestAggregate("0001", "Van");
        storage.save(testAggregate);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from entities");
        assertThat(rows.size()).isEqualTo(1);
        assertThat(rows.get(0).get("id")).isEqualTo("0001");
        assertThat(rows.get(0).get("state").toString()).isEqualTo("{\"id\": \"0001\", \"name\": \"Van\"}");
        assertThat(rows.get(0).get("timestamp")).isEqualTo(Timestamp.from(timeService.now()));
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

    private void recreateCollectionTable() {
        jdbcTemplate.execute("drop table if exists entities");
        jdbcTemplate.execute(
            "create table entities\n" +
                "(\n" +
                "    id        varchar(50) not null primary key,\n" +
                "    state     jsonb       not null,\n" +
                "    timestamp timestamp   not null\n" +
                ")"
        );
    }

}
